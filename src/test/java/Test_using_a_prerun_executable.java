import com.saucelabs.saucerest.SauceREST;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Test_using_a_prerun_executable
{
	@Rule
	public TestName testName = new TestName();
	
	static final String username = System.getenv("SAUCE_USERNAME");
	static final String accessKey = System.getenv("SAUCE_ACCESS_KEY");
	
	SauceREST api;
	URL url;
	DesiredCapabilities capabilities;
	RemoteWebDriver driver;

	@Before
	public void setup() throws IOException
	{
		/* specify the pre-run executable file to upload to sauce-storage */
		File prerun = new File("/tmp/prerun.bat");

		api = new SauceREST(username, accessKey);
		api.uploadFile(prerun);

		url = new URL("https://" + username + ":" + accessKey + "@ondemand.saucelabs.com:443/wd/hub");
		
		capabilities = DesiredCapabilities.chrome();
		capabilities.setCapability(CapabilityType.VERSION, "latest");
		capabilities.setCapability("platform", "Windows 10");
		capabilities.setCapability("name", this.getClass().getSimpleName() + " " + testName.getMethodName());
		
		/* add "prerun" to desired capabilities */
		/* use "sauce-storage:" for the uploaded file URL prefix" */
		capabilities.setCapability("prerun", "sauce-storage:prerun.bat");

		driver = new RemoteWebDriver(url, capabilities);
    }
	
	@Test
	public void with_sauce_storage()
	{
		driver.get("https://saucelabs.com");
	}
	
	@After
	public void teardown()
	{
		driver.quit();
	}
}

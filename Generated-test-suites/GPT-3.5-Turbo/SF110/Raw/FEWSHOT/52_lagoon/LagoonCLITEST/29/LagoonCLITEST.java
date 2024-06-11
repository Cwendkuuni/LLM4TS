
package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LagoonCLITest {
    
    @Test
    public void testMain() {
        // Test case when args length is less than 1
        LagoonCLI.main(new String[] {});
        // Test case when args length is 1
        LagoonCLI.main(new String[] {"file"});
        // Test case when args length is greater than 1 and args[1] equals "build"
        LagoonCLI.main(new String[] {"file", "build"});
        // Test case when args length is greater than 1 and args[1] equals "force"
        LagoonCLI.main(new String[] {"file", "force"});
        // Test case when args length is greater than 1 and args[1] is a valid interval
        LagoonCLI.main(new String[] {"file", "10"});
        // Test case when args length is greater than 1 and args[1] is not a valid interval
        LagoonCLI.main(new String[] {"file", "abc"});
        // Test case when args length is greater than 1 and interval is less than 1
        LagoonCLI.main(new String[] {"file", "0"});
        // Test case when args[0] ends with ".xml" and args[1] is a valid interval
        LagoonCLI.main(new String[] {"file.xml", "10"});
        // Test case when args[0] ends with ".xml" and args[1] is not a valid interval
        LagoonCLI.main(new String[] {"file.xml", "abc"});
        // Test case when args[0] ends with ".xml" and interval is less than 1
        LagoonCLI.main(new String[] {"file.xml", "0"});
    }

    @Test
    public void testGetProperty() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://localhost");
        LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
        LagoonCLI.properties.setProperty("sourceDir", "src");
        LagoonCLI.properties.setProperty("password", "password");

        assertEquals("http://localhost", LagoonCLI.getProperty("targetURL"));
        assertEquals("sitemap.xml", LagoonCLI.getProperty("sitemapFile"));
        assertEquals("src", LagoonCLI.getProperty("sourceDir"));
        assertEquals("password", LagoonCLI.getProperty("password"));

        LagoonCLI.properties.clear();
        try {
            LagoonCLI.getProperty("targetURL");
        } catch (LagoonException e) {
            assertEquals("Property targetURL not specified", e.getMessage());
        }
    }

    @Test
    public void testShowTime() {
        // Test case when time is less than 10 seconds
        LagoonCLI.showTime(5000L);
        // Test case when time is greater than 10 seconds
        LagoonCLI.showTime(20000L);
    }
}


package nu.staldal.lagoon;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class LagoonCLITest {

    public static Properties getTestProperties() {
        Properties properties = new Properties();
        properties.put("targetURL", "https://www.example.com");
        properties.put("sitemapFile", "sitemap.xml");
        properties.put("sourceDir", "src/main/java");
        properties.put("password", "password");
        return properties;
    }

    @Test
    public void testMain() {
        // Test with property file
        String[] args = {"property_file", "1000"};
        LagoonCLI.main(args);

        // Test with sitemap file and build option
        args = new String[]{"sitemap_file", "build"};
        LagoonCLI.main(args);

        // Test with sitemap file and force option
        args = new String[]{"sitemap_file", "force"};
        LagoonCLI.main(args);

        // Test with sitemap file and interval option
        args = new String[]{"sitemap_file", "2000"};
        LagoonCLI.main(args);

        // Test with incorrect interval format
        args = new String[]{"property_file", "interval"};
        LagoonCLI.main(args);

        // Test with interval less than 1
        args = new String[]{"property_file", "-1"};
        LagoonCLI.main(args);

        // Test with no command line arguments
        LagoonCLI.main(new String[0]);
    }

    @Test
    public void testGetProperty() {
        // Test with existing property
        LagoonCLI.properties = getTestProperties();
        String value = LagoonCLI.getProperty("targetURL");
        assertNotNull(value);
        assertEquals("https://www.example.com", value);

        // Test with non-existing property
        try {
            value = LagoonCLI.getProperty("nonExistingProperty");
        } catch (LagoonException e) {
            assertNull(value);
        }
    }

    @Test
    public void testShowTime() {
        // Test with time less than 10 seconds
        PrintWriter out = new PrintWriter(System.out);
        LagoonCLI.showTime(5000);
        assertEquals("in 5000 ms\n", out.toString());

        // Test with time more than 10 seconds
        out = new PrintWriter(System.out);
        LagoonCLI.showTime(15000);
        assertEquals("in 15 s\n", out.toString());
    }
    
    // Add additional test cases for stripLeadingAndTrailingQuotes() and stripLeadingHyphens() methods
    
}

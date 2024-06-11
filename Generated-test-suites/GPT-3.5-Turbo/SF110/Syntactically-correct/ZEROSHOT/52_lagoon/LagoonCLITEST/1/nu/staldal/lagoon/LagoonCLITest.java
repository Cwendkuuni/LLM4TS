import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonCLITest {

    @Test
    public void testMain() {
        // Test case 1: When no arguments are provided
        String[] args1 = {};
        LagoonCLI.main(args1);
        
        // Test case 2: When only property_file argument is provided
        String[] args2 = {"property_file"};
        LagoonCLI.main(args2);
        
        // Test case 3: When property_file and build argument are provided
        String[] args3 = {"property_file", "build"};
        LagoonCLI.main(args3);
        
        // Test case 4: When property_file and force argument are provided
        String[] args4 = {"property_file", "force"};
        LagoonCLI.main(args4);
        
        // Test case 5: When property_file and interval argument are provided
        String[] args5 = {"property_file", "interval"};
        LagoonCLI.main(args5);
        
        // Test case 6: When property_file, interval and build arguments are provided
        String[] args6 = {"property_file", "interval", "build"};
        LagoonCLI.main(args6);
        
        // Test case 7: When property_file, interval and force arguments are provided
        String[] args7 = {"property_file", "interval", "force"};
        LagoonCLI.main(args7);
        
        // Test case 8: When property_file, negative interval and build arguments are provided
        String[] args8 = {"property_file", "-interval", "build"};
        LagoonCLI.main(args8);
        
        // Test case 9: When property_file, interval and invalid build option are provided
        String[] args9 = {"property_file", "interval", "invalid"};
        LagoonCLI.main(args9);
        
        // Test case 10: When property_file, interval and invalid force option are provided
        String[] args10 = {"property_file", "interval", "invalid"};
        LagoonCLI.main(args10);
        
        // Test case 11: When proper file name is provided
        String[] args11 = {"valid_file_name"};
        LagoonCLI.main(args11);
        
        // Test case 12: When invalid file name is provided
        String[] args12 = {"invalid_file_name"};
        LagoonCLI.main(args12);
    }
    
    @Test
    public void testGetProperty() {
        // Test case 1: When a valid property name is provided
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://test.com");
        LagoonCLI.properties.setProperty("sitemapFile", "sitemap.xml");
        LagoonCLI.properties.setProperty("sourceDir", "src");
        String result1 = LagoonCLI.getProperty("targetURL");
        assertEquals("http://test.com", result1);
        
        // Test case 2: When an invalid property name is provided
        try {
            String result2 = LagoonCLI.getProperty("invalidProperty");
        } catch(Exception e) {
            assertTrue(e instanceof LagoonException);
        }
    }
    
    @Test
    public void testShowTime() {
        // Test case 1: When less than 10 seconds
        long ms1 = 5000;
        String expected1 = "in 5 s";
        LagoonCLI.showTime(ms1);
        
        // Test case 2: When more than 10 seconds
        long ms2 = 15000;
        String expected2 = "in 15 s";
        LagoonCLI.showTime(ms2);
    }
}
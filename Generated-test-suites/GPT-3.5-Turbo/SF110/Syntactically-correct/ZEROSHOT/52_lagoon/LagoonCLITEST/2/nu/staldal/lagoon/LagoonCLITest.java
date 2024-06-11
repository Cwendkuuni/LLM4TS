package nu.staldal.lagoon;

import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonCLITest {
    
    @Test
    public void testMain() {
        // Test case 1: No arguments
        String[] args1 = {};
        LagoonCLI.main(args1);
        // Check if the expected output is printed
        
        // Test case 2: Valid property file and valid interval number
        String[] args2 = {"property_file.txt", "10"};
        LagoonCLI.main(args2);
        // Check if the expected output is printed
        
        // Test case 3: Valid sitemap file and valid interval number
        String[] args3 = {"sitemap_file.xml", "5"};
        LagoonCLI.main(args3);
        // Check if the expected output is printed
        
        // Test case 4: Valid property file and build command
        String[] args4 = {"property_file.txt", "build"};
        LagoonCLI.main(args4);
        // Check if the expected output is printed
        
        // Test case 5: Valid sitemap file and force command
        String[] args5 = {"sitemap_file.xml", "force"};
        LagoonCLI.main(args5);
        // Check if the expected output is printed
        
        // Test case 6: Invalid property file
        String[] args6 = {"invalid_property_file.txt", "10"};
        LagoonCLI.main(args6);
        // Check if the expected output is printed
        
        // Test case 7: Invalid sitemap file
        String[] args7 = {"invalid_sitemap_file.xml", "5"};
        LagoonCLI.main(args7);
        // Check if the expected output is printed
        
    }
    
    @Test
    public void testGetProperty() {
        // Test case 1: Existing property name
        String name1 = "targetURL";
        String expectedValue1 = "http://example.com";
        try {
            String value1 = LagoonCLI.getProperty(name1);
            assertEquals(expectedValue1, value1);
        } catch (LagoonException e) {
            fail("Exception thrown: " + e.getMessage());
        }
        
        // Test case 2: Non-existing property name
        String name2 = "invalidProperty";
        try {
            LagoonCLI.getProperty(name2);
            fail("Expected LagoonException");
        } catch (LagoonException e) {
            // Exception is expected
        }
    }
    
    @Test
    public void testShowTime() {
        // Test case 1: Less than 10 seconds
        long ms1 = 5000;
        String expectedTime1 = "in 5 s";
        LagoonCLI.showTime(ms1);
        // Check if the expected output is printed
        
        // Test case 2: More than 10 seconds
        long ms2 = 15000;
        String expectedTime2 = "in 15 s";
        LagoonCLI.showTime(ms2);
        // Check if the expected output is printed
    }
}
package nu.staldal.lagoon;

import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonCLITest {
    @Test
    public void testMain() {
        // Test case 1: args length < 1
        String[] args1 = {};
        LagoonCLI.main(args1);
        
        // Test case 2: args[1] equals "build"
        String[] args2 = {"property_file", "build"};
        LagoonCLI.main(args2);
        
        // Test case 3: args[1] equals "force"
        String[] args3 = {"property_file", "force"};
        LagoonCLI.main(args3);
        
        // Test case 4: args[1] is a valid interval
        String[] args4 = {"property_file", "500"};
        LagoonCLI.main(args4);
        
        // Test case 5: args[1] is not a valid interval
        String[] args5 = {"property_file", "abc"};
        LagoonCLI.main(args5);
        
        // Test case 6: interval < 1L
        String[] args6 = {"property_file", "0"};
        LagoonCLI.main(args6);
        
        // Test case 7: sitemapFile ends with .xml
        String[] args7 = {"sitemap.xml", "build"};
        LagoonCLI.main(args7);
        
        // Test case 8: sitemapFile ends with .sitemap
        String[] args8 = {"sitemap.sitemap", "build"};
        LagoonCLI.main(args8);
        
        // Test case 9: FileNotFoundException
        String[] args9 = {"nonexistent.properties", "build"};
        LagoonCLI.main(args9);
        
        // Test case 10: IOException
        String[] args10 = {"property_file", "build"};
        LagoonCLI.main(args10);
        
        // Test case 11: LagoonException
        String[] args11 = {"property_file", "build"};
        LagoonCLI.main(args11);
    }
    
    @Test
    public void testGetProperty() {
        // Test case 1: value is null
        try {
            LagoonCLI.getProperty("targetURL");
            fail("Expected LagoonException");
        } catch (LagoonException e) {
            assertEquals("Property targetURL not specified", e.getMessage());
        }
        
        // Test case 2: value is not null
        LagoonCLI.properties.setProperty("targetURL", "https://example.com");
        try {
            String targetURL = LagoonCLI.getProperty("targetURL");
            assertEquals("https://example.com", targetURL);
        } catch (LagoonException e) {
            fail("Unexpected LagoonException");
        }
    }
    
    @Test
    public void testShowTime() {
        // Test case 1: ms < 10000L
        long ms1 = 5000L;
        ByteArrayOutputStream out1 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out1));
        LagoonCLI.showTime(ms1);
        assertEquals("in 5000 ms\n", out1.toString());
        
        // Test case 2: ms >= 10000L
        long ms2 = 15000L;
        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out2));
        LagoonCLI.showTime(ms2);
        assertEquals("in 15 s\n", out2.toString());
    }
}

package nu.staldal.lagoon;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class LagoonCLITest {

    @Test
    public void testMain() {
        // Test case 1
        String[] args1 = {"property_file"};
        LagoonCLI.main(args1);

        // Test case 2
        String[] args2 = {"property_file", "interval"};
        LagoonCLI.main(args2);

        // Test case 3
        String[] args3 = {"property_file", "build"};
        LagoonCLI.main(args3);
        
        // Test case 4
        String[] args4 = {"property_file", "force"};
        LagoonCLI.main(args4);
        
        // Test case 5
        String[] args5 = {"property_file"};
        LagoonCLI.main(args5);
        
        // Test case 6
        String[] args6 = {"property_file", "invalid_interval"};
        LagoonCLI.main(args6);
        
        // Test case 7
        String[] args7 = {"property_file", "-1"};
        LagoonCLI.main(args7);
        
        // Test case 8
        String[] args8 = {"sitemap_file"};
        LagoonCLI.main(args8);
        
        // Test case 9
        String[] args9 = {"sitemap_file", "interval"};
        LagoonCLI.main(args9);
        
        // Test case 10
        String[] args10 = {"sitemap_file", "build"};
        LagoonCLI.main(args10);
        
        // Test case 11
        String[] args11 = {"sitemap_file", "force"};
        LagoonCLI.main(args11);
        
        // Test case 12
        String[] args12 = {"sitemap_file"};
        LagoonCLI.main(args12);
        
        // Test case 13
        String[] args13 = {"sitemap_file", "invalid_interval"};
        LagoonCLI.main(args13);
        
        // Test case 14
        String[] args14 = {"sitemap_file", "-1"};
        LagoonCLI.main(args14);
    }
    
    @Test
    public void testGetProperty() throws LagoonException {
        // Test case 1
        LagoonCLI.properties.setProperty("targetURL", "http://example.com");
        String value1 = LagoonCLI.getProperty("targetURL");
        assertEquals("http://example.com", value1);
        
        // Test case 2
        LagoonCLI.properties.setProperty("targetURL", null);
        try {
            LagoonCLI.getProperty("targetURL");
        } catch (LagoonException e) {
            assertEquals("Property targetURL not specified", e.getMessage());
        }
    }
    
    @Test
    public void testShowTime() {
        // Test case 1
        long ms1 = 5000;
        CaptureOutput co1 = new CaptureOutput();
        co1.start();
        LagoonCLI.showTime(ms1);
        co1.stop();
        assertEquals("in 5 s", co1.getOutput());
        
        // Test case 2
        long ms2 = 500;
        CaptureOutput co2 = new CaptureOutput();
        co2.start();
        LagoonCLI.showTime(ms2);
        co2.stop();
        assertEquals("in 500 ms", co2.getOutput());
    }

}

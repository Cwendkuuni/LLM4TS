
import org.junit.*;

public class LagoonCLITest {

    @Test
    public void testMain() {
        // Test case 1: No arguments
        // Expected output: Syntax message is displayed
        LagoonCLI.main(new String[] {});
        
        // Test case 2: Valid property file, no interval/build/force
        // Expected output: Lagoon initialized successfully, Ready message is displayed
        LagoonCLI.main(new String[] {"property_file"});
        
        // Test case 3: Valid property file, build
        // Expected output: Lagoon initialized successfully, Building website message is displayed
        LagoonCLI.main(new String[] {"property_file", "build"});
        
        // Test case 4: Valid property file, force
        // Expected output: Lagoon initialized successfully, Force building website message is displayed
        LagoonCLI.main(new String[] {"property_file", "force"});
        
        // Test case 5: Valid property file, valid interval
        // Expected output: Lagoon initialized successfully, Building website message is displayed repeatedly
        LagoonCLI.main(new String[] {"property_file", "100"});
        
        // Test case 6: Valid sitemap file, no interval/build/force
        // Expected output: Lagoon initialized successfully, Ready message is displayed
        LagoonCLI.main(new String[] {"sitemap_file"});
        
        // Test case 7: Invalid interval
        // Expected output: Syntax message is displayed
        LagoonCLI.main(new String[] {"property_file", "invalid_interval"});
        
        // Test case 8: Invalid property file
        // Expected output: File not found error message is displayed
        LagoonCLI.main(new String[] {"invalid_property_file"});
    }
    
    @Test
    public void testGetProperty() throws LagoonException {
        // Test case 1: Valid property value
        // Expected output: Returns the property value
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("targetURL", "http://example.com");
        String value = LagoonCLI.getProperty("targetURL");
        Assert.assertEquals("http://example.com", value);
        
        // Test case 2: Missing property value
        // Expected output: Throws LagoonException with error message
        try {
            value = LagoonCLI.getProperty("missingProperty");
            Assert.fail("LagoonException should have been thrown");
        } catch (LagoonException e) {
            Assert.assertEquals("Property missingProperty not specified", e.getMessage());
        }
    }
    
    @Test
    public void testShowTime() {
        // Test case 1: Time less than 10,000 ms
        // Expected output: Prints time in ms
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(5000);
        String expectedOutput = "in 5000 ms\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
        
        // Test case 2: Time more than 10,000 ms
        // Expected output: Prints time in s
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(15000);
        expectedOutput = "in 15 s\n";
        Assert.assertEquals(expectedOutput, outContent.toString());
    }
}

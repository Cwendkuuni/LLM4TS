import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonCLITest {
    
    @Test
    public void testMain_NoArgs() {
        // Test case for main method with no arguments
        String[] args = {};
        LagoonCLI.main(args); // The main method outputs syntax message, no assertions needed
    }
    
    @Test
    public void testMain_ValidArgs() {
        // Test case for main method with valid arguments (property file)
        String[] args = {"example.properties"};
        LagoonCLI.main(args); // The main method should run successfully, no assertions needed
    }
    
    @Test
    public void testMain_InvalidArgs() {
        // Test case for main method with invalid arguments
        String[] args = {"invalid", "invalid"};
        LagoonCLI.main(args); // The main method should output syntax message, no assertions needed
    }
    
    @Test
    public void testGetProperty() {
        // Test case for getProperty method with valid property name
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("key", "value");
        try {
            String result = LagoonCLI.getProperty("key");
            assertEquals("value", result);
        } catch (LagoonException e) {
            fail("Exception thrown when not expected");
        }
    }
    
    @Test
    public void testGetProperty_NotFound() {
        // Test case for getProperty method with property name not found
        LagoonCLI.properties = new Properties();
        LagoonCLI.properties.setProperty("key", "value");
        try {
            String result = LagoonCLI.getProperty("invalid");
            fail("Expected LagoonException to be thrown");
        } catch (LagoonException e) {
            // Pass if exception is thrown
        }
    }
    
    @Test
    public void testShowTime_LessThan10Seconds() {
        // Test case for showTime method with time less than 10 seconds
        long time = 5000L;
        LagoonCLI.showTime(time); // The method outputs expected time message, no assertions needed
    }
    
    @Test
    public void testShowTime_MoreThan10Seconds() {
        // Test case for showTime method with time more than 10 seconds
        long time = 15000L;
        LagoonCLI.showTime(time); // The method outputs expected time message, no assertions needed
    }
}
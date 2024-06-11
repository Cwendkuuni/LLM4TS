import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonCLITest {

    @Test
    public void testMain() {
        // Test main method with valid arguments
        String[] args = {"property_file.xml", "build"};
        LagoonCLI.main(args);
        
        // Test main method with invalid arguments
        String[] args2 = {};
        LagoonCLI.main(args2);
    }

    @Test
    public void testGetProperty() {
        // Test getProperty method with valid property name
        String propertyValue = LagoonCLI.getProperty("targetURL");
        assertNotNull(propertyValue);

        // Test getProperty method with invalid property name
        try {
            LagoonCLI.getProperty("invalidProperty");
            fail("Expected LagoonException to be thrown");
        } catch (LagoonException e) {
            assertEquals("Property invalidProperty not specified", e.getMessage());
        }
    }

    @Test
    public void testShowTime() {
        // Test showTime method with small time value
        long ms = 5000L;
        String expected = "in 5 s";
        assertEquals(expected, LagoonCLI.showTime(ms));
        
        // Test showTime method with large time value
        long ms2 = 15000L;
        String expected2 = "in 15 s";
        assertEquals(expected2, LagoonCLI.showTime(ms2));
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class LagoonCLITest {

    @Test
    public void testMain() {
        // Test for valid input arguments
        String[] validArgs = {"property_file", "build"};
        LagoonCLI.main(validArgs);

        // Test for invalid input arguments
        String[] invalidArgs = {};
        LagoonCLI.main(invalidArgs);
    }

    @Test
    public void testGetProperty() {
        // Test for valid property name
        try {
            assertEquals("value1", LagoonCLI.getProperty("property1"));
        } catch (LagoonException e) {
            fail("Unexpected exception");
        }

        // Test for invalid property name
        try {
            LagoonCLI.getProperty("invalid_property");
            fail("Expected LagoonException");
        } catch (LagoonException e) {
            // expected
        }
    }

    @Test
    public void testShowTime() {
        // Test for time less than 10 seconds
        long time1 = 5000;
        assertEquals("in 5000 ms", LagoonCLI.showTime(time1));

        // Test for time more than 10 seconds
        long time2 = 15000;
        assertEquals("in 15 s", LagoonCLI.showTime(time2));
    }
}
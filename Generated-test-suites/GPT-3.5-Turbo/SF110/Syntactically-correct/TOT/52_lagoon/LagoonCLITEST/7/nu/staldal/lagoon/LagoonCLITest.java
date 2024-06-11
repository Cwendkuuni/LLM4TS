import org.junit.*;
import static org.junit.Assert.*;

public class LagoonCLITest {

    @Test
    public void testMain_InvalidArguments() {
        String[] args = new String[0];
        LagoonCLI.main(args);
        
        // Expecting "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]"
        // Check if the above message is printed to the console
    }

    @Test
    public void testMain_BuildOption() {
        String[] args = new String[2];
        args[0] = "property_file";
        args[1] = "build";
        LagoonCLI.main(args);
        
        // Expecting "Building website..."
        // Check if the above message is printed to the console
        // Expecting "Lagoon initialized successfully"
        // Check if the above message is printed to the console
        // Expecting "Building website..."
        // Check if the above message is printed to the console
        // Expecting "...with errors..."
        // Check if the above message is printed to the console
        // Expecting "Exiting Lagoon..."
        // Check if the above message is printed to the console
    }

    @Test
    public void testMain_ForceOption() {
        String[] args = new String[2];
        args[0] = "property_file";
        args[1] = "force";
        LagoonCLI.main(args);
        
        // Expecting "Force building website..."
        // Check if the above message is printed to the console
        // Expecting "Lagoon initialized successfully"
        // Check if the above message is printed to the console
        // Expecting "Building website..."
        // Check if the above message is printed to the console
        // Expecting "...with errors..."
        // Check if the above message is printed to the console
        // Expecting "Exiting Lagoon..."
        // Check if the above message is printed to the console
    }

    @Test
    public void testMain_IntervalOption_ValidInterval() {
        String[] args = new String[2];
        args[0] = "property_file";
        args[1] = "10";
        LagoonCLI.main(args);
        
        // Expecting "Building website..." to be printed twice with an interval of 10 milliseconds
        // Check if the above message is printed to the console twice with a time interval of 10 ms
        
        // Expecting "Exiting Lagoon..."
        // Check if the above message is printed to the console
    }

    @Test
    public void testMain_IntervalOption_InvalidInterval() {
        String[] args = new String[2];
        args[0] = "property_file";
        args[1] = "invalid_interval";
        LagoonCLI.main(args);
        
        // Expecting "Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]"
        // Check if the above message is printed to the console
    }

    @Test
    public void testMain_NoOption() {
        String[] args = new String[1];
        args[0] = "property_file";
        LagoonCLI.main(args);
        
        // Expecting "Ready" to be printed to the console and wait for user input
        // Check if the above message is printed to the console and program waits for user input
        // Expecting "Building website..."
        // Check if the above message is printed to the console
        // Expecting "Exiting Lagoon..."
        // Check if the above message is printed to the console
    }

    @Test
    public void testGetProperty_ValidProperty() {
        try {
            LagoonCLI.properties = new Properties();
            LagoonCLI.properties.setProperty("targetURL", "http://example.com");
            String value = LagoonCLI.getProperty("targetURL");
            assertEquals("http://example.com", value);
        } catch (LagoonException e) {
            fail("Unexpected LagoonException thrown");
        }
    }

    @Test(expected = LagoonException.class)
    public void testGetProperty_InvalidProperty() throws LagoonException {
        LagoonCLI.properties = new Properties();
        LagoonCLI.getProperty("invalidProperty");
    }

    @Test
    public void testShowTime_LessThan10Seconds() {
        long ms = 5000;
        String expected = "in 5 s";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(ms);
        assertEquals(expected, outContent.toString());
    }

    @Test
    public void testShowTime_MoreThan10Seconds() {
        long ms = 15000;
        String expected = "in 15 s";
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        LagoonCLI.showTime(ms);
        assertEquals(expected, outContent.toString());
    }
}
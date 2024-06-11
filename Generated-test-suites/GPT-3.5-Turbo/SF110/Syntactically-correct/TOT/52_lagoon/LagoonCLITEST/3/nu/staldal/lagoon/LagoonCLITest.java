import org.junit.*;
import static org.junit.Assert.*;


public class LagoonCLITest {

    @Test
    public void testMain_InvalidArgs() {
        // Test when no arguments are provided
        // The program should display the syntax message and exit
        LagoonCLI.main(new String[]{});
    }

    @Test
    public void testMain_BuildArg() {
        // Test when the build argument is provided
        // The program should build the website without errors
        // The output should show the time elapsed
        LagoonCLI.main(new String[]{"property_file", "build"});
    }

    @Test
    public void testMain_ForceArg() {
        // Test when the force argument is provided
        // The program should force build the website without errors
        // The output should show the time elapsed
        LagoonCLI.main(new String[]{"property_file", "force"});
    }

    @Test
    public void testMain_IntervalArg() {
        // Test when an interval argument is provided
        // The program should build the website at regular intervals without errors
        // The output should show the time elapsed between each build
        LagoonCLI.main(new String[]{"property_file", "1000"});
    }

    @Test
    public void testMain_InvalidIntervalArg() {
        // Test when an invalid interval argument is provided
        // The program should display the syntax message and exit
        LagoonCLI.main(new String[]{"property_file", "invalid"});
    }

    @Test
    public void testMain_PropertyFileNotFound() {
        // Test when the specified property file is not found
        // The program should display an error message and exit
        LagoonCLI.main(new String[]{"nonexistent_file", "interval"});
    }

    @Test
    public void testMain_IOException() {
        // Test when an IOException occurs while initializing Lagoon
        // The program should display an error message and exit
        LagoonCLI.main(new String[]{"property_file", "interval"});
    }

    @Test
    public void testMain_BuildWithErrors() {
        // Test when the build argument is provided and building the website results in errors
        // The program should display an error message indicating the errors
        LagoonCLI.main(new String[]{"property_file", "build_with_errors"});
    }

    @Test
    public void testMain_ForceBuildWithErrors() {
        // Test when the force argument is provided and building the website results in errors
        // The program should display an error message indicating the errors
        LagoonCLI.main(new String[]{"property_file", "force_build_with_errors"});
    }

    @Test
    public void testMain_UserInput_Build() {
        // Test when the user inputs 'b' to trigger a build
        // The program should build the website without errors
        // The output should show the time elapsed
        LagoonCLI.main(new String[]{"property_file", "user_input_build"});
    }

    @Test
    public void testMain_UserInput_ForceBuild() {
        // Test when the user inputs 'f' to trigger a force build
        // The program should force build the website without errors
        // The output should show the time elapsed
        LagoonCLI.main(new String[]{"property_file", "user_input_force_build"});
    }

    @Test
    public void testMain_UserInput_Quit() {
        // Test when the user inputs 'q' to quit the program
        // The program should exit without any further actions
        LagoonCLI.main(new String[]{"property_file", "user_input_quit"});
    }

    @Test
    public void testMain_ThreadStillRunning() {
        // Test when the program finishes execution but there are still threads running
        // The program should display a message indicating the running threads
        LagoonCLI.main(new String[]{"property_file", "threads_running"});
    }

    @Test
    public void testGetProperty() {
        // Test when retrieving a property value from the properties object
        // The method should return the expected property value
        String propertyValue = LagoonCLI.getProperty("property_name");
        assertEquals("expected_value", propertyValue);
    }

    @Test(expected = LagoonException.class)
    public void testGetProperty_PropertyNotFound() {
        // Test when retrieving a property value that is not specified
        // The method should throw a LagoonException
        LagoonCLI.getProperty("nonexistent_property");
    }

    @Test
    public void testShowTime_LessThan10Seconds() {
        // Test when the time elapsed is less than 10 seconds
        // The output should show the time in milliseconds
        String expectedOutput = "in " + 5000 + " ms";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        LagoonCLI.showTime(5000);
        assertEquals(expectedOutput, outputStream.toString().trim());
    }

    @Test
    public void testShowTime_GreaterThan10Seconds() {
        // Test when the time elapsed is more than 10 seconds
        // The output should show the time in seconds
        String expectedOutput = "in " + 15 + " s";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        LagoonCLI.showTime(15000);
        assertEquals(expectedOutput, outputStream.toString().trim());
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class LagoonCLITest {

    @Test
    public void testMain_invalidArguments() {
        // Test when main method is called with invalid arguments
        // This should print the syntax message and return
    }

    @Test
    public void testMain_buildOption() {
        // Test when main method is called with build option
        // This should build the website without errors
    }

    @Test
    public void testMain_forceOption() {
        // Test when main method is called with force option
        // This should force build the website without errors
    }

    @Test
    public void testMain_intervalOption() {
        // Test when main method is called with interval option
        // This should build the website at specified intervals
    }

    // Add more test cases for main method as per edge cases and error scenarios

    @Test
    public void testGetProperty_validProperty() {
        // Test getting property value from properties
        // This should return the property value
    }

    @Test(expected = LagoonException.class)
    public void testGetProperty_invalidProperty() throws LagoonException {
        // Test getting property value for invalid property
        // This should throw LagoonException
        LagoonCLI.getProperty("invalidProperty");
    }

    @Test
    public void testShowTime_lessThanTenSeconds() {
        // Test showing time in milliseconds
        // This should print time in milliseconds
    }

    @Test
    public void testShowTime_moreThanTenSeconds() {
        // Test showing time in seconds
        // This should print time in seconds
    }

    // Add more test cases for edge cases and error scenarios for getProperty and showTime methods

}
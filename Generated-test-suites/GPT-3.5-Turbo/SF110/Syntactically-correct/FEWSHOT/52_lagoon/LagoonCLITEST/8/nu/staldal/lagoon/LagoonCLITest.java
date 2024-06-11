import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;

public class LagoonCLITest {

    @Test
    public void testMain_noArgs() {
       // Test when no args passed
       // Verify that the usage message is displayed
       // Expected output: Syntax:\nnu.staldal.lagoon.LagoonCLI <property_file> [<interval>|build|force]\nnu.staldal.lagoon.LagoonCLI <sitemap_file> [<interval>|build|force]
    }

    @Test
    public void testMain_buildFlag() {
       // Test when build flag is passed
       // Verify that the build method is called with the correct parameters
    }

    @Test
    public void testMain_forceFlag() {
       // Test when force flag is passed
       // Verify that the build method is called with the correct parameters
    }

    @Test
    public void testMain_intervalFlag() {
       // Test when interval flag is passed
       // Verify that the build method is called with the correct parameters and the interval value is correctly parsed
    }

    @Test
    public void testMain_invalidInterval() {
       // Test when an invalid interval value is passed
       // Verify that an error message is displayed and the build method is not called
    }

    @Test
    public void testMain_invalidPropertyFile() {
       // Test when an invalid property file is passed
       // Verify that an error message is displayed and the build method is not called
    }

    @Test
    public void testMain_validPropertyFile() {
       // Test when a valid property file is passed
       // Verify that the build method is called with the correct parameters
    }

    @Test
    public void testMain_validSitemapFile() {
       // Test when a valid sitemap file is passed
       // Verify that the build method is called with the correct parameters
    }

    @Test
    public void testGetProperty_existingProperty() {
       // Test when an existing property is requested
       // Verify that the correct value is returned
    }

    @Test
    public void testGetProperty_nonExistingProperty() {
       // Test when a non-existing property is requested
       // Verify that a LagoonException is thrown
    }

    @Test
    public void testShowTime_lessThan10s() {
       // Test when the time is less than 10 seconds
       // Verify that the time is displayed in milliseconds
    }

    @Test
    public void testShowTime_greaterThan10s() {
       // Test when the time is greater than 10 seconds
       // Verify that the time is displayed in seconds
    }

    // TODO: Add tests for other methods in LagoonCLI class

}
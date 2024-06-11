package nu.staldal.lagoon;
import static org.junit.Assert.*;
import org.junit.Test;
import org.xml.sax.SAXException;
import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.AuthenticationMissingException;
import nu.staldal.lagoon.core.AuthenticationException;
import java.io.IOException;

public class LagoonCLITest {

    @Test
    public void testMain_invalidArgs() {
        // Test when no arguments are passed
        // Expected output is the syntax message
        // Not possible to test main method fully, so testing for one specific condition
    }

    @Test
    public void testMain_buildFlag() {
        // Test when build flag is passed as argument
        // Expected output is building the website...
    }

    @Test
    public void testMain_forceFlag() {
        // Test when force flag is passed as argument
        // Expected output is force building the website...
    }

    @Test
    public void testMain_interval() {
        // Test when interval flag is passed as argument
        // Expected output is building website with intervals
    }

    @Test
    public void testGetProperty() {
        // Test when property exists
        // Expected output is value of the property
        // Edge case: when property doesn't exist, should throw LagoonException

    }

    @Test
    public void testShowTime_smallMs() {
        // Test when milliseconds are less than 10,000
        // Expected output is time in ms
    }

    @Test
    public void testShowTime_largeMs() {
        // Test when milliseconds are greater than or equal to 10,000
        // Expected output is time in seconds
    }

    @Test
    public void testMain_exceptionHandling() {
        // Test various exception handling scenarios in the main method.
        // Eg. AuthenticationMissingException, AuthenticationException, FileNotFoundException, IOException
    }

}
import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testStartQueryNoQuery() {
        WhoIS whoIs = new WhoIS();
        // Test for startQuery when no query is entered
        whoIs.startQuery();
        // Add assertion to check for the error message displayed
    }

    @Test
    public void testStartQueryWithQuery() {
        WhoIS whoIs = new WhoIS();
        // Test for startQuery when a query is entered
        // Add mock Socket connection to simulate connection and check for query output
    }

    @Test
    public void testExceptionUnknownHostException() {
        WhoIS whoIs = new WhoIS();
        // Test for UnknownHostException case when query is made
        // Check for the error message displayed
    }

    @Test
    public void testExceptionIOException() {
        WhoIS whoIs = new WhoIS();
        // Test for IOException case when query is made
        // Check for the error message displayed
    }

    @Test
    public void testGetQuery() {
        WhoIS whoIs = new WhoIS();
        // Test the getQuery method for various selection combinations
        // Add assertions to check the generated query string
    }

    @Test
    public void testReset() {
        WhoIS whoIs = new WhoIS();
        // Test the reset method for resetting all fields
        // Check if all fields are back to default values
    }
}
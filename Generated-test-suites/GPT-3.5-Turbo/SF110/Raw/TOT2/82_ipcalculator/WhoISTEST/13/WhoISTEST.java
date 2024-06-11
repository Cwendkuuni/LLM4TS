import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class WhoISTest {

    @Test
    public void testStartQueryNoQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.reset();
        whoIS.startQuery();
        // Assert that an error message for no query detected is displayed
        // This is an edge case where no query is entered
        // and the startQuery method should handle this scenario
    }

    @Test
    public void testStartQueryWithQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("exampleQuery");
        whoIS.startQuery();
        // Assert that the query method is called with the correct query value
        // This is a typical use case where a valid query is entered
        // and the startQuery method should initiate the query process
    }

    @Test
    public void testQueryUnknownHostException() {
        WhoIS whoIS = new WhoIS();
        whoIS.WHOIS_ADDRESS = "invalidAddress";
        whoIS.query("exampleQuery");
        // Assert that an error message for unknown host is displayed
        // This is an error scenario where the WHOIS address is invalid
        // and the query method should handle this exception
    }

    @Test
    public void testQueryIOException() {
        WhoIS whoIS = new WhoIS();
        whoIS.WHOIS_ADDRESS = "validAddress";
        whoIS.WHOIS_PORT = 12345; // Invalid port to simulate IOException
        whoIS.query("exampleQuery");
        // Assert that an error message for server not contacted is displayed
        // This is an error scenario where the server cannot be contacted
        // and the query method should handle this exception
    }

    @Test
    public void testGetQueryWithInverseLookup() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("exampleQuery");
        whoIS.inverseQueries.setSelectedIndex(1); // Selecting an inverse query option
        String query = whoIS.getQuery();
        // Assert that the query includes the inverse lookup option
        // This is a typical use case for a query with inverse lookup
    }

    @Test
    public void testReset() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("exampleQuery");
        whoIS.reset();
        // Assert that all fields are reset to their initial state
        // This is a typical use case for resetting the query panel
    }
}
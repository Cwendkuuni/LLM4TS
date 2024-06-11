import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testStartQueryWithEmptyQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        // Assert that an error dialog is displayed with the message "No Query Detected"
    }

    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example");
        whoIS.startQuery();
        // Assert that the query method is called with the correct query
    }

    @Test
    public void testStartQueryWithUnknownHostException() {
        WhoIS whoIS = new WhoIS();
        whoIS.WHOIS_ADDRESS = "unknownhost";
        whoIS.queryTextField.setText("example");
        whoIS.startQuery();
        // Assert that an error dialog is displayed with the message "Unknown Whois host: unknownhost"
    }

    @Test
    public void testStartQueryWithIOException() {
        WhoIS whoIS = new WhoIS();
        whoIS.WHOIS_ADDRESS = "validhost";
        // Disconnect the internet connection to simulate an IOException
        whoIS.queryTextField.setText("example");
        whoIS.startQuery();
        // Assert that an error dialog is displayed with the message "Whois server could not be contacted.\nPlease check your internet connection."
    }

    @Test
    public void testGetQueryWithNoInverseQueries() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example");
        String query = whoIS.getQuery();
        // Assert that the returned query is correct without inverse queries
    }

    @Test
    public void testGetQueryWithInverseQueries() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example");
        whoIS.inverseQueries.setSelectedIndex(1);
        String query = whoIS.getQuery();
        // Assert that the returned query is correct with inverse queries
    }

    @Test
    public void testGetQueryWithLowerCaseL() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example");
        whoIS.lowercaseL.setSelected(true);
        String query = whoIS.getQuery();
        // Assert that the returned query is correct with lowercase L
    }

    @Test
    public void testGetQueryWithUpperCaseL() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example");
        whoIS.uppercaseL.setSelected(true);
        String query = whoIS.getQuery();
        // Assert that the returned query is correct with uppercase L
    }

    @Test
    public void testGetQueryWithLowerCaseM() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example");
        whoIS.lowercaseM.setSelected(true);
        String query = whoIS.getQuery();
        // Assert that the returned query is correct with lowercase M
    }

    @Test
    public void testGetQueryWithUpperCaseM() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example");
        whoIS.uppercaseM.setSelected(true);
        String query = whoIS.getQuery();
        // Assert that the returned query is correct with uppercase M
    }

    @Test
    public void testGetQueryWithLowerCaseX() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example");
        whoIS.lowercaseX.setSelected(true);
        String query = whoIS.getQuery();
        // Assert that the returned query is correct with lowercase X
    }

    @Test
    public void testGetQueryWithBooleanD() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example");
        whoIS.lowercaseD.setSelected(true);
        String query = whoIS.getQuery();
        // Assert that the returned query is correct with boolean D
    }

    @Test
    public void testGetQueryWithMiscQueries() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example");
        whoIS.miscList.setSelectedIndex(1); // Select "as-block" from the miscList
        String query = whoIS.getQuery();
        // Assert that the returned query is correct with misc queries
    }

    @Test
    public void testReset() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example");
        whoIS.inverseQueries.setSelectedIndex(1);
        whoIS.miscList.setSelectedIndex(1);
        whoIS.reset();
        // Assert that all fields are reset to default values
    }
}
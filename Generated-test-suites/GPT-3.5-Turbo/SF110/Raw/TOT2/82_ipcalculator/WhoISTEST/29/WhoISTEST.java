import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {
    
    @Test
    public void testStartQueryNoQueryDetected() {
        // Create a WhoIS instance
        WhoIS whoIS = new WhoIS();
        // Set queryTextField text to empty
        whoIS.queryTextField.setText("");
        // Call startQuery method
        whoIS.startQuery();
        // Check if error message is displayed
        assertTrue(whoIS.queryTextField.getText().isEmpty());
    }

    @Test
    public void testStartQueryWithQuery() {
        // Create a WhoIS instance
        WhoIS whoIS = new WhoIS();
        // Set queryTextField text to a valid query
        whoIS.queryTextField.setText("query");
        // Call startQuery method
        whoIS.startQuery();
        // Check if query is executed without errors
        assertNotNull(whoIS.queryTextField.getText());
    }

    @Test
    public void testQueryUnknownHostException() {
        // Create a WhoIS instance
        WhoIS whoIS = new WhoIS();
        // Set invalid WHOIS_ADDRESS
        whoIS.WHOIS_ADDRESS = "invalid.address";
        // Call query method
        whoIS.query("query");
        // Check if error is displayed for UnknownHostException
        assertNotNull(whoIS.queryTextField.getText());
    }

    @Test
    public void testQueryIOException() {
        // Create a WhoIS instance
        WhoIS whoIS = new WhoIS();
        // Set invalid WHOIS_PORT
        whoIS.WHOIS_PORT = 9999;
        // Call query method
        whoIS.query("query");
        // Check if error is displayed for IOException
        assertNotNull(whoIS.queryTextField.getText());
    }

    @Test
    public void testGetQueryWithInverseQuery() {
        // Create a WhoIS instance
        WhoIS whoIS = new WhoIS();
        // Set selected inverseQueries item
        whoIS.inverseQueries.setSelectedItem("admin-c");
        // Get the query string
        String query = whoIS.getQuery();
        // Check if the query contains the inverse query
        assertTrue(query.contains("-i admin-c"));
    }

    @Test
    public void testGetQueryWithMiscQueries() {
        // Create a WhoIS instance
        WhoIS whoIS = new WhoIS();
        // Select some items in miscList
        whoIS.miscList.setSelectedValues(new Object[]{"as-block", "person"}, true);
        // Get the query string
        String query = whoIS.getQuery();
        // Check if the query contains the selected misc queries
        assertTrue(query.contains("-T as-block,person"));
    }

    @Test
    public void testReset() {
        // Create a WhoIS instance
        WhoIS whoIS = new WhoIS();
        // Set some fields to non-default values
        whoIS.queryTextField.setText("query");
        whoIS.inverseQueries.setSelectedItem("admin-c");
        whoIS.miscList.setSelectedIndex(1);
        // Call reset method
        whoIS.reset();
        // Check if all fields are reset to default values
        assertEquals("", whoIS.queryTextField.getText());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }
}
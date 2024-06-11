package ipac;

import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testStartQuery() {
        // Test case for starting a query with a non-empty query text
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example.com");
        whoIS.startQuery();
        // Assertion to confirm that the query is started
        // Implement additional assertions as needed to validate the behavior
    }

    @Test
    public void testStartQueryNoQuery() {
        // Test case for starting a query with an empty query text
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        // Assertion to confirm that an error is displayed
        // Implement additional assertions as needed to validate the behavior
    }

    @Test
    public void testQueryUnknownHostException() {
        // Test case for handling UnknownHostException when contacting Whois server
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example.com");
        whoIS.WHOIS_ADDRESS = "unknownhost";
        whoIS.startQuery();
        // Assertion to confirm that an error is displayed
        // Implement additional assertions as needed to validate the behavior
    }

    @Test
    public void testQueryIOException() {
        // Test case for handling IOException when contacting Whois server
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example.com");
        whoIS.WHOIS_PORT = -1; // Invalid port number to simulate IOException
        whoIS.startQuery();
        // Assertion to confirm that an error is displayed
        // Implement additional assertions as needed to validate the behavior
    }

    @Test
    public void testGetQuery() {
        // Test case for getting the query string
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example.com");
        String query = whoIS.getQuery();
        // Assertion to check the generated query string
        assertEquals("example.com", query);
        // Implement additional assertions as needed to validate the behavior
    }

    @Test
    public void testReset() {
        // Test case for resetting the form
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("example.com");
        whoIS.inverseQueries.setSelectedIndex(1);
        whoIS.miscList.setSelectedIndex(1);
        whoIS.reset();
        // Assertions to check that the form elements are reset to default values
        assertEquals("", whoIS.queryTextField.getText());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
        // Implement additional assertions as needed to validate the behavior
    }
    
}
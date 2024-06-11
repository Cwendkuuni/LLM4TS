import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {
    
    @Test
    public void testCreateWhoIS() {
        WhoIS whoIS = new WhoIS();
        assertNotNull(whoIS);
    }
    
    @Test
    public void testStartQueryWithNoQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        // Expect an error dialog pop-up with message "No Query Detected"
        // Assertion on error dialog display
    }
    
    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("test query");
        whoIS.startQuery();
        // Expect a successful query execution
        // Assertion on query execution
    }
    
    @Test
    public void testQueryUnknownHostException() {
        WhoIS whoIS = new WhoIS();
        whoIS.WHOIS_ADDRESS = "invalidhost";
        whoIS.query("test query");
        // Expect an error dialog pop-up with message "Unknown Whois host: invalidhost"
        // Assertion on error dialog display
    }
    
    @Test
    public void testQueryIOException() {
        WhoIS whoIS = new WhoIS();
        whoIS.WHOIS_ADDRESS = "whois.apnic.net"; // Valid host to trigger IOException
        whoIS.WHOIS_PORT = 9999; // Invalid port to trigger IOException
        whoIS.query("test query");
        // Expect an error dialog pop-up with message "Whois server could not be contacted.\nPlease check your internet connection."
        // Assertion on error dialog display
    }
    
    @Test
    public void testGetQuery() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("test query");
        String expectedQuery = "test query";
        assertEquals(expectedQuery, whoIS.getQuery());
    }
    
    @Test
    public void testReset() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("test query");
        whoIS.reset();
        assertEquals("", whoIS.queryTextField.getText());
        assertTrue(whoIS.resetRadioButton.isSelected());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }
}
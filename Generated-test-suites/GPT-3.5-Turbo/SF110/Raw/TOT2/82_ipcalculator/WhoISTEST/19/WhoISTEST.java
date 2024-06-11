import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {

    @Test
    public void testStartQueryWithEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("");
        whois.startQuery();
        // Check if error message is displayed for empty query
    }

    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("google.com");
        whois.startQuery();
        // Check if query method is called with valid input
    }

    @Test
    public void testStartQueryWithInvalidHost() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "invalidHost";
        whois.queryTextField.setText("google.com");
        whois.startQuery();
        // Check if error message is displayed for unknown host
    }

    @Test
    public void testStartQueryWithIOException() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "whois.apnic.net";
        whois.WHOIS_PORT = 12345; // Invalid port
        whois.queryTextField.setText("google.com");
        whois.startQuery();
        // Check if error message is displayed for IO exception
    }

    @Test
    public void testGetQueryWithInverseLookup() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("google.com");
        whois.inverseQueries.setSelectedIndex(1); // Select inverse lookup option
        String query = whois.getQuery();
        // Check if query includes inverse lookup option
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();

        whois.queryTextField.setText("test query");
        whois.inverseQueries.setSelectedIndex(1); // Select inverse lookup
        whois.lowercaseL.setSelected(true); // Select lowercaseL option

        whois.reset();

        // Check if fields are reset to default values
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}
package ipac;

import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {

    @Test
    public void testStartQuery() {
        // Typical Use Case
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        // Edge Case: Empty Query
        whois.queryTextField.setText("");
        whois.startQuery();
        // Edge Case: Invalid Query
        whois.queryTextField.setText("$@#%");
        whois.startQuery();
    }

    @Test
    public void testDisplayError() {
        // Typical Use Case
        WhoIS whois = new WhoIS();
        whois.displayError("Error Message");
        // Edge Case: Null Error Message
        whois.displayError(null);
    }

    @Test
    public void testQuery() {
        // Typical Use Case
        WhoIS whois = new WhoIS();
        whois.query("example.com");
        // Edge Case: Unknown Host Exception
        whois.WHOIS_ADDRESS = "unknownhost.com";
        whois.query("example.com");
        // Edge Case: IOException
        whois.WHOIS_ADDRESS = "whois.apnic.net";
        whois.WHOIS_PORT = 80; // Invalid Port
        whois.query("example.com");
    }

    @Test
    public void testGetQuery() {
        // Typical Use Case
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        String query = whois.getQuery();
        assertEquals(query, "example.com");
        // Edge Case: Inverse Query
        whois.inverseQueries.setSelectedItem("admin-c");
        query = whois.getQuery();
        assertEquals(query, "-i admin-c example.com");
        // Edge Case: Multiple Object Types Selected
        whois.miscList.setSelectedIndex(1);
        whois.miscList.setSelectedIndex(3);
        query = whois.getQuery();
        assertEquals(query, "-T as-set,aut-num example.com");
    }

    @Test
    public void testReset() {
        // Typical Use Case
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.miscList.setSelectedIndex(1);
        whois.reset();
        assertEquals(whois.queryTextField.getText(), "");
        assertEquals(whois.resetRadioButton.isSelected(), true);
        assertEquals(whois.lowercaseD.isSelected(), false);
        assertEquals(whois.inverseQueries.getSelectedIndex(), 0);
        assertEquals(whois.miscList.getSelectedIndex(), 0);
    }

}
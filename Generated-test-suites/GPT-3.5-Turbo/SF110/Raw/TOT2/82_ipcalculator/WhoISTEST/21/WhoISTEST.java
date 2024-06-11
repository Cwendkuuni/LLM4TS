package ipac;

import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {
    
    // Test case for start query method
    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        // Testing when query field is empty
        whois.queryTextField.setText("");
        whois.startQuery(); // This should display error message
    }
    
    // Test case for reset method
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        // Testing reset method
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertEquals(true, whois.resetRadioButton.isSelected());
        assertEquals(false, whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
    
    // Test case for query method
    @Test
    public void testQuery() {
        // Testing with known Whois host
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "whois.apnic.net";
        whois.WHOIS_PORT = 43;
        whois.query("query");
        // Add assertions for expected output
    }
    
    // Test case for getQuery method
    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        // Test when inverse query is selected
        whois.inverseQueries.setSelectedIndex(1);
        whois.queryTextField.setText("query");
        assertEquals("-i admin-c query", whois.getQuery());
        // Add more test cases for different options
    }
}
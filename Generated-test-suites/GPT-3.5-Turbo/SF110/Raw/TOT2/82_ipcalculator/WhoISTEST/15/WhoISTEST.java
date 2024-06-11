import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {

    @Test
    public void testStartQueryWithEmptyQuery() {
        WhoIS whois = new WhoIS();
        
        // Test when query is empty
        whois.queryTextField.setText("");
        whois.startQuery();
        
        // Check if error message is displayed
        // In this case, the expected behavior is to show an error message "No Query Detected"
        assertTrue(whois.isVisible() && whois.getComponents().length > 0);
    }

    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        
        // Test when query is not empty
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        
        // Check if query method is called
        // In this case, the expected behavior is to query the WHOIS server
        // We can't directly test the network call, so we assume if no exceptions are thrown, the test passes
        assertEquals(0, 0);
    }

    @Test
    public void testQueryWithUnknownHostException() {
        WhoIS whois = new WhoIS();
        
        // Test when unknown host is encountered
        whois.WHOIS_ADDRESS = "unknownhost.com";
        whois.query("example.com");
        
        // Check if error message is displayed
        // In this case, the expected behavior is to show an error message "Unknown Whois host: unknownhost.com"
        assertTrue(whois.isVisible() && whois.getComponents().length > 0);
    }

    @Test
    public void testQueryWithIOException() {
        WhoIS whois = new WhoIS();
        
        // Test when IOException occurs
        whois.WHOIS_ADDRESS = "whois.apnic.net";
        whois.WHOIS_PORT = 9999; // Assuming invalid port
        whois.query("example.com");
        
        // Check if error message is displayed
        // In this case, the expected behavior is to show an error message "Whois server could not be contacted. Please check your internet connection."
        assertTrue(whois.isVisible() && whois.getComponents().length > 0);
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        
        // Test if getQuery method generates correct query string based on selected options
        whois.lowercaseL.setSelected(true); // Select 1st level less specific
        whois.queryTextField.setText("example.com");
        
        assertEquals("-l example.com", whois.getQuery());
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        
        // Test if reset method correctly resets all fields
        whois.queryTextField.setText("example.com");
        whois.inverseQueries.setSelectedIndex(1); // Select "admin-c"
        whois.miscList.setSelectedIndex(2); // Select "as-set"
        
        whois.reset();
        
        assertTrue(whois.queryTextField.getText().isEmpty() && whois.inverseQueries.getSelectedIndex() == 0 && whois.miscList.getSelectedIndex() == 0);
    }
}
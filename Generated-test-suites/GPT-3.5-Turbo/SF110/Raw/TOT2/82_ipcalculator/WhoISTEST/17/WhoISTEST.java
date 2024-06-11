import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {
    
    @Test
    public void testStartQueryWithEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("");
        whois.startQuery();
        // Assert that an error message is displayed for empty query
        // Error message should be "No Query Detected"
    }
    
    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.startQuery();
        // Add assertions for valid query
        // Assert functionalities related to starting a query
    }
    
    @Test
    public void testQueryWithUnknownHostException() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "unknownhost";
        whois.query("example");
        // Assert that an error message is displayed for unknown host
    }
    
    @Test
    public void testQueryWithIOException() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "invalidaddress";
        whois.query("example");
        // Assert that an error message is displayed for IOException
    }
    
    @Test
    public void testGetQueryWithInverseQueries() {
        WhoIS whois = new WhoIS();
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.queryTextField.setText("example");
        String query = whois.getQuery();
        // Assert that the query string includes the inverse query option
    }
    
    @Test
    public void testGetQueryWithMiscQueries() {
        WhoIS whois = new WhoIS();
        whois.miscList.setSelectedIndex(1); // Select "as-block"
        whois.queryTextField.setText("example");
        String query = whois.getQuery();
        // Assert that the query string includes the selected misc query option
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.inverseQueries.setSelectedIndex(1);
        whois.miscList.setSelectedIndex(2);
        whois.reset();
        // Assert that all fields are reset to default values
    }
}
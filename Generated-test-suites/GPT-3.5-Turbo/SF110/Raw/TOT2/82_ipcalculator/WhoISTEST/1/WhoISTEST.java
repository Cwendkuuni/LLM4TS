import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {
    
    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        // Assert the result of the query
        // add relevant assertions here
    }
    
    @Test
    public void testStartQueryWithEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("");
        whois.startQuery();
        // Assert that an error message is displayed
        // add relevant assertions here
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.reset();
        // Assert that all fields are reset to default values
        // add relevant assertions here
    }
    
    @Test
    public void testGetQueryWithInverseQuerySelected() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.inverseQueries.setSelectedIndex(1);
        String query = whois.getQuery();
        // Assert that the query includes the inverse query option
        // add relevant assertions here
    }
    
    @Test
    public void testQueryWithUnknownHostException() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "invalid.host";
        whois.query("example.com");
        // Assert that an error message is displayed for UnknownHostException
        // add relevant assertions here
    }
    
    @Test
    public void testQueryWithIOException() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "valid.host";
        // Simulate IOException by blocking connection
        whois.query("example.com");
        // Assert that an error message is displayed for IOException
        // add relevant assertions here
    }
}
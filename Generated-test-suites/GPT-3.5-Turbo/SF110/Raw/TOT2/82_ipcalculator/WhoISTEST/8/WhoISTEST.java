package ipac;
import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testStartQueryWithEmptyQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("");
        whois.startQuery();
        // Assert error message is displayed
    }

    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("query");
        whois.startQuery();
        // Assert query is executed successfully
    }

    @Test
    public void testStartQueryWithUnknownHostException() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "invalidhost";
        whois.queryTextField.setText("query");
        whois.startQuery();
        // Assert error message for UnknownHostException is displayed
    }

    @Test
    public void testStartQueryWithIOException() {
        WhoIS whois = new WhoIS();
        whois.WHOIS_ADDRESS = "validhost";
        whois.WHOIS_PORT = 123; // Assuming an invalid port
        whois.queryTextField.setText("query");
        whois.startQuery();
        // Assert error message for IOException is displayed
    }

}
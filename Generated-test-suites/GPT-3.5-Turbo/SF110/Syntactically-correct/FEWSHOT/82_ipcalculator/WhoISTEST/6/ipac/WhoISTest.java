import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        // Add assertions here
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.reset();
        // Add assertions here
    }

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        whois.query("example.com");
        // Add assertions here
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        String query = whois.getQuery();
        // Add assertions here
    }

}
import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        whois.query("example.com");
        // Test the output of WhoISOutput class
        // Assert statements to check the output
    }

    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        whois.startQuery();
        // Assert statements to check if query is started correctly
    }
    
    @Test
    public void testDisplayError() {
        WhoIS whois = new WhoIS();
        whois.displayError("Error");
        // Assert statements to check if error message is displayed correctly
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.reset();
        // Assert statements to check if reset method works correctly
    }

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        String query = whois.getQuery();
        // Assert statements to check if get query method works correctly
    }
}
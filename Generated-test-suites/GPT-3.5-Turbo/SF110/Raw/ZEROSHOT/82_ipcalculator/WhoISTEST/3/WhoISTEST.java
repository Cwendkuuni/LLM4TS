import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {
    
    @Test
    public void testConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
    }
    
    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        whois.query("example");
        // Assert the result of the query, if any
    }
    
    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        String query = whois.getQuery();
        // Assert the expected query string
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.reset();
        // Assert the state of the fields after reset
    }
    
    // Add more test methods for other class methods
    
    
}
import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {
    
    @Test
    public void testStartQueryNoQuery() {
        WhoIS whois = new WhoIS();
        // Testing startQuery method with no query
        whois.startQuery();
        // Assert error message is displayed
        // To be implemented
    }
    
    @Test
    public void testStartQueryWithQuery() {
        WhoIS whois = new WhoIS();
        // Testing startQuery method with a query
        // To be implemented
        // Assert query is executed
    }
    
    @Test
    public void testQueryUnknownHostException() {
        WhoIS whois = new WhoIS();
        // Testing query method with UnknownHostException
        // To be implemented
        // Assert error message is displayed
    }
    
    @Test
    public void testQueryIOException() {
        WhoIS whois = new WhoIS();
        // Testing query method with IOException
        // To be implemented
        // Assert error message is displayed
    }
    
    @Test
    public void testGetQueryWithInverseQueries() {
        WhoIS whois = new WhoIS();
        // Testing getQuery method with inverseQueries selected
        // To be implemented
        // Assert correct query string is returned
    }
    
    @Test
    public void testGetQueryWithMiscList() {
        WhoIS whois = new WhoIS();
        // Testing getQuery method with items selected in miscList
        // To be implemented
        // Assert correct query string is returned
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        // Testing reset method
        // To be implemented
        // Assert all fields are reset
    }
}
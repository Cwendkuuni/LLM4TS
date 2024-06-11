import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testQuery() {
        WhoIS whois = new WhoIS();
        String query = "exampleQuery";
        whois.query(query);
        // Assert expected behavior
    }
    
    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        String expectedResult = "exampleQuery";
        String query = whois.getQuery();
        assertEquals(expectedResult, query);
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.reset();
        // Assert expected behavior
    }
}
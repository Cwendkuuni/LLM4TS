package ipac;
import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {
    
    @Test
    public void testConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
        // Add more assertions to test the constructor
    }
    
    @Test
    public void testActionPerformed_QueryButton() {
        WhoIS whois = new WhoIS();
        // Simulate query button action performed
        // Verify the startQuery method is called
        // Add assertions to verify expected behavior
    }
    
    @Test
    public void testActionPerformed_ResetButton() {
        WhoIS whois = new WhoIS();
        // Simulate reset button action performed
        // Verify the reset method is called
        // Add assertions to verify expected behavior
    }
    
    @Test
    public void testStartQuery_EmptyQuery() {
        WhoIS whois = new WhoIS();
        // Set query text field to empty
        // Call startQuery method
        // Verify error is displayed for empty query
    }
    
    @Test
    public void testStartQuery_ValidQuery() {
        WhoIS whois = new WhoIS();
        // Set a valid query in query text field
        // Call startQuery method
        // Verify query method is called with the correct query
    }
    
    @Test
    public void testStartQuery_UnknownHostException() {
        WhoIS whois = new WhoIS();
        // Set invalid WHOIS address
        // Call startQuery method
        // Verify error is displayed for unknown host
    }
    
    @Test
    public void testStartQuery_IOException() {
        WhoIS whois = new WhoIS();
        // Simulate IOException while querying
        // Verify error is displayed for server connection issue
    }
    
    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        // Set various options and selections
        // Call getQuery method
        // Verify the generated query string is correct
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        // Set some values and selections
        // Call reset method
        // Verify all values are reset to default
    }
}
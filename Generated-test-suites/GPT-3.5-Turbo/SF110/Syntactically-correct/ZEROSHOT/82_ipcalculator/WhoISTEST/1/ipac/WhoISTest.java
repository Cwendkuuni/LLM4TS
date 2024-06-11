import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {
    private WhoIS whois;
    
    @Before
    public void setUp() {
        whois = new WhoIS();
    }
    
    @Test
    public void testStartQueryNoQueryDetected() {
        whois.queryTextField.setText("");
        whois.startQuery();
        // Error dialog should be displayed with "No Query Detected" message
        
        // Code to verify the error dialog is displayed and has the correct message
    }
    
    @Test
    public void testStartQueryValidQuery() {
        whois.queryTextField.setText("example.com");
        whois.startQuery();
        // Code to verify the query method is called with the correct query
        
        // Code to verify the WhoISOutput is created with the correct output
        
        // Verify additional conditions based on specific requirements
    }
    
    @Test
    public void testReset() {
        whois.queryTextField.setText("example.com");
        whois.reset();
        // Verify that all query-related components are reset to their default values
    }
    
    // Add more test methods to comprehensively test all methods in the WhoIS class
    
}
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class WhoISTest {
    
    private WhoIS whoISInstance;
    
    @Before
    public void setUp() {
        whoISInstance = new WhoIS();
    }
    
    @Test
    public void testStartQueryNoQueryDetected() {
        // Test case for startQuery when no query is detected
        // Should display error message
        whoISInstance.reset();
        whoISInstance.queryTextField.setText("");
        whoISInstance.startQuery();
        
        // Assert that error message is displayed
        // You need to assert based on how the error is displayed in the GUI
    }
    
    @Test
    public void testStartQueryWithValidQuery() {
        // Test case for startQuery with valid query
        // Should perform the query
        whoISInstance.reset();
        whoISInstance.queryTextField.setText("example");
        whoISInstance.startQuery();
        
        // Assert based on the expected output of the query
    }
    
    @Test
    public void testStartQueryUnknownHostException() {
        // Test case for startQuery with unknown host exception
        // Should display error message
        whoISInstance.reset();
        whoISInstance.queryTextField.setText("example");
        whoISInstance.WHOIS_ADDRESS = "invalidhost";
        whoISInstance.startQuery();
        
        // Assert that error message for UnknownHostException is displayed
    }
    
    // Add more test cases for edge cases and exception handling scenarios

}
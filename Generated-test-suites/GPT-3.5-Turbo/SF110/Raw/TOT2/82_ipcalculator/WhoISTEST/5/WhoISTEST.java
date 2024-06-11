import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {
    
    @Test
    public void testConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
        // Add more assertions to test the initialization of WhoIS object
    }
    
    @Test
    public void testQueryTextField() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("test");
        assertEquals("test", whois.queryTextField.getText());
        // Add more assertions to test the queryTextField functionality
    }
    
    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("test");
        whois.startQuery();
        // Add assertions to validate query execution
    }
    
    @Test
    public void testDisplayError() {
        WhoIS whois = new WhoIS();
        whois.displayError("Test Error");
        // Add assertions to validate error display
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("test");
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        // Add more assertions to test the reset functionality
    }
    
    // Add more test cases for other methods and edge cases
    
}
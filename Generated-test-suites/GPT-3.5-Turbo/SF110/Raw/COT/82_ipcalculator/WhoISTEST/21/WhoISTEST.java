
import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {
    
    @Test
    public void testConstructor() {
        WhoIS whois = new WhoIS();
        assertNotNull(whois);
    }
    
    @Test
    public void testActionPerformed_QueryButton() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.queryButton.doClick();
        // perform assertions to check if the query was sent and received properly
    }
    
    @Test
    public void testActionPerformed_ResetButton() {
        WhoIS whois = new WhoIS();
        whois.resetButton.doClick();
        assertEquals("", whois.queryTextField.getText());
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
    
    // Add more individual test cases for the other methods
    
    @Test
    public void testStartQuery_InvalidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("");
        whois.startQuery();
        // perform assertions to check if an error message is displayed
    }
    
    @Test
    public void testStartQuery_ValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.startQuery();
        // perform assertions to check if the query was sent and received properly
    }
}
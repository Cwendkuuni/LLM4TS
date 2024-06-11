import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example.com");
        assertEquals("example.com", whois.getQuery());
    }

    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
    
    // Test other methods in WhoIS class
    
}
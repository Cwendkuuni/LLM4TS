package ipac;

import static org.junit.Assert.*;
import org.junit.Test;

public class WhoISTest {
    
    @Test
    public void testStartQueryWithNoQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("");
        
        whois.startQuery();
        
        assertTrue(whois.displayErrorCalled);
    }
    
    @Test
    public void testStartQueryWithValidQuery() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        
        whois.startQuery();
        
        assertEquals("example", whois.queryCalledWith);
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        whois.queryTextField.setText("example");
        whois.resetRadioButton.setSelected(false);
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedIndex(1);
        whois.miscList.setSelectedIndex(1);
        
        whois.reset();
        
        assertEquals("", whois.queryTextField.getText());
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(-1, whois.miscList.getSelectedIndex());
    }
}
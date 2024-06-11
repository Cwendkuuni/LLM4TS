package ipac;

import org.junit.Test;
import org.junit.Assert;

public class WhoISTest {
    
    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        
        // Test startQuery with empty query field
        whois.queryTextField.setText("");
        whois.startQuery();
        // Verify error message is displayed
        assertNotNull("Error message displayed", whois.displayError);
        
        // Test startQuery with valid query
        whois.queryTextField.setText("query");
        whois.startQuery();
        // Verify query is executed and results are displayed
        assertNotNull("Query executed and results displayed", whois.query);
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        
        // Test reset method
        whois.queryTextField.setText("query");
        whois.reset();
        // Verify query field is cleared
        assertEquals("", whois.queryTextField.getText());
        // Verify radio buttons are reset
        assertTrue(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        // Verify combo box is reset
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        // Verify list is reset
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
    
    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        
        // Test getQuery method
        whois.queryTextField.setText("query");
        whois.inverseQueries.setSelectedIndex(1);
        whois.lowercaseL.setSelected(true);
        String expectedQuery = "-i admin-c query -l";
        assertEquals(expectedQuery, whois.getQuery());
    }
}
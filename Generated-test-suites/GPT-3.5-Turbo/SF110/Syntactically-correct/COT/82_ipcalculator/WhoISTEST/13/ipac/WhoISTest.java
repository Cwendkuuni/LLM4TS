import org.junit.*;
import static org.junit.Assert.*;

public class WhoISTest {
    
    @Test
    public void testQueryPanel() {
        WhoIS whois = new WhoIS();
        
        // Test creation of query panel
        JPanel queryPanel = whois.queryPanel();
        
        // Test if query panel is not null
        assertNotNull(queryPanel);
        
        // Test if query text field is created
        assertNotNull(whois.queryTextField);
        
        // Test if query button is created
        assertNotNull(whois.queryButton);
        
        // Test if reset button is created
        assertNotNull(whois.resetButton);
    }
    
    @Test
    public void testAdditionalQueriesPanel() {
        WhoIS whois = new WhoIS();
        
        // Test creation of additional queries panel
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        
        // Test if additional queries panel is not null
        assertNotNull(additionalQueriesPanel);
        
        // Test if IP address lookups panel is created
        assertNotNull(whois.IPAddressLookups());
        
        // Test if miscellaneous queries panel is created
        assertNotNull(whois.MiscellaneousQueries());
    }
    
    @Test
    public void testIPAddressLookups() {
        WhoIS whois = new WhoIS();
        
        // Test creation of IP address lookups panel
        JPanel ipAddressLookupsPanel = whois.IPAddressLookups();
        
        // Test if IP address lookups panel is not null
        assertNotNull(ipAddressLookupsPanel);
        
        // Test if all radio buttons are created
        assertNotNull(whois.lowercaseL);
        assertNotNull(whois.uppercaseL);
        assertNotNull(whois.lowercaseM);
        assertNotNull(whois.uppercaseM);
        assertNotNull(whois.lowercaseX);
        assertNotNull(whois.resetRadioButton);
        
        // Test if checkbox is created
        assertNotNull(whois.lowercaseD);
    }
    
    @Test
    public void testMiscellaneousQueries() {
        WhoIS whois = new WhoIS();
        
        // Test creation of miscellaneous queries panel
        JPanel miscellaneousQueriesPanel = whois.MiscellaneousQueries();
        
        // Test if miscellaneous queries panel is not null
        assertNotNull(miscellaneousQueriesPanel);
        
        // Test if inverse queries combo box is created
        assertNotNull(whois.inverseQueries);
        
        // Test if miscList list is created
        assertNotNull(whois.miscList);
    }
    
    @Test
    public void testQueryHintsPanel() {
        WhoIS whois = new WhoIS();
        
        // Test creation of query hints panel
        JPanel queryHintsPanel = whois.queryHintsPanel();
        
        // Test if query hints panel is not null
        assertNotNull(queryHintsPanel);
    }
    
    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        
        // Test start query with empty query
        whois.queryTextField.setText("");
        whois.startQuery();
        
        // Test start query with non-empty query
        whois.queryTextField.setText("example");
        whois.startQuery();
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        
        // Test reset method
        whois.reset();
        
        // Test if query text field is empty after reset
        assertTrue(whois.queryTextField.getText().isEmpty());
        
        // Test if reset radio button is selected after reset
        assertTrue(whois.resetRadioButton.isSelected());
        
        // Test if lowercaseD checkbox is not selected after reset
        assertFalse(whois.lowercaseD.isSelected());
        
        // Test if inverseQueries combo box is set to None after reset
        assertEquals("None", whois.inverseQueries.getSelectedItem());
        
        // Test if miscList list is set to first item after reset
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}
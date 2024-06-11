
import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {

    @Test
    public void testQueryPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryPanel = whois.queryPanel();
        
        assertNotNull(queryPanel);
        assertNotNull(queryPanel.getComponent(0));
        assertNotNull(queryPanel.getComponent(1));
        assertNotNull(queryPanel.getComponent(2));
        
        assertEquals("Search for : ", ((JLabel)queryPanel.getComponent(0)).getText());
        assertTrue(queryPanel.getComponent(1) instanceof JTextField);
        assertTrue(queryPanel.getComponent(2) instanceof JButton);
        assertTrue(queryPanel.getComponent(3) instanceof JButton);
    }
    
    @Test
    public void testAdditionalQueriesPanel() {
        WhoIS whois = new WhoIS();
        JPanel additionalQueriesPanel = whois.additionalQueriesPanel();
        
        assertNotNull(additionalQueriesPanel);
        assertNotNull(additionalQueriesPanel.getComponent(0));
        assertNotNull(additionalQueriesPanel.getComponent(1));
        
        assertTrue(additionalQueriesPanel.getComponent(0) instanceof JPanel);
        assertTrue(additionalQueriesPanel.getComponent(1) instanceof JPanel);
    }
    
    @Test
    public void testIPAddressLookups() {
        WhoIS whois = new WhoIS();
        JPanel lookupPanel = whois.IPAddressLookups();
        
        assertNotNull(lookupPanel);
        assertEquals(6, lookupPanel.getComponentCount());
        
        assertTrue(lookupPanel.getComponent(0) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(1) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(2) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(3) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(4) instanceof JRadioButton);
        assertTrue(lookupPanel.getComponent(5) instanceof JCheckBox);
    }
    
    @Test
    public void testMiscellaneousQueries() {
        WhoIS whois = new WhoIS();
        JPanel miscPanel = whois.MiscellaneousQueries();
        
        assertNotNull(miscPanel);
        assertNotNull(miscPanel.getComponent(0));
        assertNotNull(miscPanel.getComponent(1));
        
        assertTrue(miscPanel.getComponent(0) instanceof JLabel);
        assertTrue(miscPanel.getComponent(1) instanceof JComboBox);
        assertTrue(miscPanel.getComponent(2) instanceof JLabel);
        assertTrue(miscPanel.getComponent(3) instanceof JScrollPane);
    }
    
    @Test
    public void testQueryHintsPanel() {
        WhoIS whois = new WhoIS();
        JPanel queryHintsPanel = whois.queryHintsPanel();
        
        assertNotNull(queryHintsPanel);
        assertNotNull(queryHintsPanel.getComponent(0));
        
        assertTrue(queryHintsPanel.getComponent(0) instanceof JPanel);
    }
    
    @Test
    public void testStartQuery() {
        WhoIS whois = new WhoIS();
        
        whois.queryTextField.setText("example");
        whois.startQuery();
        
        // Verify that query method is called
        // We can't assert anything more specific as the query method has network dependencies
    }
    
    @Test
    public void testDisplayError() {
        WhoIS whois = new WhoIS();
        
        whois.displayError("Test Error");
        // Verify that error dialog is displayed
        // We can't assert anything more specific as the error dialog is system dependent
    }
    
    @Test
    public void testGetQuery() {
        WhoIS whois = new WhoIS();
        
        whois.queryTextField.setText("example");
        
        whois.uppercaseL.setSelected(true);
        assertEquals("-L example", whois.getQuery());
        
        whois.lowercaseM.setSelected(true);
        assertEquals("-m example", whois.getQuery());
        
        whois.lowercaseD.setSelected(true);
        assertEquals("-m example", whois.getQuery());
        
        whois.inverseQueries.setSelectedIndex(1);
        assertEquals("-i admin-c example", whois.getQuery());
        
        whois.miscList.setSelectedIndex(1);
        assertEquals("-T as-block,-i admin-c example", whois.getQuery());
    }
    
    @Test
    public void testReset() {
        WhoIS whois = new WhoIS();
        
        whois.queryTextField.setText("example");
        whois.uppercaseL.setSelected(true);
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedIndex(1);
        whois.miscList.setSelectedIndex(1);
        
        whois.reset();
        
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.uppercaseL.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}


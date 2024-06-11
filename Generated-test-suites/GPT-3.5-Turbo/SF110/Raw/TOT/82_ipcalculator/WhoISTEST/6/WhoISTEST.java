import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {
    // Test case for queryPanel method
    @Test
    public void testQueryPanel() {
        WhoIS whoIS = new WhoIS();
        JPanel queryPanel = whoIS.queryPanel();
        
        // Checking if the queryTextField is not null
        assertNotNull(queryPanel.queryTextField);
        
        // Checking if the queryTextField has the correct tool tip text
        assertEquals("Enter Whois database object you would like to query", queryPanel.queryTextField.getToolTipText());
        
        // Checking if the queryButton is not null
        assertNotNull(queryPanel.queryButton);
        
        // Checking if the resetButton is not null
        assertNotNull(queryPanel.resetButton);
    }
    
    // Test case for additionalQueriesPanel method
    @Test
    public void testAdditionalQueriesPanel() {
        WhoIS whoIS = new WhoIS();
        JPanel additionalQueriesPanel = whoIS.additionalQueriesPanel();
        
        // Checking if the additionalQueriesPanel is not null
        assertNotNull(additionalQueriesPanel);
        
        // Checking if the lowercaseL radio button is not null
        assertNotNull(additionalQueriesPanel.lowercaseL);
        
        // Checking if the uppercaseL radio button is not null
        assertNotNull(additionalQueriesPanel.uppercaseL);
        
        // Checking if the lowercaseM radio button is not null
        assertNotNull(additionalQueriesPanel.lowercaseM);
        
        // Checking if the uppercaseM radio button is not null
        assertNotNull(additionalQueriesPanel.uppercaseM);
        
        // Checking if the lowercaseX radio button is not null
        assertNotNull(additionalQueriesPanel.lowercaseX);
        
        // Checking if the lowercaseD check box is not null
        assertNotNull(additionalQueriesPanel.lowercaseD);
    }
    
    // Test case for IPAddressLookups method
    @Test
    public void testIPAddressLookups() {
        WhoIS whoIS = new WhoIS();
        JPanel lookupPanel = whoIS.IPAddressLookups();
        
        // Checking if the lookupPanel is not null
        assertNotNull(lookupPanel);
        
        // Checking if the radioButtonGroup is not null
        assertNotNull(lookupPanel.radioButtonGroup);
        
        // Checking if the lowercaseL radio button is not null
        assertNotNull(lookupPanel.lowercaseL);
        
        // Checking if the uppercaseL radio button is not null
        assertNotNull(lookupPanel.uppercaseL);
        
        // Checking if the lowercaseM radio button is not null
        assertNotNull(lookupPanel.lowercaseM);
        
        // Checking if the uppercaseM radio button is not null
        assertNotNull(lookupPanel.uppercaseM);
        
        // Checking if the lowercaseX radio button is not null
        assertNotNull(lookupPanel.lowercaseX);
        
        // Checking if the lowercaseD check box is not null
        assertNotNull(lookupPanel.lowercaseD);
    }
    
    // Test case for MiscellaneousQueries method
    @Test
    public void testMiscellaneousQueries() {
        WhoIS whoIS = new WhoIS();
        JPanel miscPanel = whoIS.MiscellaneousQueries();
        
        // Checking if the miscPanel is not null
        assertNotNull(miscPanel);
        
        // Checking if the inverseQueries combo box is not null
        assertNotNull(miscPanel.inverseQueries);
        
        // Checking if the miscList is not null
        assertNotNull(miscPanel.miscList);
    }
    
    // Test case for queryHintsPanel method
    @Test
    public void testQueryHintsPanel() {
        WhoIS whoIS = new WhoIS();
        JPanel queryHintsPanel = whoIS.queryHintsPanel();
        
        // Checking if the queryHintsPanel is not null
        assertNotNull(queryHintsPanel);
    }
    
    // Test case for actionPerformed method with queryButton source
    @Test
    public void testActionPerformed_queryButton() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Hello");
        whoIS.startQuery();
        
        // Checking if the queryTextField is empty after startQuery method
        assertEquals("", whoIS.queryTextField.getText());
    }
    
    // Test case for actionPerformed method with resetButton source
    @Test
    public void testActionPerformed_resetButton() {
        WhoIS whoIS = new WhoIS();
        whoIS.queryTextField.setText("Hello");
        whoIS.reset();
        
        // Checking if the queryTextField is empty after reset method
        assertEquals("", whoIS.queryTextField.getText());
        
        // Checking if the resetRadioButton is selected after reset method
        assertTrue(whoIS.resetRadioButton.isSelected());
        
        // Checking if the lowercaseD check box is not selected after reset method
        assertFalse(whoIS.lowercaseD.isSelected());
        
        // Checking if the inverseQueries combo box is set to index 0 after reset method
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        
        // Checking if the miscList is set to index 0 after reset method
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }
}
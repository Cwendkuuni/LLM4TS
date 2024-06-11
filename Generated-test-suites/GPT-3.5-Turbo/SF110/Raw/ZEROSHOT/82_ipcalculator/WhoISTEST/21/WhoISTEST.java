import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {
    
    private WhoIS whois;
    
    @Before
    public void setup() {
        whois = new WhoIS();
    }
    
    @Test
    public void testQueryPanel() {
        assertEquals("Search for : ", whois.queryPanel().getComponent(0));
        assertEquals(JTextField.class, whois.queryPanel().getComponent(1).getClass());
        assertEquals("Query", whois.queryPanel().getComponent(2));
        assertEquals("Reset", whois.queryPanel().getComponent(3));
    }
    
    @Test
    public void testAdditionalQueriesPanel() {
        assertEquals(JPanel.class, whois.additionalQueriesPanel().getClass());
        assertEquals(FlowLayout.class, whois.additionalQueriesPanel().getLayout().getClass());
        assertEquals(2, whois.additionalQueriesPanel().getComponentCount());
    }
    
    @Test
    public void testIPAddressLookups() {
        assertEquals(JPanel.class, whois.IPAddressLookups().getClass());
        assertEquals(BoxLayout.class, whois.IPAddressLookups().getLayout().getClass());
        assertEquals(6, whois.IPAddressLookups().getComponentCount());
    }
    
    @Test
    public void testMiscellaneousQueries() {
        assertEquals(JPanel.class, whois.MiscellaneousQueries().getClass());
        assertEquals(BoxLayout.class, whois.MiscellaneousQueries().getLayout().getClass());
        assertEquals(2, whois.MiscellaneousQueries().getComponentCount());
    }
    
    @Test
    public void testQueryHintsPanel() {
        assertEquals(JPanel.class, whois.queryHintsPanel().getClass());
        assertEquals(FlowLayout.class, whois.queryHintsPanel().getLayout().getClass());
        assertEquals(1, whois.queryHintsPanel().getComponentCount());
    }
    
    @Test
    public void testCreatePopupMenu() {
        whois.createPopupMenu();
        assertNotNull(whois.popupMenu);
        assertEquals(4, whois.popupMenu.getComponentCount());
    }
    
    @Test
    public void testActionPerformed_QueryButton() {
        whois.actionPerformed(new ActionEvent(whois.queryButton, 1, null));
        // assertion for startQuery() called
    }
    
    @Test
    public void testActionPerformed_ResetButton() {
        whois.actionPerformed(new ActionEvent(whois.resetButton, 1, null));
        // assertion for reset() called
    }
    
    @Test
    public void testStartQuery_NoQuery() {
        whois.queryTextField.setText("");
        whois.startQuery();
        // assertion for displayError() called with "No Query Detected"
    }
    
    @Test
    public void testStartQuery_WithQuery() {
        whois.queryTextField.setText("example");
        whois.startQuery();
        // assertion for query() called with "example"
    }
    
    @Test
    public void testQuery() {
        // Unable to test the query method as it requires a live internet connection and a valid Whois server
    }
    
    @Test
    public void testGetQuery_NoInverseQuery() {
        whois.queryTextField.setText("example");
        whois.lowercaseL.setSelected(true);
        assertEquals("-l example", whois.getQuery());
    }
    
    @Test
    public void testGetQuery_WithInverseQuery() {
        whois.queryTextField.setText("example");
        whois.inverseQueries.setSelectedItem("admin-c");
        assertEquals("-i admin-c example", whois.getQuery());
    }
    
    @Test
    public void testReset() {
        whois.queryTextField.setText("example");
        whois.lowercaseD.setSelected(true);
        whois.inverseQueries.setSelectedItem("admin-c");
        whois.miscList.setSelectedIndex(1);
        whois.reset();
        assertEquals("", whois.queryTextField.getText());
        assertFalse(whois.resetRadioButton.isSelected());
        assertFalse(whois.lowercaseD.isSelected());
        assertEquals(0, whois.inverseQueries.getSelectedIndex());
        assertEquals(0, whois.miscList.getSelectedIndex());
    }
}
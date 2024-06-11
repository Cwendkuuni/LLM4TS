
import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {
    
    @Test
    public void testConstructor() {
        WhoIS whoIS = new WhoIS();
        assertNotNull(whoIS);
    }
    
    @Test
    public void testActionPerformed_QueryButton() {
        WhoIS whoIS = new WhoIS();
        // Test the actionPerformed method when the queryButton is clicked
        // Simulate the button click
        whoIS.queryButton.doClick();
        // Verify the expected behavior
        assertEquals("", whoIS.getQuery());
    }
    
    @Test
    public void testActionPerformed_ResetButton() {
        WhoIS whoIS = new WhoIS();
        // Set some values for testing
        whoIS.queryTextField.setText("test query");
        whoIS.lowercaseD.setSelected(true);
        whoIS.inverseQueries.setSelectedIndex(1);
        whoIS.miscList.setSelectedIndex(2);
        
        // Test the actionPerformed method when the resetButton is clicked
        // Simulate the button click
        whoIS.resetButton.doClick();
        // Verify the expected behavior
        assertEquals("", whoIS.queryTextField.getText());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }
    
    @Test
    public void testStartQuery_EmptyQuery() {
        WhoIS whoIS = new WhoIS();
        // Test the startQuery method with an empty query
        whoIS.startQuery();
        // Verify the expected behavior
        // A pop-up error message should be displayed
    }
  
    @Test
    public void testGetQuery() {
        WhoIS whoIS = new WhoIS();
        // Set values for testing
        whoIS.queryTextField.setText("test query");
        whoIS.lowercaseL.setSelected(true);
        whoIS.inverseQueries.setSelectedIndex(1);
        whoIS.miscList.setSelectedIndex(2);
        // Test the getQuery method
        String query = whoIS.getQuery();
        // Verify the expected query is returned
        assertEquals("-i admin-c test query", query);
    }
    
    @Test
    public void testReset() {
        WhoIS whoIS = new WhoIS();
        // Set some values for testing
        whoIS.queryTextField.setText("test query");
        whoIS.lowercaseD.setSelected(true);
        whoIS.inverseQueries.setSelectedIndex(1);
        whoIS.miscList.setSelectedIndex(2);
        // Test the reset method
        whoIS.reset();
        // Verify the expected behavior
        assertEquals("", whoIS.queryTextField.getText());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }
}

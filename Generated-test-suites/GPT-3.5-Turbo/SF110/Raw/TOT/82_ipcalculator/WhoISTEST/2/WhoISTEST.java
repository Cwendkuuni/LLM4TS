import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {
    
    private WhoIS whoIS;
    
    @Before
    public void setUp() {
        whoIS = new WhoIS();
    }
    
    @Test
    public void testQueryPanel() {
        // Typical use case
        // Set query text field to "example"
        // Expected: Query text field should have value "example"
        whoIS.queryTextField.setText("example");
        assertEquals("example", whoIS.queryTextField.getText());
        
        // Edge case
        // Set query text field to empty string
        // Expected: Query text field should have value ""
        whoIS.queryTextField.setText("");
        assertEquals("", whoIS.queryTextField.getText());
        
        // Error scenario
        // Set query text field to null
        // Expected: Query text field should have value ""
        whoIS.queryTextField.setText(null);
        assertEquals("", whoIS.queryTextField.getText());
    }
    
    @Test
    public void testAdditionalQueriesPanel() {
        // Typical use case
        // Select "1st level less specific" radio button
        // Expected: "1st level less specific" radio button should be selected
        whoIS.lowercaseL.setSelected(true);
        assertTrue(whoIS.lowercaseL.isSelected());
        
        // Edge case
        // Select "All more specific" radio button
        // Expected: "All more specific" radio button should be selected
        whoIS.uppercaseM.setSelected(true);
        assertTrue(whoIS.uppercaseM.isSelected());
        
        // Error scenario
        // Select "Reset Radio Button" radio button
        // Expected: "Reset Radio Button" radio button should be selected
        whoIS.resetRadioButton.setSelected(true);
        assertTrue(whoIS.resetRadioButton.isSelected());
    }
    
    @Test
    public void testMiscellaneousQueries() {
        // Typical use case
        // Select "admin-c" in inverse lookup scroll list
        // Expected: "admin-c" should be the selected item in inverse lookup scroll list
        whoIS.inverseQueries.setSelectedItem("admin-c");
        assertEquals("admin-c", whoIS.inverseQueries.getSelectedItem());
        
        // Edge case
        // Select "All" in inverse lookup scroll list
        // Expected: "All" should be the selected item in inverse lookup scroll list
        whoIS.inverseQueries.setSelectedItem("All");
        assertEquals("All", whoIS.inverseQueries.getSelectedItem());
        
        // Error scenario
        // Select a non-existing item in inverse lookup scroll list
        // Expected: The selected item in inverse lookup scroll list should remain unchanged
        whoIS.inverseQueries.setSelectedItem("invalid");
        assertEquals("All", whoIS.inverseQueries.getSelectedItem());
    }
    
    @Test
    public void testStartQuery() {
        // Typical use case
        // Set query text field to "example"
        // Expected: startQuery() method should be executed
        whoIS.queryTextField.setText("example");
        whoIS.startQuery();
        
        // Edge case
        // Set query text field to empty string
        // Expected: startQuery() method should be executed
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        
        // Error scenario
        // Set query text field to null
        // Expected: startQuery() method should be executed
        whoIS.queryTextField.setText(null);
        whoIS.startQuery();
    }
    
    @Test
    public void testReset() {
        // Typical use case
        // Call reset() method
        // Expected: Query text field should be empty, "Reset Radio Button" radio button should be selected, inverse lookup scroll list should have "None" selected, and misc list should have "All" selected
        whoIS.reset();
        assertEquals("", whoIS.queryTextField.getText());
        assertTrue(whoIS.resetRadioButton.isSelected());
        assertEquals("None", whoIS.inverseQueries.getSelectedItem());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
        
        // Edge case
        // Set query text field to "example"
        // Call reset() method
        // Expected: Query text field should be empty, "Reset Radio Button" radio button should be selected, inverse lookup scroll list should have "None" selected, and misc list should have "All" selected
        whoIS.queryTextField.setText("example");
        whoIS.reset();
        assertEquals("", whoIS.queryTextField.getText());
        assertTrue(whoIS.resetRadioButton.isSelected());
        assertEquals("None", whoIS.inverseQueries.getSelectedItem());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
        
        // Error scenario
        // Set query text field to "example"
        // Select "1st level less specific" radio button
        // Select "admin-c" in inverse lookup scroll list
        // Select "as-block" in misc list
        // Call reset() method
        // Expected: Query text field should be empty, "Reset Radio Button" radio button should be selected, inverse lookup scroll list should have "None" selected, and misc list should have "All" selected
        whoIS.queryTextField.setText("example");
        whoIS.lowercaseL.setSelected(true);
        whoIS.inverseQueries.setSelectedItem("admin-c");
        whoIS.miscList.setSelectedIndex(1);
        whoIS.reset();
        assertEquals("", whoIS.queryTextField.getText());
        assertTrue(whoIS.resetRadioButton.isSelected());
        assertEquals("None", whoIS.inverseQueries.getSelectedItem());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }
}
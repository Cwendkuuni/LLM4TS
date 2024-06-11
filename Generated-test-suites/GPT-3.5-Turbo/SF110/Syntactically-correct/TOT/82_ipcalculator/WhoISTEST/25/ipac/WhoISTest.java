import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {
    
    private WhoIS whoIS;
    
    @Before
    public void setup() {
        whoIS = new WhoIS();
    }
    
    @Test
    public void testQueryButton() {
        // Typical use case: Query button is clicked and query is not empty
        whoIS.queryTextField.setText("example.com");
        whoIS.queryButton.doClick();
        // Assertion: No error message is displayed
        assertNull(whoIS.getRootPane().getErrorMessage());
        
        // Error scenario: Query button is clicked and query is empty
        whoIS.queryTextField.setText("");
        whoIS.queryButton.doClick();
        // Assertion: Error dialog is displayed with message "No Query Detected"
        assertEquals("No Query Detected", whoIS.getRootPane().getErrorMessage());
    }
    
    @Test
    public void testResetButton() {
        // Typical use case: Reset button is clicked
        whoIS.queryTextField.setText("example.com");
        whoIS.resetButton.doClick();
        // Assertion: Query text field is cleared
        assertEquals("", whoIS.queryTextField.getText());
        // Assertion: Reset radio button is selected
        assertTrue(whoIS.resetRadioButton.isSelected());
        // Assertion: Lowercase D checkbox is not selected
        assertFalse(whoIS.lowercaseD.isSelected());
        // Assertion: Inverse queries combobox is set to "None"
        assertEquals("None", whoIS.inverseQueries.getSelectedItem());
        // Assertion: Miscellaneous queries list is set to "All"
        assertEquals("All", whoIS.miscList.getSelectedValue());
    }
    
    @Test
    public void testStartQuery() {
        // Typical use case: Query is started with a non-empty query
        whoIS.queryTextField.setText("example.com");
        whoIS.startQuery();
        // Assertion: No error message is displayed
        assertNull(whoIS.getRootPane().getErrorMessage());
        
        // Error scenario: Query is started with an empty query
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        // Assertion: Error dialog is displayed with message "No Query Detected"
        assertEquals("No Query Detected", whoIS.getRootPane().getErrorMessage());
    }
    
    @Test
    public void testQuery() {
        // Unable to test method query() directly as it uses network connection
        
        // No error scenarios identified
    }
    
    @Test
    public void testGetQuery() {
        // Typical use case: Various options are selected
        whoIS.queryTextField.setText("example.com");
        whoIS.lowercaseL.setSelected(true);
        whoIS.inverseQueries.setSelectedItem("admin-c");
        whoIS.miscList.setSelectedValues(new String[]{"as-set", "domain"}, true);
        String expectedQuery = "-l -i admin-c -T as-set,domain example.com";
        String actualQuery = whoIS.getQuery();
        // Assertion: Query string is correctly generated
        assertEquals(expectedQuery, actualQuery);
        
        // Edge case: No options selected
        whoIS.queryTextField.setText("example.com");
        whoIS.lowercaseX.setSelected(true);
        whoIS.booleanD = true;
        whoIS.miscList.setSelectedValue("All", true);
        expectedQuery = "-x -d example.com";
        actualQuery = whoIS.getQuery();
        // Assertion: Query string is correctly generated
        assertEquals(expectedQuery, actualQuery);
        
        // Edge case: All options selected
        whoIS.queryTextField.setText("example.com");
        whoIS.lowercaseL.setSelected(true);
        whoIS.uppercaseL.setSelected(true);
        whoIS.lowercaseM.setSelected(true);
        whoIS.uppercaseM.setSelected(true);
        whoIS.lowercaseX.setSelected(true);
        whoIS.booleanD = true;
        whoIS.miscList.setSelectedValues(new String[]{"as-set", "domain"}, true);
        expectedQuery = "-x -d -T as-set,domain example.com";
        actualQuery = whoIS.getQuery();
        // Assertion: Query string is correctly generated
        assertEquals(expectedQuery, actualQuery);
    }
    
    @Test
    public void testReset() {
        // Typical use case: Reset method is called
        whoIS.queryTextField.setText("example.com");
        whoIS.lowercaseD.setSelected(true);
        whoIS.inverseQueries.setSelectedItem("admin-c");
        whoIS.miscList.setSelectedValue("as-set", true);
        whoIS.reset();
        // Assertion: Query text field is cleared
        assertEquals("", whoIS.queryTextField.getText());
        // Assertion: Reset radio button is selected
        assertTrue(whoIS.resetRadioButton.isSelected());
        // Assertion: Lowercase D checkbox is not selected
        assertFalse(whoIS.lowercaseD.isSelected());
        // Assertion: Inverse queries combobox is set to "None"
        assertEquals("None", whoIS.inverseQueries.getSelectedItem());
        // Assertion: Miscellaneous queries list is set to "All"
        assertEquals("All", whoIS.miscList.getSelectedValue());
    }
}
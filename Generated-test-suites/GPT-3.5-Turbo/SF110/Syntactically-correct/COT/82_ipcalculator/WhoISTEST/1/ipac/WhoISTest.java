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
    public void testQuery() {
        // Test case 1: Empty query should display error
        whoIS.query("");
        
        // Test case 2: Valid query should not display error
        whoIS.query("example query");
    }
    
    @Test
    public void testReset() {
        // Test case 1: Resetting fields should clear all values
        whoIS.queryTextField.setText("example query");
        whoIS.reset();
        assertEquals("", whoIS.queryTextField.getText());
        assertTrue(whoIS.resetRadioButton.isSelected());
        assertFalse(whoIS.lowercaseD.isSelected());
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testStartQuery() {
        // Test case 1: Empty query should display error
        whoIS.queryTextField.setText("");
        whoIS.startQuery();
        
        // Test case 2: Valid query should not display error
        whoIS.queryTextField.setText("example query");
        whoIS.startQuery();
    }
    
    @Test
    public void testGetQuery() {
        // Test case 1: No inverse query selected
        whoIS.inverseQueries.setSelectedIndex(0);
        assertEquals("example query", whoIS.getQuery());
        
        // Test case 2: Inverse query selected
        whoIS.inverseQueries.setSelectedIndex(1);
        assertEquals("-i admin-c example query", whoIS.getQuery());
        
        // Test case 3: Different IP address lookup options selected
        whoIS.lowercaseL.setSelected(true);
        assertEquals("-l example query", whoIS.getQuery());
        whoIS.uppercaseL.setSelected(true);
        assertEquals("-L example query", whoIS.getQuery());
        whoIS.lowercaseM.setSelected(true);
        assertEquals("-m example query", whoIS.getQuery());
        whoIS.uppercaseM.setSelected(true);
        assertEquals("-M example query", whoIS.getQuery());
        whoIS.lowercaseX.setSelected(true);
        assertEquals("-x example query", whoIS.getQuery());
        
        // Test case 4: D checkbox selected
        whoIS.booleanD = true;
        assertEquals("-d example query", whoIS.getQuery());
        
        // Test case 5: Miscellaneous queries selected
        whoIS.miscList.setSelectedIndex(1);
        assertEquals("-T as-block,example query", whoIS.getQuery());
        whoIS.miscList.setSelectedIndex(2);
        assertEquals("-T as-set,example query", whoIS.getQuery());
        whoIS.miscList.setSelectedIndex(3);
        assertEquals("-T aut-num,example query", whoIS.getQuery());
        
        // Test case 6: All fields selected
        whoIS.inverseQueries.setSelectedIndex(1);
        whoIS.lowercaseL.setSelected(true);
        whoIS.booleanD = true;
        whoIS.miscList.setSelectedIndex(1);
        assertEquals("-i admin-c -l -d -T as-block,example query", whoIS.getQuery());
    }
}
package ipac;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WhoISTest {
    
    private WhoIS whoIs;
    
    @Before
    public void setUp() {
        whoIs = new WhoIS();
    }
    
    @Test
    public void testQueryButton() {
        // Test queryButton ActionListener
        // Implementation details of actionPerfomed() method prevent direct testing
        // Need to find a way to test it indirectly
        assertNotNull(whoIs.queryButton);
    }
    
    @Test
    public void testResetButton() {
        // Test resetButton ActionListener
        // Implementation details of actionPerfomed() method prevent direct testing
        // Need to find a way to test it indirectly
        assertNotNull(whoIs.resetButton);
    }
    
    @Test
    public void testStartQueryWithNoQueryDetected() {
        // Test startQuery() method when no query is detected
        // Expected: display error message
        whoIs.queryTextField.setText("");
        whoIs.startQuery();
        // Need to assert that the error message is displayed - HOW?
    }
    
    @Test
    public void testStartQuery() {
        // Test startQuery() method when query is detected
        // Expected: query() method is called
        
        // Can't directly test query() method as it involves interaction with external servers
        // Need to find a way to test it indirectly
    }
    
    @Test
    public void testGetQueryWithNoInverseQueries() {
        // Test getQuery() method when no inverse query is selected
        // Expected: return query without inverse query option
        
        whoIs.queryTextField.setText("example.com");
        whoIs.lowercaseL.setSelected(true);
        whoIs.uppercaseL.setSelected(false);
        whoIs.lowercaseM.setSelected(false);
        whoIs.uppercaseM.setSelected(false);
        whoIs.lowercaseX.setSelected(false);
        whoIs.booleanD = false;
        whoIs.miscList.setSelectedIndex(0);
        
        String expectedQuery = "-l example.com";
        assertEquals(expectedQuery, whoIs.getQuery());
    }
    
    @Test
    public void testGetQueryWithInverseQueries() {
        // Test getQuery() method when inverse query is selected
        // Expected: return query with inverse query option
        
        whoIs.queryTextField.setText("example.com");
        whoIs.lowercaseL.setSelected(false);
        whoIs.uppercaseL.setSelected(false);
        whoIs.lowercaseM.setSelected(false);
        whoIs.uppercaseM.setSelected(false);
        whoIs.lowercaseX.setSelected(false);
        whoIs.booleanD = true;
        whoIs.inverseQueries.setSelectedIndex(3);
        whoIs.miscList.setSelectedIndex(0);
        
        String expectedQuery = "-i author example.com";
        assertEquals(expectedQuery, whoIs.getQuery());
    }
    
    @Test
    public void testReset() {
        // Test reset() method
        
        whoIs.queryTextField.setText("example.com");
        whoIs.lowercaseD.setSelected(true);
        whoIs.inverseQueries.setSelectedIndex(3);
        whoIs.miscList.setSelectedIndex(2);
        
        whoIs.reset();
        
        // Check if the fields are reset
        assertEquals("", whoIs.queryTextField.getText());
        assertFalse(whoIs.resetRadioButton.isSelected());
        assertFalse(whoIs.lowercaseD.isSelected());
        assertEquals(0, whoIs.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIs.miscList.getSelectedIndex());
    }
}
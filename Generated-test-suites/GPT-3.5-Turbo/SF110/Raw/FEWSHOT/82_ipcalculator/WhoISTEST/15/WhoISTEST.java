package ipac;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

public class WhoISTest {
    private WhoIS whoIs;

    @Before
    public void setUp() {
        whoIs = new WhoIS();
    }
    
    @Test
    public void testStartQuery() {
        whoIs.queryTextField.setText("");
        whoIs.startQuery();
        assertEquals("No Query Detected", whoIs.queryTextField);
        
        whoIs.queryTextField.setText("query");
        whoIs.startQuery();
        // Assertion for querying functionality
    }
    
    @Test
    public void testDisplayError() {
        String error = "Error Message";
        whoIs.displayError(error);
        // Assertion for displaying error message
    }
    
    @Test
    public void testReset() {
        whoIs.queryTextField.setText("query");
        whoIs.reset();
        assertEquals("", whoIs.queryTextField);

        assertTrue(whoIs.resetRadioButton.isSelected());
        assertFalse(whoIs.lowercaseD.isSelected());
        assertEquals(0, whoIs.inverseQueries.getSelectedIndex());
        assertEquals(0, whoIs.miscList.getSelectedIndex());
    }
    
    @Test
    public void testGetQuery() {
        whoIs.queryTextField.setText("query");
        whoIs.inverseQueries.setSelectedIndex(1);
        whoIs.lowercaseL.setSelected(true);
        assertEquals("-i admin-c query", whoIs.getQuery());
        
        whoIs.queryTextField.setText("query");
        whoIs.uppercaseL.setSelected(true);
        assertEquals("-L query", whoIs.getQuery());
        
        whoIs.queryTextField.setText("query");
        whoIs.lowercaseM.setSelected(true);
        assertEquals("-m query", whoIs.getQuery());
        
        whoIs.queryTextField.setText("query");
        whoIs.uppercaseM.setSelected(true);
        assertEquals("-M query", whoIs.getQuery());
        
        whoIs.queryTextField.setText("query");
        whoIs.lowercaseX.setSelected(true);
        assertEquals("-x query", whoIs.getQuery());
        
        whoIs.queryTextField.setText("query");
        whoIs.lowercaseD.setSelected(true);
        assertEquals("-d query", whoIs.getQuery());
        
        whoIs.queryTextField.setText("query");
        whoIs.miscList.setSelectedIndex(1);
        assertEquals("-T as-block query", whoIs.getQuery());
    }
}
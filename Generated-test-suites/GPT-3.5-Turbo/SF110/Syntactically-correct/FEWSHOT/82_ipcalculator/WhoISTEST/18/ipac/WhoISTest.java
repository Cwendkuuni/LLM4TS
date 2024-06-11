import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class WhoISTest {
    private WhoIS whoIS;
    
    @Before
    public void setUp() {
        whoIS = new WhoIS();
    }
    
    @Test
    public void testStartQuery_emptyQuery_displayError() {
        // Arrange
        whoIS.queryTextField.setText("");
        
        // Act
        whoIS.startQuery();
        
        // Assert
        // Error message should be displayed
    }
    
    @Test
    public void testStartQuery_validQuery_queryCalled() {
        // Arrange
        whoIS.queryTextField.setText("example");
        
        // Act
        whoIS.startQuery();
        
        // Assert
        // verify that query method is called with "example" as the argument
    }
    
    @Test
    public void testReset_resetButton_selectedRadioButtonSetToTrue() {
        // Arrange
        whoIS.lowercaseL.setSelected(true);
        
        // Act
        whoIS.reset();
        
        // Assert
        assertFalse(whoIS.lowercaseL.isSelected());
        assertTrue(whoIS.resetRadioButton.isSelected());
    }
    
    @Test
    public void testReset_resetButton_inverseQueriesSetToDefault() {
        // Arrange
        whoIS.inverseQueries.setSelectedIndex(2);
        
        // Act
        whoIS.reset();
        
        // Assert
        assertEquals(0, whoIS.inverseQueries.getSelectedIndex());
    }
    
    @Test
    public void testReset_resetButton_miscListSetToDefault() {
        // Arrange
        whoIS.miscList.setSelectedIndex(4);
        
        // Act
        whoIS.reset();
        
        // Assert
        assertEquals(0, whoIS.miscList.getSelectedIndex());
    }
}
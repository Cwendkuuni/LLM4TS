import static org.junit.Assert.*;
import org.junit.*;

public class VariableTest {

    @BeforeClass
    public static void setUpClass() {
        // Initialize variableList before each test case
        Variable.variableList.clear();
    }
    
    @Test
    public void testAddVariable_ValidVariableName_AddsVariableToList() {
        // Arrange
        String varName = "x";
        
        // Act
        boolean result = Variable.addVariable(varName);
        
        // Assert
        assertTrue(result);
        assertEquals(1, Variable.variableList.size());
        assertEquals(varName, Variable.variableList.get(0).getName());
        assertEquals(0, Variable.variableList.get(0).getValue());
    }
    
    @Test
    public void testAddVariable_DuplicateVariableName_ReturnsFalse() {
        // Arrange
        String varName = "x";
        Variable.addVariable(varName); // Add variable with the same name
        
        // Act
        boolean result = Variable.addVariable(varName);
        
        // Assert
        assertFalse(result);
        assertEquals(1, Variable.variableList.size()); // List size remains the same
    }
    
    @Test
    public void testRemoveVariable_ExistingVariableName_RemovesVariableFromList() {
        // Arrange
        String varName = "x";
        Variable.addVariable(varName);
        
        // Act
        Variable.removeVariable(varName);
        
        // Assert
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testRemoveVariable_NonexistentVariableName_DoesNotRemoveAnything() {
        // Arrange
        String varName = "x";
        
        // Act
        Variable.removeVariable(varName);
        
        // Assert
        assertEquals(0, Variable.variableList.size()); // List size remains the same
    }
    
    @Test
    public void testGetVariableValue_ExistingVariableName_ReturnsVariableValue() {
        // Arrange
        String varName = "x";
        Variable.addVariable(varName);
        Variable.setVariableValue(varName, 5);
        
        // Act
        int result = Variable.getVariableValue(varName);
        
        // Assert
        assertEquals(5, result);
    }
    
    @Test
    public void testGetVariableValue_NonexistentVariableName_ReturnsZero() {
        // Arrange
        String varName = "x";
        
        // Act
        int result = Variable.getVariableValue(varName);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testGetVariableValue_ReferenceVariableName_ReturnsReferencedVariableValue() {
        // Arrange
        String varName = "x";
        Variable.addVariable(varName);
        Variable.setVariableValue(varName, 5);
        
        // Act
        int result = Variable.getVariableValue("*" + varName);
        
        // Assert
        assertEquals(5, result);
    }
    
    @Test
    public void testSetVariableValue_ExistingVariableName_SetsVariableValue() {
        // Arrange
        String varName = "x";
        Variable.addVariable(varName);
        
        // Act
        Variable.setVariableValue(varName, 5);
        
        // Assert
        assertEquals(5, Variable.getVariableValue(varName));
    }
    
    @Test
    public void testSetVariableValue_NonexistentVariableName_DoesNotSetAnything() {
        // Arrange
        String varName = "x";
        
        // Act
        Variable.setVariableValue(varName, 5);
        
        // Assert
        assertEquals(0, Variable.getVariableValue(varName)); // Value remains unchanged
    }
    
    @Test
    public void testFindVariable_ExistingVariableName_ReturnsVariableIndex() {
        // Arrange
        String varName = "x";
        Variable.addVariable(varName);
        
        // Act
        int result = Variable.findVariable(varName);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testFindVariable_NonexistentVariableName_ReturnsNegativeOne() {
        // Arrange
        String varName = "x";
        
        // Act
        int result = Variable.findVariable(varName);
        
        // Assert
        assertEquals(-1, result);
    }
    
    @Test
    public void testFindVariable_ReferenceVariableName_ReturnsVariableValue() {
        // Arrange
        String varName = "x";
        Variable.addVariable(varName);
        Variable.setVariableValue(varName, 5);
        
        // Act
        int result = Variable.findVariable("&" + varName);
        
        // Assert
        assertEquals(5, result);
    }
}
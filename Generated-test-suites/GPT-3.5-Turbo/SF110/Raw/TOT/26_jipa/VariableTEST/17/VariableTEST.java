import org.junit.Test;
import java.util.*;

public class VariableTest {
    
    @Test
    public void testAddVariable_typicalCase() {
        // Arrange
        String varName = "var1";
        
        // Act
        boolean result = Variable.addVariable(varName);
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testAddVariable_duplicateVariable() {
        // Arrange
        String varName = "var1";
        Variable.addVariable(varName);
        
        // Act
        boolean result = Variable.addVariable(varName);
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testRemoveVariable_existingVariable() {
        // Arrange
        String varName = "var1";
        Variable.addVariable(varName);
        
        // Act
        Variable.removeVariable(varName);
        // Assert
        assertNull(Variable.getVariableValue(varName));
    }
    
    @Test
    public void testRemoveVariable_nonExistingVariable() {
        // Arrange
        String varName = "var1";
        
        // Act
        Variable.removeVariable(varName);
        // Assert
        assertNull(Variable.getVariableValue(varName));
    }
    
    @Test
    public void testGetVariableValue_validVariable() {
        // Arrange
        String varName = "var1";
        int varValue = 10;
        Variable.addVariable(varName);
        Variable.setVariableValue(varName, varValue);
        
        // Act
        int result = Variable.getVariableValue(varName);
        // Assert
        assertEquals(varValue, result);
    }
    
    @Test
    public void testGetVariableValue_invalidVariable() {
        // Arrange
        String varName = "var1";
        
        // Act
        int result = Variable.getVariableValue(varName);
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testSetVariableValue_validVariable() {
        // Arrange
        String varName = "var1";
        int varValue = 10;
        Variable.addVariable(varName);
        
        // Act
        Variable.setVariableValue(varName, varValue);
        // Assert
        assertEquals(varValue, Variable.getVariableValue(varName));
    }
    
    @Test
    public void testSetVariableValue_invalidVariable() {
        // Arrange
        String varName = "var1";
        int varValue = 10;
        
        // Act
        Variable.setVariableValue(varName, varValue);
        // Assert
        assertEquals(0, Variable.getVariableValue(varName));
    }
    
    @Test
    public void testFindVariable_existingVariable() {
        // Arrange
        String varName = "var1";
        Variable.addVariable(varName);
        
        // Act
        int result = Variable.findVariable(varName);
        // Assert
        assertTrue(result >= 0);
    }
    
    @Test
    public void testFindVariable_nonExistingVariable() {
        // Arrange
        String varName = "var1";
        
        // Act
        int result = Variable.findVariable(varName);
        // Assert
        assertEquals(-1, result);
    }
}
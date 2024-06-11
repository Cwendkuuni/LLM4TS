package jipa;

import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testAddVariable_alreadyExists() {
        // Arrange
        String varName = "var2";
        Variable.addVariable(varName);
        
        // Act
        boolean result = Variable.addVariable(varName);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testRemoveVariable_typicalCase() {
        // Arrange
        String varName = "var1";
        Variable.addVariable(varName);
        
        // Act
        Variable.removeVariable(varName);
        
        // Assert
        assertEquals(0, Variable.getVariableValue(varName));
    }
    
    @Test
    public void testGetVariableValue_typicalCase() {
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
    public void testGetVariableValue_nonExistingVariable() {
        // Arrange
        String varName = "var1";
        
        // Act
        int result = Variable.getVariableValue(varName);
        
        // Assert
        assertEquals(0, result);
    }
    
    @Test
    public void testGetVariableValue_referencedVariable() {
        // Arrange
        String varName = "var1";
        int varValue = 10;
        Variable.addVariable(varName);
        Variable.setVariableValue(varName, varValue);
        
        // Act
        int result = Variable.getVariableValue("*" + varName);
        
        // Assert
        assertEquals(varValue, result);
    }
    
    @Test
    public void testSetVariableValue_typicalCase() {
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
    public void testSetVariableValue_nonExistingVariable() {
        // Arrange
        String varName = "var1";
        int varValue = 10;
        
        // Act
        Variable.setVariableValue(varName, varValue);
        
        // Assert
        assertEquals(0, Variable.getVariableValue(varName));
    }
    
    @Test
    public void testFindVariable_typicalCase() {
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
    
    @Test
    public void testFindVariable_referencedVariable() {
        // Arrange
        String varName = "var1";
        int varValue = 10;
        Variable.addVariable(varName);
        Variable.setVariableValue(varName, varValue);
        
        // Act
        int result = Variable.findVariable("&" + varName);
        
        // Assert
        assertEquals(varValue, result);
    }
}
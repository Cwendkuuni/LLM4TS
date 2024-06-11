import org.junit.Test;
import static org.junit.Assert.*;

import java.util.List;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.variableList = new ArrayList<Variable>();  // Clear the variable list
        
        // Test adding a new variable
        assertTrue(Variable.addVariable("var1"));
        
        // Test adding an existing variable
        assertFalse(Variable.addVariable("var1"));
    }
    
    @Test
    public void testRemoveVariable() {
        Variable.variableList = new ArrayList<Variable>();  // Clear the variable list
        
        // Add a variable to the list
        Variable variable1 = new Variable("var1");
        Variable.variableList.add(variable1);
        
        // Test removing an existing variable
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
        
        // Test removing a non-existing variable
        Variable.removeVariable("var2");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testGetVariableValue() {
        Variable.variableList = new ArrayList<Variable>();  // Clear the variable list
        
        // Add a variable with a value to the list
        Variable variable = new Variable("var1");
        variable.setValue(10);
        Variable.variableList.add(variable);
        
        // Test getting the value of an existing variable
        assertEquals(10, Variable.getVariableValue("var1"));
        
        // Test getting the value of a non-existing variable
        assertEquals(0, Variable.getVariableValue("var2"));
        
        // Test getting the value of a reference variable
        assertEquals(10, Variable.getVariableValue("*var1"));
    }
    
    @Test
    public void testSetVariableValue() {
        Variable.variableList = new ArrayList<Variable>();  // Clear the variable list
        
        // Add a variable to the list
        Variable variable = new Variable("var1");
        Variable.variableList.add(variable);
        
        // Test setting the value of an existing variable
        Variable.setVariableValue("var1", 10);
        assertEquals(10, variable.getValue());
        
        // Test setting the value of a non-existing variable
        Variable.setVariableValue("var2", 20);
        assertEquals(0, Variable.getVariableValue("var2"));
    }
    
    @Test
    public void testFindVariable() {
        Variable.variableList = new ArrayList<Variable>();  // Clear the variable list
        
        // Add a variable to the list
        Variable variable = new Variable("var1");
        Variable.variableList.add(variable);
        
        // Test finding an existing variable by name
        assertEquals(0, Variable.findVariable("var1"));
        
        // Test finding a non-existing variable by name
        assertEquals(-1, Variable.findVariable("var2"));
        
        // Test finding an existing variable by reference
        assertEquals(0, Variable.findVariable("&var1"));
        
        // Test finding a non-existing variable by reference
        assertEquals(-1, Variable.findVariable("&var2"));
    }
}
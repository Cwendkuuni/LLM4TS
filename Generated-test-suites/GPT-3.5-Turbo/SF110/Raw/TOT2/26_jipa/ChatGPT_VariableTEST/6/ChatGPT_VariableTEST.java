import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class VariableTest {
    
    @Before
    public void setUp() {
        Variable.variableList.clear();
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1")); // Testing duplicate variable addition
        assertTrue(Variable.addVariable("var2"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        // Testing variable removal
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
        assertEquals(1, Variable.variableList.size());
        
        // Testing removal of non-existent variable
        Variable.removeVariable("var3"); // This should not affect the variable list
        assertEquals(1, Variable.variableList.size());
        
        // Testing removal of last variable
        Variable.removeVariable("var2");
        assertFalse(Variable.variableList.contains("var2"));
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        
        // Testing valid variable values
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        
        // Testing wildcard variable values
        assertEquals(1, Variable.getVariableValue("*var1"));
        assertEquals(0, Variable.getVariableValue("*var3")); // Variable does not exist, should return default value 0
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        // Testing setting variable values
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        
        // Testing if values were set correctly
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
    }
}
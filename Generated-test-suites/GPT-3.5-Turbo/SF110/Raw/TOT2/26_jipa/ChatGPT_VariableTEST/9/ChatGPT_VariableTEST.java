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
        assertFalse(Variable.addVariable("var1"));
        assertTrue(Variable.addVariable("var2"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        // Test removing an existing variable
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains("var1"));
        assertEquals(1, Variable.variableList.size());
        
        // Test removing a non-existing variable
        Variable.removeVariable("var3");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        
        // Test getting variable values
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        
        // Test getting value for wildcard variable
        assertEquals(1, Variable.getVariableValue("*var1"));
        
        // Test getting value for non-existing variable
        assertEquals(0, Variable.getVariableValue("*var3"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        
        // Test setting variable values
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        
        // Test setting value for non-existing variable
        Variable.setVariableValue("var3", 3);
        assertEquals(3, Variable.getVariableValue("var3"));
    }

}
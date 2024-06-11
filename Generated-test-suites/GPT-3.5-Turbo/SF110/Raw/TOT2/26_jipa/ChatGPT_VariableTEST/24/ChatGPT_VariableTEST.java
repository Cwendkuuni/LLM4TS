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
        // Typical use case
        assertTrue(Variable.addVariable("var1"));
        
        // Edge case
        assertFalse(Variable.addVariable("var1"));
        
        // Typical use case
        assertTrue(Variable.addVariable("var2"));
    }

    @Test
    public void testRemoveVariable() {
        // Typical use case
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        
        assertFalse(Variable.variableList.contains("var1"));
        assertEquals(1, Variable.variableList.size());
        
        // Typical use case
        Variable.removeVariable("var2");
        
        assertFalse(Variable.variableList.contains("var2"));
        assertEquals(0, Variable.variableList.size());
        
        // Edge case
        Variable.removeVariable("var3");
        
        // Error scenario
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        
        // Typical use case
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        
        // Edge case
        assertEquals(1, Variable.getVariableValue("*var1"));
        
        // Error scenario
        assertEquals(0, Variable.getVariableValue("*var3"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        
        // Typical use case
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
    }

}
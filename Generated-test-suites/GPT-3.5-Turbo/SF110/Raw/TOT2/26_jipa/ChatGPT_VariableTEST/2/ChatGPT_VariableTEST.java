import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ChatGPT_VariableTest {

    @Test
    public void testAddVariable() {
        // typical use case
        assertTrue(Variable.addVariable("var1"));
        
        // edge case: adding duplicate variable
        assertFalse(Variable.addVariable("var1"));
        
        // typical use case
        assertTrue(Variable.addVariable("var2"));
        
        // error scenario: adding null variable
        assertFalse(Variable.addVariable(null));
    }

    @Test
    public void testRemoveVariable() {
        // typical use case
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        
        // edge case: check if removed variable is no longer in the list
        assertFalse(Variable.variableList.contains("var1"));
        
        // typical use case
        assertEquals(1, Variable.variableList.size());
        
        // removing another variable
        Variable.removeVariable("var2");
        
        // check if second variable is removed
        assertFalse(Variable.variableList.contains("var2"));
        
        // check size after second removal
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        // typical use case
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        
        // edge case: variable name with invalid character
        assertEquals(0, Variable.getVariableValue("*var1"));
        
        // error scenario: getting value for non-existent variable
        assertEquals(0, Variable.getVariableValue("var3"));
    }

    @Test
    public void testSetVariableValue() {
        // typical use case
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 1);
        Variable.setVariableValue("var2", 2);
        
        assertEquals(1, Variable.getVariableValue("var1"));
        assertEquals(2, Variable.getVariableValue("var2"));
        
        // error scenario: setting value for non-existent variable
        Variable.setVariableValue("var3", 3);
        assertEquals(0, Variable.getVariableValue("var3"));
    }

}
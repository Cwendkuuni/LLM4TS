
package jipa;
import java.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("A"));
        assertFalse(Variable.addVariable("A"));
        assertTrue(Variable.addVariable("B"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("A");
        Variable.addVariable("B");
        Variable.removeVariable("A");
        assertFalse(Variable.variableList.contains("A"));
        assertTrue(Variable.variableList.contains("B"));
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("A");
        Variable.setVariableValue("A", 10);
        Variable.addVariable("B");
        assertEquals(10, Variable.getVariableValue("A"));
        assertEquals(0, Variable.getVariableValue("B"));

        Variable.addVariable("*A");
        Variable.setVariableValue("A", 20);
        assertEquals(20, Variable.getVariableValue("*A"));
        assertEquals(0, Variable.getVariableValue("B"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("A");
        Variable.addVariable("B");
        Variable.setVariableValue("A", 10);
        Variable.setVariableValue("B", 20);
        assertEquals(10, Variable.getVariableValue("A"));
        assertEquals(20, Variable.getVariableValue("B"));
    }
   
}


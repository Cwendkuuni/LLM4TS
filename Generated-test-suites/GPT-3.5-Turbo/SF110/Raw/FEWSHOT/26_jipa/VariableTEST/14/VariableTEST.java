package jipa;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class VariableTest {

    @Before
    public void setUp() {
        Variable.variableList.clear();
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertFalse(Variable.variableList.contains(new Variable("var1")));
        assertTrue(Variable.variableList.contains(new Variable("var2")));
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(0, Variable.findVariable("*var1"));
        assertEquals(-1, Variable.findVariable("var2"));
    }
}
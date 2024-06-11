package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertTrue(Variable.addVariable("var2"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        assertEquals("var2", Variable.variableList.get(0).getName());
        Variable.removeVariable("var3");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
        Variable.setVariableValue("var2", 10);
        assertEquals(0, Variable.getVariableValue("var2"));
        assertEquals(0, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var2", 10);
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
        assertEquals(-1, Variable.findVariable("var3"));
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.findVariable("&var1"));
        assertEquals(-1, Variable.findVariable("&var2"));
    }
}
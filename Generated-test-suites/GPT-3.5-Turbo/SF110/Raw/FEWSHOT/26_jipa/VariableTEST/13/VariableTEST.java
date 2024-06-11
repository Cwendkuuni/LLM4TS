package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class VariableTest {
    @BeforeClass
    public static void setUp() {
        Variable.variableList.clear();
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertTrue(Variable.addVariable("var2"));
        assertFalse(Variable.addVariable("var1"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertEquals(1, Variable.variableList.size());
        assertEquals("var2", Variable.variableList.get(0).getName());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        assertEquals(0, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("&var1"));
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(10, Variable.getVariableValue("&var1"));
        assertEquals(0, Variable.getVariableValue("&var2"));
        assertEquals(0, Variable.getVariableValue("*var2"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.variableList.get(0).getValue());
        Variable.setVariableValue("var2", 20);
        assertEquals(20, Variable.variableList.get(1).getValue());
        Variable.setVariableValue("var3", 30);
        assertNull(Variable.variableList.get(2));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var3");
        assertEquals(0, Variable.findVariable("var1"));
        assertEquals(1, Variable.findVariable("var2"));
        assertEquals(2, Variable.findVariable("var3"));
        assertEquals(-1, Variable.findVariable("var4"));
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.findVariable("&var1"));
        assertEquals(-1, Variable.findVariable("&var2"));
    }
}
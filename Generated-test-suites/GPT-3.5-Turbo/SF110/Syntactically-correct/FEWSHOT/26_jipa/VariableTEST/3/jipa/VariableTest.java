package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class VariableTest {

    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<Variable>();
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
        assertNull(Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var1", 20);
        assertEquals(20, Variable.getVariableValue("var1"));

        assertEquals(0, Variable.getVariableValue("var2"));
        Variable.addVariable("var2");
        Variable.setVariableValue("var2", 30);
        assertEquals(30, Variable.getVariableValue("var2"));

        Variable.addVariable("var3");
        Variable.setVariableValue("var3", 40);
        assertEquals(0, Variable.getVariableValue("*var1"));
        assertEquals(30, Variable.getVariableValue("*var2"));
        assertEquals(40, Variable.getVariableValue("*var3"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 10);
        Variable.setVariableValue("var2", 20);
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(20, Variable.getVariableValue("var2"));

        Variable.setVariableValue("var1", 30);
        assertEquals(30, Variable.getVariableValue("var1"));

        Variable.setVariableValue("var2", 40);
        assertEquals(40, Variable.getVariableValue("var2"));
    }
}
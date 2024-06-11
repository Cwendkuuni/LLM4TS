package jipa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class VariableTest {

    @BeforeClass
    public static void setup() {
        Variable.variableList = new ArrayList<Variable>();
    }

    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
        assertTrue(Variable.addVariable("var2"));
        assertTrue(Variable.addVariable("var3"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.addVariable("var3");
        Variable.removeVariable("var2");
        assertNull(Variable.getVariableValue("var2"));
        assertEquals(0, Variable.findVariable("var2"));
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        assertEquals(0, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        Variable.setVariableValue("var2", 5); // Variable does not exist
        assertNull(Variable.getVariableValue("var2"));
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
    }
}
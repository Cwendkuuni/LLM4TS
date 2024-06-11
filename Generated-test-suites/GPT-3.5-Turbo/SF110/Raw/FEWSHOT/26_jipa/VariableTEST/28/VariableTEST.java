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
        assertTrue(Variable.addVariable("varName1"));
        assertTrue(Variable.addVariable("varName2"));
        assertFalse(Variable.addVariable("varName2"));
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("varName1");
        Variable.addVariable("varName2");
        Variable.removeVariable("varName1");
        assertNull(Variable.findVariable("varName1"));
        assertEquals(0, Variable.getVariableValue("varName1"));
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("varName1");
        Variable.setVariableValue("varName1", 10);
        assertEquals(10, Variable.getVariableValue("varName1"));
        assertEquals(0, Variable.getVariableValue("varName2"));
        assertEquals(0, Variable.getVariableValue("*varName2"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("varName1");
        Variable.setVariableValue("varName1", 10);
        assertEquals(10, Variable.getVariableValue("varName1"));
        Variable.setVariableValue("varName2", 20);
        assertEquals(0, Variable.getVariableValue("varName2"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("varName1");
        Variable.setVariableValue("varName1", 10);
        assertEquals(0, Variable.findVariable("varName1"));
        assertEquals(-1, Variable.findVariable("varName2"));
    }

}
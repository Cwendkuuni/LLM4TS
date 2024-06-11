
package jipa;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {

    @BeforeClass
    public static void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }

    @AfterClass
    public static void tearDown() {
        Variable.variableList.clear();
    }

    @Test
    public void testAddVariable_ValidName_Success() {
        assertTrue(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
        assertEquals("var1", Variable.variableList.get(0).getName());
        assertEquals(0, Variable.variableList.get(0).getValue());
    }

    @Test
    public void testAddVariable_DuplicateName_Failure() {
        assertTrue(Variable.addVariable("var2"));
        assertFalse(Variable.addVariable("var2"));
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable_ExistingVariable_Success() {
        assertTrue(Variable.addVariable("var3"));
        assertEquals(1, Variable.variableList.size());
        Variable.removeVariable("var3");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable_NonexistentVariable_NoEffect() {
        assertEquals(0, Variable.variableList.size());
        Variable.removeVariable("var4");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue_ExistingVariable_Success() {
        assertTrue(Variable.addVariable("var5"));
        assertEquals(1, Variable.variableList.size());
        assertEquals(0, Variable.getVariableValue("var5"));
    }

    @Test
    public void testGetVariableValue_NonexistentVariable_ReturnsDefault() {
        assertEquals(0, Variable.variableList.size());
        assertEquals(0, Variable.getVariableValue("var6"));
    }

    @Test
    public void testSetVariableValue_ExistingVariable_Success() {
        assertTrue(Variable.addVariable("var7"));
        assertEquals(1, Variable.variableList.size());
        Variable.setVariableValue("var7", 10);
        assertEquals(10, Variable.getVariableValue("var7"));
    }

    @Test
    public void testSetVariableValue_NonexistentVariable_NoEffect() {
        assertEquals(0, Variable.variableList.size());
        Variable.setVariableValue("var8", 20);
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testFindVariable_ExistingVariable_Success() {
        assertTrue(Variable.addVariable("var9"));
        assertEquals(1, Variable.variableList.size());
        assertEquals(0, Variable.findVariable("var9"));
    }

    @Test
    public void testFindVariable_NonexistentVariable_ReturnsNegative() {
        assertEquals(0, Variable.variableList.size());
        assertEquals(-1, Variable.findVariable("var10"));
    }

    @Test
    public void testFindVariable_ExistingReferenceVariable_Success() {
        assertTrue(Variable.addVariable("var11"));
        Variable.setVariableValue("var11", 30);
        assertEquals(1, Variable.variableList.size());
        assertEquals(30, Variable.findVariable("&&var11"));
    }

    @Test
    public void testFindVariable_ExistingVariableAsReference_ReturnsValue() {
        assertTrue(Variable.addVariable("var12"));
        Variable.setVariableValue("var12", 40);
        assertEquals(1, Variable.variableList.size());
        assertEquals(40, Variable.findVariable("*var12"));
    }
}



package jipa;

import static org.junit.Assert.*;
import org.junit.*;

public class VariableTest {
    
    @BeforeClass
    public static void setup() {
        Variable.variableList.clear();
    }
    
    @Test
    public void testAddVariable_typicalCase() {
        assertTrue(Variable.addVariable("x"));
    }
    
    @Test
    public void testAddVariable_variableAlreadyExists() {
        Variable.addVariable("y");
        assertFalse(Variable.addVariable("y"));
    }
    
    @Test
    public void testRemoveVariable_typicalCase() {
        Variable.addVariable("z");
        Variable.removeVariable("z");
        assertEquals(0, Variable.variableList.size());
    }
    
    @Test
    public void testRemoveVariable_variableDoesNotExist() {
        Variable.removeVariable("w");
    }
    
    @Test
    public void testGetVariableValue_typicalCase() {
        Variable.addVariable("a");
        Variable.setVariableValue("a", 5);
        assertEquals(5, Variable.getVariableValue("a"));
    }
    
    @Test
    public void testGetVariableValue_variableNotInList() {
        assertEquals(0, Variable.getVariableValue("b"));
    }
    
    @Test
    public void testGetVariableValue_variableIsReference() {
        Variable.addVariable("c");
        Variable.setVariableValue("c", 2);
        assertEquals(2, Variable.getVariableValue("*c"));
    }
    
    @Test
    public void testSetVariableValue_typicalCase() {
        Variable.addVariable("d");
        Variable.setVariableValue("d", 7);
        assertEquals(7, Variable.getVariableValue("d"));
    }
    
    @Test
    public void testSetVariableValue_variableNotInList() {
        Variable.setVariableValue("e", 3);
    }
    
    @Test
    public void testSetVariableValue_variableWithReferencePrefix() {
        Variable.addVariable("f");
        Variable.setVariableValue("f", 4);
        Variable.setVariableValue("*f", 8);
        assertEquals(8, Variable.getVariableValue("f"));
    }
    
    @Test
    public void testFindVariable_typicalCase() {
        Variable.addVariable("g");
        Variable.addVariable("h");
        assertEquals(0, Variable.findVariable("g"));
    }
    
    @Test
    public void testFindVariable_variableNotInList() {
        assertEquals(-1, Variable.findVariable("i"));
    }
    
    @Test
    public void testFindVariable_variableWithReferencePrefix() {
        Variable.addVariable("j");
        Variable.setVariableValue("j", 6);
        assertEquals(6, Variable.findVariable("*j"));
    }
}

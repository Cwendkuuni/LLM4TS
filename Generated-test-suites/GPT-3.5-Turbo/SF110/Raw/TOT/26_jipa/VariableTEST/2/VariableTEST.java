import org.junit.*;
import static org.junit.Assert.*;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.addVariable("var1");
        assertTrue(Variable.variableList.size() == 1);
        Variable.addVariable("var2");
        assertTrue(Variable.variableList.size() == 2);
    }

    @Test
    public void testAddVariable_duplicateVariable() {
        Variable.addVariable("var1");
        assertFalse(Variable.addVariable("var1")); // duplicate variable should not be added
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertTrue(Variable.variableList.size() == 1);
        Variable.removeVariable("var2");
        assertTrue(Variable.variableList.size() == 0);
    }

    @Test
    public void testGetVariableValue_existingVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1"));
    }

    @Test
    public void testGetVariableValue_nonExistingVariable() {
        assertEquals(0, Variable.getVariableValue("var1")); // non-existing variable should return 0
    }

    @Test
    public void testGetVariableValue_referencedVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("*var1")); // referenced variable should be resolved and return its value
    }

    @Test
    public void testSetVariableValue_existingVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.getVariableValue("var1")); // existing variable should have its value updated
    }

    @Test
    public void testSetVariableValue_nonExistingVariable() {
        Variable.setVariableValue("var1", 5);
        assertEquals(0, Variable.getVariableValue("var1")); // non-existing variable should not have its value updated
    }

    @Test
    public void testFindVariable_existingVariable() {
        Variable.addVariable("var1");
        assertEquals(0, Variable.findVariable("var1")); // existing variable should return its index in the variable list
    }

    @Test
    public void testFindVariable_nonExistingVariable() {
        assertEquals(-1, Variable.findVariable("var1")); // non-existing variable should return -1
    }

    @Test
    public void testFindVariable_referencedVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(5, Variable.findVariable("*var1")); // referenced variable should be resolved and return its value
    }
    
    @Test
    public void testFindVariable_existingReferencedVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        assertEquals(0, Variable.findVariable("&var1")); // existing referenced variable should return its index in the variable list
    }
    
    @Test
    public void testFindVariable_nonExistingReferencedVariable() {
        assertEquals(-1, Variable.findVariable("&var1")); // non-existing referenced variable should return -1
    }

}
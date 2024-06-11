import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {

    @BeforeClass
    public static void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }

    // Test addVariable() method
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertFalse(Variable.addVariable("var1"));
    }

    // Test removeVariable() method
    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.removeVariable("var1");
        assertNull(findVariable("var1"));
    }

    // Test getVariableValue() method
    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("var2"));
        assertNull(findVariable("var2"));
    }

    // Test setVariableValue() method
    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }

    // Helper method to find a Variable by name
    private Variable findVariable(String varName) {
        for (Variable variable : Variable.variableList) {
            if (variable.getName().equals(varName))
                return variable;
        }
        return null;
    }

    // Test for exceptions in findVariable() method
    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindVariableException() {
        Variable.findVariable("nonexistent");
    }

    // Test for edge case of empty variable list in getVariableValue() method
    @Test
    public void testGetVariableValueWithEmptyList() {
        Variable.variableList.clear();
        assertEquals(0, Variable.getVariableValue("var1"));
    }

    // Test for edge case of empty variable list in removeVariable() method
    @Test
    public void testRemoveVariableWithEmptyList() {
        Variable.variableList.clear();
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }

    // Test for edge case of empty variable list in setVariableValue() method
    @Test
    public void testSetVariableValueWithEmptyList() {
        Variable.variableList.clear();
        Variable.setVariableValue("var1", 10);
        assertEquals(0, Variable.variableList.size());
    }

    // Test for edge case of empty variable list in findVariable() method
    @Test
    public void testFindVariableWithEmptyList() {
        Variable.variableList.clear();
        assertNull(Variable.findVariable("var1"));
    }

    // Test for edge case of empty variable list in addVariable() method
    @Test
    public void testAddVariableWithEmptyList() {
        Variable.variableList.clear();
        assertTrue(Variable.addVariable("var1"));
    }

    // Test for edge case of reference variable in findVariable() method
    @Test
    public void testFindReferenceVariable() {
        Variable.addVariable("*var1");
        assertEquals(Variable.getVariableValue("var1"), Variable.findVariable("&var1"));
    }

    // Test for edge case of reference variable in getVariableValue() method
    @Test
    public void testGetReferenceVariableValue() {
        Variable.addVariable("*var1");
        Variable.setVariableValue("var1", 10);
        assertEquals(10, Variable.getVariableValue("&var1"));
    }

    // Test for edge case of reference variable in setVariableValue() method
    @Test
    public void testSetReferenceVariableValue() {
        Variable.addVariable("*var1");
        Variable.setVariableValue("&var1", 10);
        assertEquals(10, Variable.getVariableValue("var1"));
    }

    // Test for edge case of reference variable in removeVariable() method
    @Test
    public void testRemoveReferenceVariable() {
        Variable.addVariable("*var1");
        Variable.removeVariable("&var1");
        assertEquals(0, Variable.variableList.size());
    }

    // End of test cases
    @AfterClass
    public static void tearDown() {
        Variable.variableList = null;
    }

}

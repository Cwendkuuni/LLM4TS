import org.junit.*;
import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

public class VariableTest {

    @BeforeClass
    public static void setUp() {
        Variable.variableList = new ArrayList<Variable>();
    }

    // Test case for addVariable method: typical use case
    @Test
    public void testAddVariable() {
        assertTrue(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
        assertEquals("var1", Variable.variableList.get(0).getName());
        assertEquals(0, Variable.variableList.get(0).getValue());
    }

    // Test case for addVariable method: variable name already exists
    @Test
    public void testAddVariable_existingVariable() {
        Variable.variableList.add(new Variable("var2"));
        assertFalse(Variable.addVariable("var2"));
        assertEquals(1, Variable.variableList.size());
    }

    // Test case for removeVariable method: typical use case
    @Test
    public void testRemoveVariable() {
        Variable.variableList.add(new Variable("var3"));
        Variable.removeVariable("var3");
        assertEquals(0, Variable.variableList.size());
    }

    // Test case for removeVariable method: variable name does not exist
    @Test
    public void testRemoveVariable_nonExistentVariable() {
        Variable.removeVariable("var4");
        assertEquals(0, Variable.variableList.size());
    }

    // Test case for getVariableValue method: typical use case
    @Test
    public void testGetVariableValue() {
        Variable.variableList.add(new Variable("var5"));
        Variable.variableList.get(0).setValue(10);
        assertEquals(10, Variable.getVariableValue("var5"));
    }

    // Test case for getVariableValue method: variable name starts with *
    @Test
    public void testGetVariableValue_referenceVariable() {
        Variable.variableList.add(new Variable("var6"));
        Variable.variableList.get(0).setValue(20);
        assertEquals(20, Variable.getVariableValue("*var6"));
    }

    // Test case for getVariableValue method: variable name does not exist
    @Test
    public void testGetVariableValue_nonExistentVariable() {
        assertEquals(0, Variable.getVariableValue("var7"));
    }

    // Test case for getVariableValue method: variable name starts with & and it exists in the list
    @Test
    public void testGetVariableValue_referenceVariableExists() {
        Variable.variableList.add(new Variable("var8"));
        Variable.variableList.get(0).setValue(30);
        assertEquals(30, Variable.getVariableValue("&var8"));
    }

    // Test case for getVariableValue method: variable name starts with & but it does not exist in the list
    @Test
    public void testGetVariableValue_referenceVariableNonExistent() {
        assertEquals(0, Variable.getVariableValue("&var9"));
    }

    // Test case for setVariableValue method: typical use case
    @Test
    public void testSetVariableValue() {
        Variable.variableList.add(new Variable("var10"));
        Variable.setVariableValue("var10", 40);
        assertEquals(40, Variable.variableList.get(0).getValue());
    }

    // Test case for setVariableValue method: variable name does not exist
    @Test
    public void testSetVariableValue_nonExistentVariable() {
        Variable.setVariableValue("var11", 50);
        assertEquals(0, Variable.variableList.size());
    }

    // Test case for findVariable method: typical use case - variable is found
    @Test
    public void testFindVariable_existingVariable() {
        Variable.variableList.add(new Variable("var12"));
        Variable.variableList.get(0).setValue(60);
        assertEquals(0, Variable.findVariable("var12"));
    }

    // Test case for findVariable method: variable name starts with &
    @Test
    public void testFindVariable_referenceVariable() {
        Variable.variableList.add(new Variable("var13"));
        Variable.variableList.get(0).setValue(70);
        assertEquals(70, Variable.findVariable("&var13"));
    }

    // Test case for findVariable method: variable name does not exist
    @Test
    public void testFindVariable_nonExistentVariable() {
        assertEquals(-1, Variable.findVariable("var14"));
    }

    // Test case for findVariable method: variable name starts with & but it does not exist in the list
    @Test
    public void testFindVariable_referenceVariableNonExistent() {
        assertEquals(-1, Variable.findVariable("&var15"));
    }

    @AfterClass
    public static void tearDown() {
        Variable.variableList = null;
    }
}
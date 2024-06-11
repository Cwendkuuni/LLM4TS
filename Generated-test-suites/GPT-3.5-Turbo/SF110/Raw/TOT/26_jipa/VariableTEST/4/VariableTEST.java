import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class VariableTest {

    @BeforeClass
    public static void setUp() {
        // Initialize variableList before each test case
        Variable.variableList = new ArrayList<Variable>();
    }

    @Test
    public void testAddVariable_typicalCase() {
        // Creating a new variable with a unique name
        String varName = "var1";
        boolean result = Variable.addVariable(varName);

        // Asserting that the variable has been added successfully
        assertTrue(result);
        assertEquals(1, Variable.variableList.size());
        assertEquals(varName, Variable.variableList.get(0).getName());
    }

    @Test
    public void testAddVariable_existingVariable() {
        // Adding a variable with the same name as an existing variable
        String varName = "var1";
        Variable.addVariable(varName);
        boolean result = Variable.addVariable(varName);

        // Asserting that the duplicate variable is not added
        assertFalse(result);
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable_existingVariable() {
        // Adding a variable and then removing it
        String varName = "var1";
        Variable.addVariable(varName);
        Variable.removeVariable(varName);

        // Asserting that the variable has been removed successfully
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable_nonExistingVariable() {
        // Removing a variable that does not exist
        String varName = "var1";
        Variable.removeVariable(varName);

        // Asserting that there are no changes in the variable list
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue_typicalCase() {
        // Adding a variable and then getting its value
        String varName = "var1";
        Variable.addVariable(varName);
        int varValue = 100;
        Variable.setVariableValue(varName, varValue);
        int result = Variable.getVariableValue(varName);

        // Asserting that the correct value is returned
        assertEquals(varValue, result);
    }

    @Test
    public void testGetVariableValue_variableNotFound() {
        // Getting the value of a non-existing variable
        String varName = "var1";
        int result = Variable.getVariableValue(varName);

        // Asserting that the default value (0) is returned
        assertEquals(0, result);
    }

    @Test
    public void testGetVariableValue_referencedVariable() {
        // Adding a variable and then getting the value by reference
        String varName = "var1";
        Variable.addVariable(varName);
        int varValue = 100;
        Variable.setVariableValue(varName, varValue);
        String varRef = "*" + varName;
        int result = Variable.getVariableValue(varRef);

        // Asserting that the correct value is returned
        assertEquals(varValue, result);
    }

    @Test
    public void testSetVariableValue_existingVariable() {
        // Adding a variable and then setting its value
        String varName = "var1";
        Variable.addVariable(varName);
        int varValue = 100;
        Variable.setVariableValue(varName, varValue);
        int result = Variable.getVariableValue(varName);

        // Asserting that the value has been set correctly
        assertEquals(varValue, result);
    }

    @Test
    public void testSetVariableValue_nonExistingVariable() {
        // Setting the value of a non-existing variable
        String varName = "var1";
        int varValue = 100;
        Variable.setVariableValue(varName, varValue);

        // Asserting that there are no changes in the variable list
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testFindVariable_existingVariable() {
        // Adding a variable and then finding it
        String varName = "var1";
        Variable.addVariable(varName);
        int result = Variable.findVariable(varName);

        // Asserting that the correct index is returned
        int expectedIndex = Variable.variableList.size() - 1;
        assertEquals(expectedIndex, result);
    }

    @Test
    public void testFindVariable_nonExistingVariable() {
        // Finding a non-existing variable
        String varName = "var1";
        int result = Variable.findVariable(varName);

        // Asserting that -1 is returned (indicating variable not found)
        assertEquals(-1, result);
    }

    @Test
    public void testFindVariable_referencedVariable() {
        // Adding a variable and then finding it by reference
        String varName = "var1";
        Variable.addVariable(varName);
        int varValue = 100;
        Variable.setVariableValue(varName, varValue);
        String varRef = "&" + varName;
        int result = Variable.findVariable(varRef);

        // Asserting that the value of the variable is returned
        assertEquals(varValue, result);
    }

    @AfterClass
    public static void tearDown() {
        // Clean up variableList after all test cases
        Variable.variableList = null;
    }
}
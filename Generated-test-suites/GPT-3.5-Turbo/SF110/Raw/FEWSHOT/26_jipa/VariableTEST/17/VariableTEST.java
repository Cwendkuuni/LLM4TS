import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

public class VariableTest {

    @BeforeClass
    public static void initializeVariableList() {
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
        assertNull(findVariable("var1"));
        assertEquals(1, Variable.variableList.size());

        Variable.removeVariable("var3");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.addVariable("var2");
        Variable.setVariableValue("var1", 10);

        assertEquals(0, Variable.getVariableValue("var3"));
        assertEquals(10, Variable.getVariableValue("var1"));
        assertEquals(0, Variable.getVariableValue("*var1"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);

        assertEquals(10, Variable.getVariableValue("var1"));
        assertNull(findVariable("*var1"));

        Variable.setVariableValue("var2", 5);
        assertEquals(0, Variable.getVariableValue("var2"));
    }
    
    private Variable findVariable(String varName) {
        for (Variable variable : Variable.variableList) {
            if (variable.getName().equals(varName)) {
                return variable;
            }
        }
        return null;
    }
}
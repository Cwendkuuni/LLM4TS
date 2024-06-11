import org.junit.Before;
import org.junit.Test;
import java.util.*;

public class VariableTest {

    @Before
    public void setUp() {
        Variable.variableList = new ArrayList<>();
    }

    @Test
    public void testAddVariable() {
        boolean result = Variable.addVariable("var1");
        assertTrue(result);
    }

    @Test
    public void testAddVariable_existingVariable() {
        Variable.addVariable("var1");
        boolean result = Variable.addVariable("var1");
        assertFalse(result);
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("var1");
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable_nonExistingVariable() {
        Variable.removeVariable("var1");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        int result = Variable.getVariableValue("var1");
        assertEquals(0, result);
    }

    @Test
    public void testGetVariableValue_nonExistingVariable() {
        int result = Variable.getVariableValue("var1");
        assertEquals(0, result);
    }

    @Test
    public void testGetVariableValue_referencedVariable() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        int result = Variable.getVariableValue("*var1");
        assertEquals(10, result);
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 10);
        int result = Variable.getVariableValue("var1");
        assertEquals(10, result);
    }

    @Test
    public void testSetVariableValue_nonExistingVariable() {
        Variable.setVariableValue("var1", 10);
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var1");
        int result = Variable.findVariable("var1");
        assertEquals(0, result);
    }

    @Test
    public void testFindVariable_nonExistingVariable() {
        int result = Variable.findVariable("var1");
        assertEquals(-1, result);
    }
}
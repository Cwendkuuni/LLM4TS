
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class VariableTest {

    @Before
    public void setUp() {
        Variable.variableList.clear();
    }

    @Test
    public void testAddVariable_typicalCase() {
        assertTrue(Variable.addVariable("var1"));
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testAddVariable_alreadyExistingVariable() {
        Variable.variableList.add(new Variable("var2"));
        assertFalse(Variable.addVariable("var2"));
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable_typicalCase() {
        Variable.variableList.add(new Variable("var3"));
        Variable.removeVariable("var3");
        assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable_nonExistingVariable() {
        Variable.variableList.add(new Variable("var4"));
        Variable.removeVariable("var5");
        assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue_existingVariable() {
        Variable.variableList.add(new Variable("var6"));
        Variable.setVariableValue("var6", 100);
        assertEquals(100, Variable.getVariableValue("var6"));
    }

    @Test
    public void testGetVariableValue_nonExistingVariable() {
        assertEquals(0, Variable.getVariableValue("var7"));
    }

    @Test
    public void testGetVariableValue_referencedVariable() {
        Variable.variableList.add(new Variable("var8"));
        Variable.setVariableValue("var8", 200);
        // var9 is a reference to var8
        assertEquals(200, Variable.getVariableValue("*var8"));
    }

    @Test
    public void testSetVariableValue_existingVariable() {
        Variable.variableList.add(new Variable("var10"));
        Variable.setVariableValue("var10", 300);
        assertEquals(300, Variable.getVariableValue("var10"));
    }

    @Test
    public void testSetVariableValue_nonExistingVariable() {
        Variable.setVariableValue("var11", 400);
        assertEquals(0, Variable.getVariableValue("var11"));
    }

    @Test
    public void testFindVariable_existingVariable() {
        Variable.variableList.add(new Variable("var12"));
        Variable.setVariableValue("var12", 500);
        assertEquals(0, Variable.findVariable("var12"));
    }

    @Test
    public void testFindVariable_nonExistingVariable() {
        assertEquals(-1, Variable.findVariable("var13"));
    }

    @Test
    public void testFindVariable_referencedVariable() {
        Variable.variableList.add(new Variable("var14"));
        Variable.setVariableValue("var14", 600);
        // var15 is a reference to var14
        assertEquals(600, Variable.findVariable("*var14"));
    }
}


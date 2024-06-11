
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class VariableTest {

    private Variable variable;
    private List<Variable> variableList;

    @BeforeEach
    public void setup() {
        variable = new Variable("test");
        variableList = Variable.variableList;
    }

    @Test
    public void testGetValue() {
        int value = variable.getValue();
        Assertions.assertEquals(0, value);
    }

    @Test
    public void testSetValue() {
        variable.setValue(10);
        int value = variable.getValue();
        Assertions.assertEquals(10, value);
    }

    @Test
    public void testGetName() {
        String name = variable.getName();
        Assertions.assertEquals("test", name);
    }

    @Test
    public void testAddVariable() {
        boolean added = Variable.addVariable("newVariable");
        Assertions.assertTrue(added);
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("toBeRemoved");
        Variable.removeVariable("toBeRemoved");
        int index = Variable.findVariable("toBeRemoved");
        Assertions.assertEquals(-1, index);
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("var1");
        Variable.setVariableValue("var1", 5);
        int value = Variable.getVariableValue("var1");
        Assertions.assertEquals(5, value);
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("var2");
        Variable.setVariableValue("var2", 7);
        int value = Variable.getVariableValue("var2");
        Assertions.assertEquals(7, value);
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("var3");
        int index = Variable.findVariable("var3");
        Assertions.assertNotEquals(-1, index);
    }
}

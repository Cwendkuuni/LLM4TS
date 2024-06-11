
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class VariableTest {

    @Test
    public void testAddVariable() {
        Variable.addVariable("testVariable");
        Assertions.assertEquals(1, Variable.variableList.size());
        Variable.addVariable("testVariable");
        Assertions.assertEquals(1, Variable.variableList.size());
    }

    @Test
    public void testRemoveVariable() {
        Variable.addVariable("testVariable");
        Assertions.assertEquals(1, Variable.variableList.size());
        Variable.removeVariable("testVariable");
        Assertions.assertEquals(0, Variable.variableList.size());
    }

    @Test
    public void testGetVariableValue() {
        Variable.addVariable("testVariable");
        Variable.setVariableValue("testVariable", 10);
        Assertions.assertEquals(10, Variable.getVariableValue("testVariable"));
        Assertions.assertEquals(0, Variable.getVariableValue("nonExistentVariable"));
        Assertions.assertEquals(10, Variable.getVariableValue("*testVariable"));
    }

    @Test
    public void testSetVariableValue() {
        Variable.addVariable("testVariable");
        Variable.setVariableValue("testVariable", 10);
        Assertions.assertEquals(10, Variable.getVariableValue("testVariable"));
        Variable.setVariableValue("nonExistentVariable", 5);
        Assertions.assertEquals(0, Variable.getVariableValue("nonExistentVariable"));
    }

    @Test
    public void testFindVariable() {
        Variable.addVariable("testVariable");
        Assertions.assertEquals(0, Variable.findVariable("testVariable"));
        Assertions.assertEquals(-1, Variable.findVariable("nonExistentVariable"));
    }
}

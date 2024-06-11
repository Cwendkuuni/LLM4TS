
import org.junit.Assert;
import org.junit.Test;

public class VariableTest {

    @Test
    public void testAddVariable() {
        // Arrange
        String varName = "testVar";

        // Act
        boolean result = Variable.addVariable(varName);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testRemoveVariable() {
        // Arrange
        String varName = "testVar";
        Variable.addVariable(varName);

        // Act
        Variable.removeVariable(varName);

        // Assert
        Assert.assertEquals(-1, Variable.findVariable(varName));
    }

    @Test
    public void testGetVariableValue() {
        // Arrange
        String varName = "testVar";
        int varValue = 42;
        Variable.addVariable(varName);
        Variable.setVariableValue(varName, varValue);

        // Act
        int result = Variable.getVariableValue(varName);

        // Assert
        Assert.assertEquals(varValue, result);
    }

    @Test
    public void testSetVariableValue() {
        // Arrange
        String varName = "testVar";
        int varValue = 42;
        Variable.addVariable(varName);

        // Act
        Variable.setVariableValue(varName, varValue);
        int result = Variable.getVariableValue(varName);

        // Assert
        Assert.assertEquals(varValue, result);
    }

    @Test
    public void testFindVariable() {
        // Arrange
        String varName = "testVar";
        Variable.addVariable(varName);

        // Act
        int result = Variable.findVariable(varName);

        // Assert
        Assert.assertEquals(0, result);
    }
}

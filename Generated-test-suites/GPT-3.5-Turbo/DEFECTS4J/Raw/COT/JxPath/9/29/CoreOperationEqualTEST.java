
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

    private EvalContext context;
    private CoreOperationEqual operationEqual;

    @Test
    public void testComputeValue_equalValues_returnsTrue() {
        // Arrange
        Object arg1 = "abc";
        Object arg2 = "abc";
        operationEqual = new CoreOperationEqual(arg1, arg2);
        // Act
        Object result = operationEqual.computeValue(context);
        // Assert
        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_unequalValues_returnsFalse() {
        // Arrange
        Object arg1 = "abc";
        Object arg2 = "def";
        operationEqual = new CoreOperationEqual(arg1, arg2);
        // Act
        Object result = operationEqual.computeValue(context);
        // Assert
        Assert.assertFalse((Boolean) result);
    }
}

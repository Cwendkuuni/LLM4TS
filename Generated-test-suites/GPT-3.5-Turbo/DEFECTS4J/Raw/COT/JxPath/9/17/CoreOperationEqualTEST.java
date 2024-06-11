import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

    // Extracted public methods:
    // - computeValue(EvalContext context)
    // - getSymbol()

    @Test
    public void testComputeValueEquals() {
        // Test when the two arguments are equal
        CoreOperationEqual operation = new CoreOperationEqual(new Expression(), new Expression());

        EvalContext context = new EvalContext(null);
        context.setValue(operation.args[0], "value");
        context.setValue(operation.args[1], "value");

        Assert.assertTrue((Boolean) operation.computeValue(context));
    }

    @Test
    public void testComputeValueNotEquals() {
        // Test when the two arguments are not equal
        CoreOperationEqual operation = new CoreOperationEqual(new Expression(), new Expression());

        EvalContext context = new EvalContext(null);
        context.setValue(operation.args[0], "value1");
        context.setValue(operation.args[1], "value2");

        Assert.assertFalse((Boolean) operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual operation = new CoreOperationEqual(new Expression(), new Expression());
        Assert.assertEquals("=", operation.getSymbol());
    }

    // Additional test cases for edge cases and exception scenarios

    @Test
    public void testComputeValueNullArguments() {
        // Test when one or both arguments are null
        CoreOperationEqual operation = new CoreOperationEqual(new Expression(), new Expression());

        EvalContext context = new EvalContext(null);
        context.setValue(operation.args[0], null);
        context.setValue(operation.args[1], null);

        Assert.assertTrue((Boolean) operation.computeValue(context));
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueNullContext() {
        // Test when the evaluation context is null
        CoreOperationEqual operation = new CoreOperationEqual(new Expression(), new Expression());
        operation.computeValue(null); // This should throw a NullPointerException
    }

    // 
    // All the test cases go above this line

    // 
}
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = createContext();
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithEqualValues() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = createContextWithEqualValues();
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithGreaterValue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = createContextWithGreaterValue();
        Object result = expression.computeValue(context);
        Assert.assertTrue(result instanceof Boolean);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(arg1, arg2);
        String symbol = expression.getSymbol();
        Assert.assertEquals("<=", symbol);
    }

    private EvalContext createContext() {
        // TODO: Create and return a mock EvalContext object
    }

    private EvalContext createContextWithEqualValues() {
        // TODO: Create and return a mock EvalContext object with equal values
    }

    private EvalContext createContextWithGreaterValue() {
        // TODO: Create and return a mock EvalContext object with a greater value
    }
}
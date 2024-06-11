import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        // Typical use case: l > r
        Expression arg1 = new Expression(5);
        Expression arg2 = new Expression(3);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Typical use case: l <= r
        arg1 = new Expression(3);
        arg2 = new Expression(5);
        operation = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        // Edge case: l == r
        arg1 = new Expression(5);
        arg2 = new Expression(5);
        operation = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        // Edge case: l and r are negative
        arg1 = new Expression(-5);
        arg2 = new Expression(-10);
        operation = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Error scenario: l and r are null
        arg1 = new Expression(null);
        arg2 = new Expression(null);
        operation = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression(5);
        Expression arg2 = new Expression(3);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        String symbol = operation.getSymbol();
        Assert.assertEquals(">", symbol);
    }
}
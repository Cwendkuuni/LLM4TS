import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);

        Assert.assertTrue(result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        String symbol = coreOperationEqual.getSymbol();

        Assert.assertEquals("=", symbol);
    }

    // TODO: Add test cases for potential edge cases and exception handling scenarios

}


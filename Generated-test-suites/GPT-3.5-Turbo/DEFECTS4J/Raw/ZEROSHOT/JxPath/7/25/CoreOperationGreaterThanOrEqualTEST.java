import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        EvalContext context = new EvalContext();
        
        // Test case 1
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual operation1 = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result1 = operation1.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result1);
        
        // Test case 2
        Expression arg3 = new Expression();
        Expression arg4 = new Expression();
        CoreOperationGreaterThanOrEqual operation2 = new CoreOperationGreaterThanOrEqual(arg3, arg4);
        Object result2 = operation2.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result2);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        String symbol = operation.getSymbol();
        Assert.assertEquals(">=", symbol);
    }
}
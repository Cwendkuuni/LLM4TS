import org.junit.Test;
import org.junit.Assert;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        Assert.assertEquals(true, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(5);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        Assert.assertEquals(">=", symbol);
    }

    // Edge cases and exception handling scenarios
    @Test
    public void testComputeValueWithNegatives() {
        Expression arg1 = new Constant(-5);
        Expression arg2 = new Constant(-3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        Assert.assertEquals(false, result);
    }

    @Test
    public void testComputeValueWithZero() {
        Expression arg1 = new Constant(0);
        Expression arg2 = new Constant(0);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        Assert.assertEquals(true, result);
    }

}
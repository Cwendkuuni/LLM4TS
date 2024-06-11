import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        
        // Test greater than or equal values
        Expression arg1 = new CoreVariable("arg1");
        Expression arg2 = new CoreVariable("arg2");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 3);
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        context.setVariable("arg1", 3);
        context.setVariable("arg2", 3);
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        context.setVariable("arg1", "abc");
        context.setVariable("arg2", "abc");
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test lesser values
        context.setVariable("arg1", 3);
        context.setVariable("arg2", 5);
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        context.setVariable("arg1", 1);
        context.setVariable("arg2", 3);
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // Test double values
        context.setVariable("arg1", 5.0);
        context.setVariable("arg2", 3.0);
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        context.setVariable("arg1", 3.0);
        context.setVariable("arg2", 3.0);
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        context.setVariable("arg1", 1.0);
        context.setVariable("arg2", 3.0);
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new CoreVariable("arg1");
        Expression arg2 = new CoreVariable("arg2");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        Assert.assertEquals(">=", operation.getSymbol());
    }
}
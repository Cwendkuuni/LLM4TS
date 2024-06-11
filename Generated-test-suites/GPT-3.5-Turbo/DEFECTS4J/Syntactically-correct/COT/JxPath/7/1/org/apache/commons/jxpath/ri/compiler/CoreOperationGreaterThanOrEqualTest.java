import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    // Test computeValue method
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        EvalContext context = null;
        
        // Test when both arguments are null
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
        
        // Test when the first argument is null
        operation = new CoreOperationGreaterThanOrEqual(null, new Expression());
        result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
        
        // Test when the second argument is null
        operation = new CoreOperationGreaterThanOrEqual(new Expression(), null);
        result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
        
        // Test when both arguments are non-null
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    // Test getSymbol method
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        
        String symbol = operation.getSymbol();
        Assert.assertEquals(">=", symbol);
    }
    
    // Add additional test cases here for potential edge cases and exceptions
    
}


// Add all the JUnit 4 test methods here


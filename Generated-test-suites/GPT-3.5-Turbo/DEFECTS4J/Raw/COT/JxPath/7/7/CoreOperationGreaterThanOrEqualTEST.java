import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Variable("x");
        Expression arg2 = new Variable("y");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals(">=", symbol);
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testComputeValueWithNaN() {
        Expression arg1 = new Constant(Double.NaN);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValueWithInfinity() {
        Expression arg1 = new Constant(Double.POSITIVE_INFINITY);
        Expression arg2 = new Constant(Double.NEGATIVE_INFINITY);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullContext() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        operation.computeValue(null); // NullPointerException expected
    }
    
}
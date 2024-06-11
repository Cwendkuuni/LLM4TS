import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Typical case where arg1 >= arg2
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
        
        // Edge case where arg1 = arg2
        arg1 = new Constant(5);
        arg2 = new Constant(5);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
        
        // Edge case where arg1 < arg2
        arg1 = new Constant(3);
        arg2 = new Constant(5);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        // Typical case
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }
    
    // Include more test cases for edge cases and exceptions as needed
    
}
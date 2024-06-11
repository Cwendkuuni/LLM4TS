import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical case: 5 >= 3
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
        
        // Edge case: 3 >= 3
        arg1 = new Constant(3);
        arg2 = new Constant(3);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
        
        // Edge case: -10 >= -20
        arg1 = new Constant(-10);
        arg2 = new Constant(-20);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
        
        // Error scenario: invalid input
        arg1 = new Constant("invalid");
        arg2 = new Constant(5);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol() {
        // Typical case
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(">=", operation.getSymbol());
        
        // Edge case
        arg1 = new Constant(3);
        arg2 = new Constant(3);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(">=", operation.getSymbol());
    }
}
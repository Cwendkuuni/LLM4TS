import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testComputeValue() {
        // Test case 1
        EvalContext context1 = new EvalContext();
        CoreOperationGreaterThanOrEqual operation1 = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Object result1 = operation1.computeValue(context1);
        assertEquals(true, result1);
        
        // Test case 2
        EvalContext context2 = new EvalContext();
        CoreOperationGreaterThanOrEqual operation2 = new CoreOperationGreaterThanOrEqual(arg3, arg4);
        Object result2 = operation2.computeValue(context2);
        assertEquals(false, result2);
        
        // Test case 3
        EvalContext context3 = new EvalContext();
        CoreOperationGreaterThanOrEqual operation3 = new CoreOperationGreaterThanOrEqual(arg5, arg6);
        Object result3 = operation3.computeValue(context3);
        assertEquals(true, result3);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }
}
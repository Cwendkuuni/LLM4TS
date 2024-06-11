import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(null, null);
        
        // Test case 1
        double arg1 = 1.5;
        double arg2 = 2.5;
        EvalContext context1 = new EvalContext();
        Object result1 = operation.computeValue(context1);
        assertEquals(false, result1);
        
        // Test case 2
        double arg3 = 2.5;
        double arg4 = 1.5;
        EvalContext context2 = new EvalContext();
        Object result2 = operation.computeValue(context2);
        assertEquals(true, result2);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(null, null);
        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }
}
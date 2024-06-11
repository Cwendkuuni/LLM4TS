import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression("arg1"), new Expression("arg2"));
        
        // Test case 1: arg1 > arg2
        EvalContext context1 = new EvalContext();
        double arg1 = 10.5;
        double arg2 = 5.2;
        context1.setVariable("arg1", arg1);
        context1.setVariable("arg2", arg2);
        Object result1 = operation.computeValue(context1);
        assertTrue((Boolean) result1);

        // Test case 2: arg1 < arg2
        EvalContext context2 = new EvalContext();
        double arg3 = 2.5;
        double arg4 = 6.1;
        context2.setVariable("arg1", arg3);
        context2.setVariable("arg2", arg4);
        Object result2 = operation.computeValue(context2);
        assertFalse((Boolean) result2);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression("arg1"), new Expression("arg2"));
        String symbol = operation.getSymbol();
        assertEquals(">", symbol);
    }
    
}
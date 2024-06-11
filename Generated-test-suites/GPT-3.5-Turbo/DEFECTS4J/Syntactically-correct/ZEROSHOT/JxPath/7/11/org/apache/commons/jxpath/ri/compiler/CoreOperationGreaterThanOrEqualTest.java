import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        // Test case 1: l >= r
        EvalContext context1 = new EvalContext();
        arg1.computeValue(context1);
        arg2.computeValue(context1);
        Object result1 = operation.computeValue(context1);
        assertEquals(Boolean.TRUE, result1);
        
        // Test case 2: l < r
        EvalContext context2 = new EvalContext();
        arg1.computeValue(context2);
        arg2.computeValue(context2);
        Object result2 = operation.computeValue(context2);
        assertEquals(Boolean.FALSE, result2);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals(">=", symbol);
    }
}
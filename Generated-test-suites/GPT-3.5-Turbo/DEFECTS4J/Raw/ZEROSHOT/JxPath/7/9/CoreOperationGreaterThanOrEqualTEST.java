import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
    
    @Test
    public void testComputeValue_larger() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setContextNodeSet(contextNodeSet);
        
        Object result = op.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_equal() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setContextNodeSet(contextNodeSet);
        
        Object result = op.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_smaller() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setContextNodeSet(contextNodeSet);
        
        Object result = op.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual op = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        String symbol = op.getSymbol();
        
        assertEquals(">=", symbol);
    }
}
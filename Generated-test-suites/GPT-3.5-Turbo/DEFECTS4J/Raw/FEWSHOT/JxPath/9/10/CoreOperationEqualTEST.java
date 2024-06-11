import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationEqualTest {
    
    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOpEqual = new CoreOperationEqual(arg1, arg2);
        
        EvalContext context = new EvalContext();
        Object result = coreOpEqual.computeValue(context);
        
        assertTrue(result instanceof Boolean);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOpEqual = new CoreOperationEqual(arg1, arg2);
        
        String symbol = coreOpEqual.getSymbol();
        
        assertEquals("=", symbol);
    }
}
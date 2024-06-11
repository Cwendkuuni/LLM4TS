import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValueTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, null);
        EvalContext context = new EvalContext();
        
        Object result = coreOperationEqual.computeValue(context);
        
        assertTrue(result.equals(Boolean.TRUE));
    }
    
    @Test
    public void testComputeValueFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, null);
        EvalContext context = new EvalContext();
        
        Object result = coreOperationEqual.computeValue(context);
        
        assertFalse(result.equals(Boolean.FALSE));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, null);
        
        String symbol = coreOperationEqual.getSymbol();
        
        assertEquals("=", symbol);
    }
}
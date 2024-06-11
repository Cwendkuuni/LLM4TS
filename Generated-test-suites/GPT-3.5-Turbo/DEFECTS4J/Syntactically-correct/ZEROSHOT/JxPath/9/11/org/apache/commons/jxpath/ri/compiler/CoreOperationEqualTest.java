import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_equal() {
        CoreOperationEqual operation = new CoreOperationEqual(null, null);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean)result);
    }
    
    @Test
    public void testComputeValue_notEqual() {
        CoreOperationEqual operation = new CoreOperationEqual(null, null);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean)result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual operation = new CoreOperationEqual(null, null);
        
        String symbol = operation.getSymbol();
        
        assertNotNull(symbol);
        assertEquals("=", symbol);
    }
    
}
import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;


public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_equalReturnsTrue() {
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_equalReturnsFalse() {
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals("=", symbol);
    }
}
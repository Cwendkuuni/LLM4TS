import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new CoreOperationEqual(null, null);
        Expression arg2 = new CoreOperationEqual(null, null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        Object result = coreOperationEqual.computeValue(context);
        assertTrue((boolean) result);
        
        arg1 = new CoreOperationEqual(null, null);
        arg2 = new CoreOperationEqual(null, "test");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(context);
        assertFalse((boolean) result);
        
        arg1 = new CoreOperationEqual(5, 5);
        arg2 = new CoreOperationEqual(5.0, 5.0);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        result = coreOperationEqual.computeValue(context);
        assertTrue((boolean) result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new CoreOperationEqual(null, null);
        Expression arg2 = new CoreOperationEqual(null, null);
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
        
        arg1 = new CoreOperationEqual(null, null);
        arg2 = new CoreOperationEqual(null, "test");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
        
        arg1 = new CoreOperationEqual(5, 5);
        arg2 = new CoreOperationEqual(5.0, 5.0);
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}
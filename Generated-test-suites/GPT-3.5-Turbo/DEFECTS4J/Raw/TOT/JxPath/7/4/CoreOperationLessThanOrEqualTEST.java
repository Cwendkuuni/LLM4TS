import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual coreOperationLessThanOrEqual;

    @Before
    public void setUp() {
        coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(null, null);
    }
    
    @Test
    public void testComputeValue_lLessThanR_returnsTrue() {
        EvalContext context = new EvalContext(null);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new LiteralDouble(1.0), new LiteralDouble(2.0));
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_lEqualToR_returnsTrue() {
        EvalContext context = new EvalContext(null);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new LiteralDouble(1.0), new LiteralDouble(1.0));
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_lGreaterThanR_returnsFalse() {
        EvalContext context = new EvalContext(null);
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new LiteralDouble(2.0), new LiteralDouble(1.0));
        Object result = coreOperationLessThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test(expected = NullPointerException.class)
    public void testComputeValue_nullContext_throwsNullPointerException() {
        CoreOperationLessThanOrEqual coreOperationLessThanOrEqual = new CoreOperationLessThanOrEqual(new LiteralDouble(1.0), new LiteralDouble(2.0));
        coreOperationLessThanOrEqual.computeValue(null);
    }
    
    @Test
    public void testGetSymbol_returnsLessThanEqualToSymbol() {
        String symbol = coreOperationLessThanOrEqual.getSymbol();
        assertEquals("<=", symbol);
    }

}
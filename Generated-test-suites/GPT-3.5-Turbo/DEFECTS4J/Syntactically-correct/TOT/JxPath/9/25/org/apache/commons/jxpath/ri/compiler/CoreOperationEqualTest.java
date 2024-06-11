import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.EvalContext;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {
    
    private CoreOperationEqual coreOperationEqual;
    
    @Before
    public void setUp() {
        coreOperationEqual = new CoreOperationEqual(null, null);
    }
    
    @Test
    public void testComputeValueTrue() {
        // Typical use case where the arguments are equal
        EvalContext context = new EvalContext(null);
        coreOperationEqual = new CoreOperationEqual(new Expression("abc"), new Expression("abc"));
        Boolean result = (Boolean) coreOperationEqual.computeValue(context);
        assertTrue(result);
    }
    
    @Test
    public void testComputeValueFalse() {
        // Typical use case where the arguments are not equal
        EvalContext context = new EvalContext(null);
        coreOperationEqual = new CoreOperationEqual(new Expression(10), new Expression(20));
        Boolean result = (Boolean) coreOperationEqual.computeValue(context);
        assertFalse(result);
    }
    
    @Test
    public void testComputeValueNullArg() {
        // Error scenario where one of the arguments is null
        EvalContext context = new EvalContext(null);
        coreOperationEqual = new CoreOperationEqual(new Expression(null), new Expression("abc"));
        Boolean result = (Boolean) coreOperationEqual.computeValue(context);
        assertFalse(result);
    }
    
    @Test
    public void testComputeValueNullContext() {
        // Error scenario where the EvalContext is null
        coreOperationEqual = new CoreOperationEqual(new Expression("abc"), new Expression("abc"));
        try {
            coreOperationEqual.computeValue(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetSymbol() {
        // Typical use case to validate the getSymbol method
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}
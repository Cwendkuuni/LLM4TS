import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationEqualTest {
    
    private CoreOperationEqual coreOperationEqual;
    private EvalContext evalContext;
    
    @Before
    public void setup() {
        coreOperationEqual = new CoreOperationEqual(null, null);
        evalContext = new EvalContext();
    }
    
    @Test
    public void testComputeValue_True() {
        // Typical use case: both arguments are equal
        coreOperationEqual.setArgs(new Expression("arg1"), new Expression("arg1"));
        Object result = coreOperationEqual.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_False() {
        // Typical use case: both arguments are not equal
        coreOperationEqual.setArgs(new Expression("arg1"), new Expression("arg2"));
        Object result = coreOperationEqual.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_NullArguments() {
        // Edge case: both arguments are null
        coreOperationEqual.setArgs(new Expression(null), new Expression(null));
        Object result = coreOperationEqual.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_NullArgument() {
        // Edge case: one argument is null
        coreOperationEqual.setArgs(new Expression("arg1"), new Expression(null));
        Object result = coreOperationEqual.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_EmptyArguments() {
        // Edge case: both arguments are empty strings
        coreOperationEqual.setArgs(new Expression(""), new Expression(""));
        Object result = coreOperationEqual.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_EmptyArgument() {
        // Edge case: one argument is an empty string
        coreOperationEqual.setArgs(new Expression("arg1"), new Expression(""));
        Object result = coreOperationEqual.computeValue(evalContext);
        assertTrue(result instanceof Boolean);
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetSymbol() {
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
    
}
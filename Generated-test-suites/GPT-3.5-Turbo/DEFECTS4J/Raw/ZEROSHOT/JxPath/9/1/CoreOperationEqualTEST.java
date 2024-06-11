import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    private CoreOperationEqual operationEqual;
    private Expression arg1;
    private Expression arg2;
    
    @Before
    public void setUp() {
        arg1 = new Expression();
        arg2 = new Expression();
        operationEqual = new CoreOperationEqual(arg1, arg2);
    }
    
    @Test
    public void testComputeValue_equalArgs_returnsTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("args", new Object[]{5, 5});
        Object result = operationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_notEqualArgs_returnsFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("args", new Object[]{5, 6});
        Object result = operationEqual.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testGetSymbol_returnsEqualSymbol() {
        String symbol = operationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}
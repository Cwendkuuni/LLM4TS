import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    private CoreOperationNotEqual operation;

    @Before
    public void setUp() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        operation = new CoreOperationNotEqual(arg1, arg2);
    }

    @Test
    public void testComputeValue_equalValues() {
        EvalContext context = new EvalContext();
        context.set(args[0], "value");
        context.set(args[1], "value");
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_notEqualValues() {
        EvalContext context = new EvalContext();
        context.set(args[0], "value1");
        context.set(args[1], "value2");
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertNotNull(symbol);
        assertEquals("!=", symbol);
    }

}
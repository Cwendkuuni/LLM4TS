import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    CoreOperationEqual coreOperationEqual;

    @Before
    public void setUp() {
        coreOperationEqual = new CoreOperationEqual(new Expression(1), new Expression(1));
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        assertEquals("=", coreOperationEqual.getSymbol());
    }

    @Test
    public void testComputeValueDifferentArguments() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(1), new Expression(2));
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testObjectArguments() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression("test"), new Expression("test"));
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testNullArguments() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(null), new Expression(null));
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue((Boolean) result);
    }
}
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {
    private CoreOperationEqual coreOperationEqual;

    @Before
    public void setUp() {
        coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
    }

    @Test
    public void testComputeValueEqual() {
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueNotEqual() {
        EvalContext context = new EvalContext();
        coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}
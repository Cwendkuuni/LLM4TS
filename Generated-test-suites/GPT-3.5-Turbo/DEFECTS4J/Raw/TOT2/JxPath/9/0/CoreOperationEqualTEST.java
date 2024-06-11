import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_equalValues_returnTrue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Object(), new Object());
        EvalContext context = new EvalContext();
        boolean result = coreOperationEqual.computeValue(context);
        assertTrue(result);
    }

    @Test
    public void testGetSymbol_returnEqualSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Object(), new Object());
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }

    @Test
    public void testComputeValue_notEqualValues_returnFalse() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Object(), new Object());
        EvalContext context = new EvalContext();
        context.setContextPosition(5);
        boolean result = coreOperationEqual.computeValue(context);
        assertFalse(result);
    }

}
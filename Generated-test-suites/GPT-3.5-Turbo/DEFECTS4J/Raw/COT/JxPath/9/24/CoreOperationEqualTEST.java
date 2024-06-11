
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        // Test for equal values
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertTrue((Boolean) result);

        // Test for not equal values
        CoreOperationEqual coreOperationEqual2 = new CoreOperationEqual(new Expression(), new Expression());
        EvalContext context2 = new EvalContext();
        Object result2 = coreOperationEqual2.computeValue(context2);
        assertFalse((Boolean) result2);
    }

    @Test
    public void testGetSymbol() {
        // Test the symbol
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}


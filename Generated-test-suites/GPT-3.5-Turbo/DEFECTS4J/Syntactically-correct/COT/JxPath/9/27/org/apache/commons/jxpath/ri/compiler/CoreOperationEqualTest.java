import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, null);
        EvalContext context = null;

        // Test case 1: args[0] = null, args[1] = null
        context = new EvalContext();
        boolean result1 = coreOperationEqual.computeValue(context);
        assertTrue(result1);

        // Test case 2: args[0] = "Hello", args[1] = "Hello"
        context = new EvalContext();
        coreOperationEqual = new CoreOperationEqual(new Expression("Hello"), new Expression("Hello"));
        boolean result2 = coreOperationEqual.computeValue(context);
        assertTrue(result2);

        // Test case 3: args[0] = null, args[1] = "Hello"
        context = new EvalContext();
        coreOperationEqual = new CoreOperationEqual(null, new Expression("Hello"));
        boolean result3 = coreOperationEqual.computeValue(context);
        assertFalse(result3);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, null);
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }
}
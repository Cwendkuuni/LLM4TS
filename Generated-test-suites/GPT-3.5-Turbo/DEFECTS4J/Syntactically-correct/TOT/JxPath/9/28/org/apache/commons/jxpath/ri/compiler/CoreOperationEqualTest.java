import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    // Test case for computeValue(EvalContext context) method
    @Test
    public void testComputeValue() {
        // Typical use case: equal values
        Expression arg1 = new Expression("value");
        Expression arg2 = new Expression("value");
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(null)); // null EvalContext

        // Typical use case: unequal values
        arg1 = new Expression("value1");
        arg2 = new Expression("value2");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(null)); // null EvalContext

        // Edge case: empty values
        arg1 = new Expression("");
        arg2 = new Expression("");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, coreOperationEqual.computeValue(null)); // null EvalContext

        // Edge case: mixed types
        arg1 = new Expression("123"); // Integer value
        arg2 = new Expression("123.0"); // Double value
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, coreOperationEqual.computeValue(null)); // null EvalContext

        // Error scenario: null argument
        arg1 = null;
        arg2 = new Expression("value2");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertNull(coreOperationEqual.computeValue(null)); // null EvalContext

        // Error scenario: null EvalContext
        arg1 = new Expression("value1");
        arg2 = new Expression("value2");
        coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        assertNull(coreOperationEqual.computeValue(null)); // null EvalContext
    }

    // Test case for getSymbol() method
    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(null, null);
        assertEquals("=", coreOperationEqual.getSymbol());
    }
}
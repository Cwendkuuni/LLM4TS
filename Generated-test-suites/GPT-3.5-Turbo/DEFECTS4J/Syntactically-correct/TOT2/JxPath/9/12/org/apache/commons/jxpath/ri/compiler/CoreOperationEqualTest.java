import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        // Typical use case
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("=", symbol);
    }

    @Test
    public void testComputeValueEdgeCases() {
        // Edge case: args are equal
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setContextNodePointer(new NodePointer(new Object()));
        Object result = coreOperationEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueExceptionHandling() {
        // Exception handling: Null context
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(), new Expression());
        try {
            coreOperationEqual.computeValue(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}
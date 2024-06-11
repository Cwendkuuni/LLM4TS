import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case: Two different objects
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Edge case: Two identical objects
        CoreOperationNotEqual operation2 = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context2 = new EvalContext();
        assertFalse(operation2.computeValue(context2));

        // Error scenario: One null object
        CoreOperationNotEqual operation3 = new CoreOperationNotEqual(new Expression(), null);
        EvalContext context3 = new EvalContext();
        assertTrue(operation3.computeValue(context3));

        // Error scenario: Both null objects
        CoreOperationNotEqual operation4 = new CoreOperationNotEqual(null, null);
        EvalContext context4 = new EvalContext();
        assertFalse(operation4.computeValue(context4));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        assertEquals("!=", operation.getSymbol());
    }
}
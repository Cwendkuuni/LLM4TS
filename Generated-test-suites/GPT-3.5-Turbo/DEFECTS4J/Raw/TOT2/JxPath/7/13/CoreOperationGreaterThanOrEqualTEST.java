import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);

        // Edge case
        operation = new CoreOperationGreaterThanOrEqual(new Constant(3.5), new Constant(3.5));
        result = operation.computeValue(context);
        assertTrue((Boolean) result);

        // Error scenario
        try {
            operation = new CoreOperationGreaterThanOrEqual(new Constant("abc"), new Constant(5));
            result = operation.computeValue(context);
            fail("Expected NumberFormatException");
        } catch (NumberFormatException e) {
            // Expected
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(3));
        assertEquals(">=", operation.getSymbol());
    }

}
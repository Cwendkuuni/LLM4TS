import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_GreaterThanOrEqual_ReturnsTrue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual();
        EvalContext context = new EvalContext();

        // Test for a case where the left value is greater than the right value
        Object result = operation.computeValue(context, 10, 5);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_NotGreaterThanOrEqual_ReturnsFalse() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual();
        EvalContext context = new EvalContext();

        // Test for a case where the left value is less than the right value
        Object result = operation.computeValue(context, 5, 10);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol_ReturnsGreaterThanOrEqualSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual();
        String symbol = operation.getSymbol();

        assertEquals(">=", symbol);
    }
}
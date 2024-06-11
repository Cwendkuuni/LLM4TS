import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        // Typical
        CoreOperationEqual core = new CoreOperationEqual(new Expression(), new Expression());
        Object result = core.computeValue(new EvalContext());
        assertFalse((Boolean) result); // Should return false in typical scenario

        // Edge Cases
        // Edge case: null values
        core = new CoreOperationEqual(null, null);
        result = core.computeValue(new EvalContext());
        assertTrue((Boolean) result); // Should return true when both arguments are null

        // Error Scenarios
        // Error scenario: invalid arguments
        core = new CoreOperationEqual(new Expression(), null);
        result = core.computeValue(new EvalContext());
        assertFalse((Boolean) result); // Should return false when one argument is null
    }

    @Test
    public void testGetSymbol() {
        // Typical
        CoreOperationEqual core = new CoreOperationEqual(new Expression(), new Expression());
        assertEquals("=", core.getSymbol()); // Should return "="
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case: when the left value is greater than the right value
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(5), new Expression(3));
        boolean result = greaterThanOrEqual.computeValue(context);
        assertTrue(result);

        // Typical use case: when the left value is equal to the right value
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(5), new Expression(5));
        boolean result = greaterThanOrEqual.computeValue(context);
        assertTrue(result);

        // Typical use case: when the left value is less than the right value
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(3), new Expression(5));
        boolean result = greaterThanOrEqual.computeValue(context);
        assertFalse(result);

        // Edge case: when the left value is double precision positive infinity
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(Double.POSITIVE_INFINITY), new Expression(5));
        boolean result = greaterThanOrEqual.computeValue(context);
        assertTrue(result);

        // Edge case: when the left value is double precision negative infinity
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(Double.NEGATIVE_INFINITY), new Expression(5));
        boolean result = greaterThanOrEqual.computeValue(context);
        assertFalse(result);

        // Edge case: when the left value is NaN
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(Double.NaN), new Expression(5));
        boolean result = greaterThanOrEqual.computeValue(context);
        assertFalse(result);

        // Error scenario: when the left value is null
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(null), new Expression(5));
        try {
            boolean result = greaterThanOrEqual.computeValue(context);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Passed, exception thrown as expected
        }

        // Error scenario: when the right value is null
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(5), new Expression(null));
        try {
            boolean result = greaterThanOrEqual.computeValue(context);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Passed, exception thrown as expected
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Expression(5), new Expression(3));
        String result = greaterThanOrEqual.getSymbol();
        assertEquals(">=", result);
    }
}
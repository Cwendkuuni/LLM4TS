import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // typical use case
        Expression arg1 = new Expression("2");
        Expression arg2 = new Expression("1");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // edge case
        arg1 = new Expression("0");
        arg2 = new Expression("0");
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // error scenario
        arg1 = new Expression("a");
        arg2 = new Expression("0");
        try {
            operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
            operation.computeValue(context);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid argument: a", e.getMessage());
        }
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression("2");
        Expression arg2 = new Expression("1");
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(">=", operation.getSymbol());
    }
}
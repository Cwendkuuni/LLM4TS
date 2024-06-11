import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {

        EvalContext context = new EvalContext();

        // Test typical use cases
        CoreOperationGreaterThan expr1 = new CoreOperationGreaterThan(new Expression(5), new Expression(3));
        assertEquals(Boolean.TRUE, expr1.computeValue(context));

        CoreOperationGreaterThan expr2 = new CoreOperationGreaterThan(new Expression(0), new Expression(-1));
        assertEquals(Boolean.TRUE, expr2.computeValue(context));

        // Test edge cases
        CoreOperationGreaterThan expr3 = new CoreOperationGreaterThan(new Expression(Double.MAX_VALUE), new Expression(Double.MIN_VALUE));
        assertEquals(Boolean.TRUE, expr3.computeValue(context));

        CoreOperationGreaterThan expr4 = new CoreOperationGreaterThan(new Expression(Double.MIN_VALUE), new Expression(Double.MAX_VALUE));
        assertEquals(Boolean.FALSE, expr4.computeValue(context));

        // Test error scenarios
        try {
            CoreOperationGreaterThan expr5 = new CoreOperationGreaterThan(new Expression("10"), new Expression(5));
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("Expected type: Number got value of type: java.lang.String", e.getMessage());
        }

        try {
            CoreOperationGreaterThan expr6 = new CoreOperationGreaterThan(new Expression(null), new Expression(5));
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            assertNull(e.getMessage());
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expr = new CoreOperationGreaterThan(new Expression(5), new Expression(3));
        assertEquals(">", expr.getSymbol());
    }
}
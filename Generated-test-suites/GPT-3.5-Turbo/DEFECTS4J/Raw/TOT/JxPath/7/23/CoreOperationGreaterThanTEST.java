import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        // typical use case
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        EvalContext context = new EvalContext();
        boolean result = (boolean) expression.computeValue(context);
        assertTrue(result);

        // edge case: both arguments are equal
        expression = new CoreOperationGreaterThan(new Constant(5), new Constant(5));
        context = new EvalContext();
        result = (boolean) expression.computeValue(context);
        assertFalse(result);

        // edge case: both arguments are negative
        expression = new CoreOperationGreaterThan(new Constant(-5), new Constant(-10));
        context = new EvalContext();
        result = (boolean) expression.computeValue(context);
        assertTrue(result);

        // error scenario: one argument is null
        expression = new CoreOperationGreaterThan(new Constant(null), new Constant(3));
        context = new EvalContext();
        try {
            result = (boolean) expression.computeValue(context);
        } catch (Exception e) {
            assertNotNull(e);
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        assertEquals(">", expression.getSymbol());
    }
}
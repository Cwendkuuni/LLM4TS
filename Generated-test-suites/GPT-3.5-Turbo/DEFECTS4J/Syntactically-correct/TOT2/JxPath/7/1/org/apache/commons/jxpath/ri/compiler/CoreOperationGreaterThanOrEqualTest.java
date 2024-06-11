import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Edge case where both arguments are equal
        arg1 = new Constant(3);
        arg2 = new Constant(3);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Edge case where both arguments are negative
        arg1 = new Constant(-5);
        arg2 = new Constant(-3);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        // Typical use case
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(">=", operation.getSymbol());
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgumentConstructor() {
        new CoreOperationGreaterThanOrEqual(null, null);
    }

    @Test
    public void testNullEvalContext() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertNull(operation.computeValue(null));
    }

}
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        Expression arg1 = new Expression(5);
        Expression arg2 = new Expression(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        // Edge case: both arguments are equal
        arg1 = new Expression(7);
        arg2 = new Expression(7);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        // Edge case: argument1 is greater than argument2
        arg1 = new Expression(10);
        arg2 = new Expression(9);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue(((Boolean) result).booleanValue());

        // Error scenario: argument1 is null
        arg1 = null;
        arg2 = new Expression(8);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        assertNull(result);

        // Error scenario: argument2 is null
        arg1 = new Expression(6);
        arg2 = null;
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression(5);
        Expression arg2 = new Expression(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(">=", operation.getSymbol());
    }
}
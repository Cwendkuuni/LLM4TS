import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    CoreOperationGreaterThanOrEqual core;

    @Test
    public void testComputeValue() {
        Expression arg1 = new LiteralExpression(5);
        Expression arg2 = new LiteralExpression(3);
        core = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue((Boolean)core.computeValue(context));

        Expression arg3 = new LiteralExpression(2);
        Expression arg4 = new LiteralExpression(2);
        core = new CoreOperationGreaterThanOrEqual(arg3, arg4);
        assertFalse((Boolean)core.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new LiteralExpression(5);
        Expression arg2 = new LiteralExpression(3);
        core = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        assertEquals(">=", core.getSymbol());
    }

    @Test
    public void testEdgeCasesAndExceptions() {
        // Test edge case where both arguments are equal
        Expression arg1 = new LiteralExpression(5);
        Expression arg2 = new LiteralExpression(5);
        core = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        assertTrue((Boolean)core.computeValue(context));

        // Test edge case where left argument is Double.POSITIVE_INFINITY
        Expression arg3 = new LiteralExpression(Double.POSITIVE_INFINITY);
        Expression arg4 = new LiteralExpression(5);
        core = new CoreOperationGreaterThanOrEqual(arg3, arg4);
        assertFalse((Boolean)core.computeValue(context));

        // Test edge case where left argument is Double.NaN
        Expression arg5 = new LiteralExpression(Double.NaN);
        Expression arg6 = new LiteralExpression(5);
        core = new CoreOperationGreaterThanOrEqual(arg5, arg6);
        assertFalse((Boolean)core.computeValue(context));

        // Test exception handling with null context
        Expression arg7 = new LiteralExpression(5);
        Expression arg8 = new LiteralExpression(3);
        core = new CoreOperationGreaterThanOrEqual(arg7, arg8);
        assertThrows(NullPointerException.class, () -> core.computeValue(null));
    }

}
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical Use Case
        Expression arg1 = new Expression("10");
        Expression arg2 = new Expression("5");
        CoreOperationGreaterThanOrEqual exp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = exp.computeValue(context);
        assertTrue((Boolean) result);

        // Edge Case
        arg1 = new Expression("-5");
        arg2 = new Expression("-5");
        exp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = exp.computeValue(context);
        assertTrue((Boolean) result);

        // Error Scenario
        arg1 = new Expression("abc");
        arg2 = new Expression("123");
        exp = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        result = exp.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual exp = new CoreOperationGreaterThanOrEqual(null, null);
        assertEquals(">=", exp.getSymbol());
    }
}
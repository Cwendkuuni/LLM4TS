import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // Typical case
        EvalContext context = new EvalContext();
        Object result = greaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Edge case
        arg1 = new Constant(3.5);
        arg2 = new Constant(3.5);
        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = greaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Error scenario
        arg1 = new Constant("not a number");
        arg2 = new Constant(5);
        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = greaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant("5");
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        // Typical case
        String symbol = greaterThanOrEqual.getSymbol();
        assertEquals(">=", symbol);

        // Edge case
        arg1 = new Constant(3.6);
        arg2 = new Constant(3.6);
        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        symbol = greaterThanOrEqual.getSymbol();
        assertEquals(">=", symbol);

        // Error scenario - No edge/exception case for this method
    }
}
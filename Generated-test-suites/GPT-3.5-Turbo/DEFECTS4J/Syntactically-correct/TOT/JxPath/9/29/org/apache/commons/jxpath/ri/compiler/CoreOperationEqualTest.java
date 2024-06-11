import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setVariable("arg1", arg1);
        context.setVariable("arg2", arg2);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Edge case
        arg1 = new Expression();
        arg2 = new Expression();
        operation = new CoreOperationEqual(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Error scenario
        // Object types that do not implement equality checks
        arg1 = new Expression();
        arg2 = new Expression();
        operation = new CoreOperationEqual(arg1, arg2);
        context = new EvalContext();
        result = operation.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals("=", symbol);
    }
}
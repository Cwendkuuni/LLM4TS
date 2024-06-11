import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case: Both arguments are equal
        CoreOperationEqual operationEqual = new CoreOperationEqual(new Expression("arg1"), new Expression("arg1"));
        EvalContext context = new EvalContext();
        assertTrue(operationEqual.computeValue(context));

        // Typical use case: Both arguments are not equal
        operationEqual = new CoreOperationEqual(new Expression("arg1"), new Expression("arg2"));
        context = new EvalContext();
        assertFalse(operationEqual.computeValue(context));

        // Edge case: First argument is null, second argument is not null
        operationEqual = new CoreOperationEqual(new Expression(null), new Expression("arg1"));
        context = new EvalContext();
        assertFalse(operationEqual.computeValue(context));

        // Edge case: First argument is not null, second argument is null
        operationEqual = new CoreOperationEqual(new Expression("arg1"), new Expression(null));
        context = new EvalContext();
        assertFalse(operationEqual.computeValue(context));

        // Edge case: Both arguments are null
        operationEqual = new CoreOperationEqual(new Expression(null), new Expression(null));
        context = new EvalContext();
        assertTrue(operationEqual.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        // Typical use case: Get the symbol of the operation
        CoreOperationEqual operationEqual = new CoreOperationEqual(new Expression("arg1"), new Expression("arg1"));
        assertEquals("=", operationEqual.getSymbol());
    }
}
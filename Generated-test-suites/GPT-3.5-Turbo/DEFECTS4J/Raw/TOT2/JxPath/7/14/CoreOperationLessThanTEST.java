import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new Variable();
        Expression arg2 = new Variable();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        assertNotNull(operation);
        assertEquals("<", operation.getSymbol());
    }

    @Test
    public void testComputeValue() {
        // Typical use case
        EvalContext context = new EvalContext();
        context.setVariableValue("x", 5);
        context.setVariableValue("y", 10);

        Expression arg1 = new VariableReference(null, "x");
        Expression arg2 = new VariableReference(null, "y");

        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        Object result = operation.computeValue(context);

        assertTrue((Boolean) result);

        // Edge case
        context.setVariableValue("x", 10);

        result = operation.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgs() {
        CoreOperationLessThan operation = new CoreOperationLessThan(null, null);
        operation.computeValue(new EvalContext());
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext(new JXPathContext(), null);
        assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Edge case
        Expression arg3 = new Constant(3.5);
        CoreOperationGreaterThan operation2 = new CoreOperationGreaterThan(arg1, arg3);
        assertEquals(Boolean.TRUE, operation2.computeValue(context));

        // Error scenario
        Expression arg4 = new Constant("a"); // Invalid value
        CoreOperationGreaterThan operation3 = new CoreOperationGreaterThan(arg1, arg4);
        assertEquals(Boolean.FALSE, operation3.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(">", operation.getSymbol());
    }
}
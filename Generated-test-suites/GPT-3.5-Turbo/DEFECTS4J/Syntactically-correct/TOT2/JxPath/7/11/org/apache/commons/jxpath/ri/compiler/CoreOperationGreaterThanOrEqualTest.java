import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_PositiveCase() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_NegativeCase() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(3), new Constant(5));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(3));
        assertEquals(">=", operation.getSymbol());
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullArguments() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        operation.computeValue(new EvalContext());
    }

}
import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValueLessThan() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValueNotLessThan() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(10), new Constant(5));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        assertEquals("<", operation.getSymbol());
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        CoreOperationLessThan operation = new CoreOperationLessThan(null, null);
        operation.computeValue(new EvalContext());
    }
}
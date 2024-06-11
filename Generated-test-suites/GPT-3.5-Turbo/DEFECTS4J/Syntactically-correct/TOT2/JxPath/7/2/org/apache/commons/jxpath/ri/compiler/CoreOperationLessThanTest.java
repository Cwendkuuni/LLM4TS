import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue_NormalValues() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext(new Object());
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_EqualValues() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(5));
        EvalContext context = new EvalContext(new Object());
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(null, null);
        assertEquals("<", operation.getSymbol());
    }

    @Test
    public void testComputeValue_NullValues() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(null), new Constant(10));
        EvalContext context = new EvalContext(new Object());
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValue_NullContext() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(10));
        operation.computeValue(null);
    }
}
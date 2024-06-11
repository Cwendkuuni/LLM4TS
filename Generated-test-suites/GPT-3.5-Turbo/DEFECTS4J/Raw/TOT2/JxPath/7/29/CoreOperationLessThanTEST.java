import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue_LessThan() {
        CoreOperationLessThan operationLessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operationLessThan.computeValue(context));
    }

    @Test
    public void testComputeValue_NotLessThan() {
        CoreOperationLessThan operationLessThan = new CoreOperationLessThan(new Constant(10), new Constant(5));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, operationLessThan.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operationLessThan = new CoreOperationLessThan(new Constant(5), new Constant(10));
        assertEquals("<", operationLessThan.getSymbol());
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        CoreOperationLessThan operationLessThan = new CoreOperationLessThan(null, null);
        operationLessThan.computeValue(new EvalContext());
    }
}
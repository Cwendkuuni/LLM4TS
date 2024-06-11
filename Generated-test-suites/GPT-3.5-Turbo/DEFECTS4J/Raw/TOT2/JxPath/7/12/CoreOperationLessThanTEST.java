import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(10), new Constant(20));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(10), new Constant(20));
        assertEquals("<", operation.getSymbol());
    }

    @Test
    public void testComputeValueEdgeCase1() {
        // Testing with negative numbers
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(-5), new Constant(-3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueEdgeCase2() {
        // Testing with equal numbers
        CoreOperationLessThan operation = new CoreOperationLessThan(new Constant(5), new Constant(5));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueNullValues() {
        CoreOperationLessThan operation = new CoreOperationLessThan(null, null);
        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }
}
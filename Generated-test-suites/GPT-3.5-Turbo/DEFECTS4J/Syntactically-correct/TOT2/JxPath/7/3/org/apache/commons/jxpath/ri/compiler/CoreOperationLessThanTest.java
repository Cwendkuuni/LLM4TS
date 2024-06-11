import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Literal(5), new Literal(10));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Literal(5), new Literal(10));
        assertEquals("<", operation.getSymbol());
    }

    // Edge cases and exception handling scenarios
    @Test
    public void testComputeValueWithZero() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Literal(0), new Literal(0));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test(expected = ArithmeticException.class)
    public void testComputeValueWithNaN() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Literal(Double.NaN), new Literal(10));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
    }
}
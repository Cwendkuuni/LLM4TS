import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression("2"), new Expression("5"));
        EvalContext context = new EvalContext(null);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression("2"), new Expression("5"));
        assertEquals("<", operation.getSymbol());
    }

    @Test
    public void testComputeValueWithEqualNumbers() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression("3"), new Expression("3"));
        EvalContext context = new EvalContext(null);
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullPointerException() {
        CoreOperationLessThan operation = new CoreOperationLessThan(null, null);
        EvalContext context = new EvalContext(null);
        operation.computeValue(context);
    }
}
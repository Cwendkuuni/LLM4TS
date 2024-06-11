import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(2), new Constant(3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result); // 2 <= 3 is true
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(5));
        assertEquals("<=", operation.getSymbol());
    }

    // Edge Cases

    @Test
    public void testComputeValueEqualValues() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(4), new Constant(4));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result); // 4 <= 4 is true
    }

    @Test
    public void testComputeValueNegativeValues() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(-2), new Constant(-3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result); // -2 <= -3 is false
    }

    // Error Scenarios

    @Test(expected = NullPointerException.class)
    public void testComputeValueNullArgument() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(null, new Constant(3));
        EvalContext context = new EvalContext();
        operation.computeValue(context); // should throw NullPointerException
    }

}
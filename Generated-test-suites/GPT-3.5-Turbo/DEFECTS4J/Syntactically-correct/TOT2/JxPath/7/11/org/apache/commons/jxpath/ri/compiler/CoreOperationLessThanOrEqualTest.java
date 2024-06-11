import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5); // typical case
        Expression arg2 = new Constant(10); // typical case
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5); // typical case
        Expression arg2 = new Constant(10); // typical case
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        assertEquals("<=", operation.getSymbol());
    }

    @Test
    public void testComputeValueNegativeCase() {
        Expression arg1 = new Constant(15); // edge case
        Expression arg2 = new Constant(10); // edge case
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueNullArguments() {
        Expression arg1 = new Constant(null); // error scenario
        Expression arg2 = new Constant(null); // error scenario
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    // Additional edge cases and error scenarios can be added here

}
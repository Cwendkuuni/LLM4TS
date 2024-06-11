import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueEqualValues() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(5);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueNegativeValues() {
        Expression arg1 = new Constant(-3);
        Expression arg2 = new Constant(-5);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueNullValues() {
        Expression arg1 = new Constant(null);
        Expression arg2 = new Constant(1);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        try {
            operation.computeValue(context);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(">", operation.getSymbol());
    }
}
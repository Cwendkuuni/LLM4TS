import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new Constant(5), new Constant(3));
        EvalContext context = new EvalContext(new Object());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new Constant(5), new Constant(5));
        assertEquals(">=", operation.getSymbol());
    }

    @Test
    public void testEdgeCases() {
        // Testing edge case where both arguments are equal
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new Constant(0), new Constant(0));
        EvalContext context = new EvalContext(new Object());
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
        
        // Testing edge case where left argument is smaller than right
        operation = new CoreOperationGreaterThanOrEqual(
                new Constant(-1), new Constant(1));
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                null, new Constant(3));
        operation.computeValue(new EvalContext(new Object()));
    }

}
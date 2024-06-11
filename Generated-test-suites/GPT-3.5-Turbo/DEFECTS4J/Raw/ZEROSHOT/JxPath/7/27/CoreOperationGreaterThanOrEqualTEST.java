import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_LeftGreaterThanRight_ReturnsTrue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_LeftEqualToRight_ReturnsTrue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(5));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_LeftLessThanRight_ReturnsFalse() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(3), new Constant(5));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol_ReturnsGreaterThanEqual() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(3));
        String symbol = operation.getSymbol();
        assertEquals(">=", symbol);
    }
}
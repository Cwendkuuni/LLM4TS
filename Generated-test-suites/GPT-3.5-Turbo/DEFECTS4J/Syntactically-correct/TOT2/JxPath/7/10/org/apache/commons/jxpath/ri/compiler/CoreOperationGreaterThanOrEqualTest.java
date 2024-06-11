import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(3));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(3));
        assertEquals(">=", operation.getSymbol());
    }

    @Test
    public void testComputeValueEdgeCases() {
        // Testing edge case with equal values
        CoreOperationGreaterThanOrEqual operation1 = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(5));
        EvalContext context1 = new EvalContext();
        assertEquals(Boolean.TRUE, operation1.computeValue(context1));

        // Testing edge case with negative values
        CoreOperationGreaterThanOrEqual operation2 = new CoreOperationGreaterThanOrEqual(new Constant(-5), new Constant(-10));
        EvalContext context2 = new EvalContext();
        assertEquals(Boolean.TRUE, operation2.computeValue(context2));
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValueNullArguments() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        operation.computeValue(new EvalContext());
    }
}
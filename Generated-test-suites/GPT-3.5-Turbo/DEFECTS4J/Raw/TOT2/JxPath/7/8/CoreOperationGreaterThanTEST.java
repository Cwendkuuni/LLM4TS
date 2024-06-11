import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        assertEquals(">", operation.getSymbol());
    }

    @Test
    public void testComputeValueEqual() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Constant(3), new Constant(3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testEdgeCases() {
        // Test with null argument
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, new Constant(3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
        
        // Test with negative values
        operation = new CoreOperationGreaterThan(new Constant(-5), new Constant(-3));
        context = new EvalContext();
        result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test(expected = NullPointerException.class)
    public void testNullArguments() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, null);
        EvalContext context = new EvalContext();
        operation.computeValue(context);
    }
}
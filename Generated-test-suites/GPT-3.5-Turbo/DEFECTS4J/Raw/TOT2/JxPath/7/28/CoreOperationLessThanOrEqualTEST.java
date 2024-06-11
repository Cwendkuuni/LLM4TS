import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(10));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        operation = new CoreOperationLessThanOrEqual(new Constant(10), new Constant(5));
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(5), new Constant(10));
        assertEquals("<=", operation.getSymbol());
    }

    // Edge cases and exception handling scenarios
    @Test
    public void testComputeValueEdgeCase() {
        // Testing with negative numbers
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Constant(-5), new Constant(-10));
        EvalContext context = new EvalContext();
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // Testing with zero values
        operation = new CoreOperationLessThanOrEqual(new Constant(0), new Constant(0));
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Testing with null arguments
        operation = new CoreOperationLessThanOrEqual(null, new Constant(10));
        assertNull(operation.computeValue(context));
    }
}
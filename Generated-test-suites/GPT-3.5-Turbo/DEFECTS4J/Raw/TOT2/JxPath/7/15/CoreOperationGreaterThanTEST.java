import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.TRUE, result);
        
        operation = new CoreOperationGreaterThan(new Constant(2), new Constant(5));
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        assertEquals(">", operation.getSymbol());

        operation = new CoreOperationGreaterThan(new Constant(2), new Constant(5));
        assertEquals(">", operation.getSymbol());
    }
    
    // Additional test cases for edge cases
    @Test
    public void testComputeValueEdgeCases() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Constant(0), new Constant(0));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
        
        operation = new CoreOperationGreaterThan(new Constant(-5), new Constant(5));
        result = operation.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }
    
    // Test exception handling
    @Test(expected = NullPointerException.class)
    public void testNullValues() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(null, null);
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
    }
}
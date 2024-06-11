import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationLessThanTest {

    private CoreOperationLessThan operation;

    @Before
    public void setUp() {
        operation = new CoreOperationLessThan(new Expression(), new Expression());
    }

    // Test computeValue method
    @Test
    public void testComputeValue() {
        // Typical use cases
        // Test when both arguments are integers and the first argument is less than the second argument
        assertEquals(Boolean.TRUE, operation.computeValue(createEvalContext(1, 2)));
        // Test when both arguments are integers and the first argument is greater than or equal to the second argument
        assertEquals(Boolean.FALSE, operation.computeValue(createEvalContext(2, 2)));
        
        // Test when both arguments are doubles and the first argument is less than the second argument
        assertEquals(Boolean.TRUE, operation.computeValue(createEvalContext(1.5, 2.5)));
        // Test when both arguments are doubles and the first argument is greater than or equal to the second argument
        assertEquals(Boolean.FALSE, operation.computeValue(createEvalContext(2.5, 2.5)));

        // Edge cases
        // Test when both arguments are Integer.MAX_VALUE and the first argument is less than the second argument
        assertEquals(Boolean.TRUE, operation.computeValue(createEvalContext(Integer.MAX_VALUE, Integer.MAX_VALUE + 1)));
        // Test when both arguments are Integer.MAX_VALUE and the first argument is greater than or equal to the second argument
        assertEquals(Boolean.FALSE, operation.computeValue(createEvalContext(Integer.MAX_VALUE, Integer.MAX_VALUE)));
        
        // Test when the first argument is Double.MAX_VALUE and the second argument is Double.POSITIVE_INFINITY
        // Result should be False; Double.MAX_VALUE is less than Double.POSITIVE_INFINITY
        assertEquals(Boolean.FALSE, operation.computeValue(createEvalContext(Double.MAX_VALUE, Double.POSITIVE_INFINITY)));

        // Error scenarios
        // Test when the first argument is null
        // Since null cannot be cast to a double, an exception should be thrown
        try {
            operation.computeValue(createEvalContext(null, 2.5));
            fail("Expected exception not thrown");
        } catch (Exception e) {
            // Pass
        }
        
        // Test when the second argument is null
        // Since null cannot be cast to a double, an exception should be thrown
        try {
            operation.computeValue(createEvalContext(1.5, null));
            fail("Expected exception not thrown");
        } catch (Exception e) {
            // Pass
        }
    }

    // Test getSymbol method
    @Test
    public void testGetSymbol() {
        assertEquals("<", operation.getSymbol());
    }

    // Helper method to create an instance of EvalContext for testing
    private EvalContext createEvalContext(Object arg1, Object arg2) {
        EvalContext context = new EvalContext();
        context.setNodeSet(new Object[] {});
        context.setExpressionStack(new Stack());
        context.setVariables(new HashMap());
        context.getVariables().put("arg1", arg1);
        context.getVariables().put("arg2", arg2);
        return context;
    }
}
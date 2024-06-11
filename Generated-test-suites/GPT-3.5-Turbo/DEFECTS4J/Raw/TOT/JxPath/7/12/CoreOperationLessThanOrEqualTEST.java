import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Test typical use cases
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        // Test cases where arg1 <= arg2
        assertEquals(Boolean.TRUE, operation.computeValue(contextWithValue(arg1, 5), contextWithValue(arg2, 10))); // arg1 < arg2, expected result: true
        assertEquals(Boolean.TRUE, operation.computeValue(contextWithValue(arg1, 5), contextWithValue(arg2, 5))); // arg1 = arg2, expected result: true
        assertEquals(Boolean.TRUE, operation.computeValue(contextWithValue(arg1, 5), contextWithValue(arg2, 5.0))); // arg1 = arg2, expected result: true
        assertEquals(Boolean.TRUE, operation.computeValue(contextWithValue(arg1, "abc"), contextWithValue(arg2, "xyz"))); // arg1 < arg2, expected result: true

        // Test cases where arg1 > arg2
        assertEquals(Boolean.FALSE, operation.computeValue(contextWithValue(arg1, 10), contextWithValue(arg2, 5))); // arg1 > arg2, expected result: false
        
        // Test edge cases
        assertEquals(Boolean.TRUE, operation.computeValue(contextWithValue(arg1, Double.MIN_VALUE), contextWithValue(arg2, Double.MAX_VALUE))); // arg1 < arg2, expected result: true
        assertEquals(Boolean.TRUE, operation.computeValue(contextWithValue(arg1, Double.MAX_VALUE), contextWithValue(arg2, Double.MAX_VALUE))); // arg1 = arg2, expected result: true
        assertEquals(Boolean.FALSE, operation.computeValue(contextWithValue(arg1, Double.MAX_VALUE), contextWithValue(arg2, Double.MIN_VALUE))); // arg1 > arg2, expected result: false

        // Test error scenarios
        // Test cases where arg1 or arg2 is null
        assertEquals(Boolean.FALSE, operation.computeValue(contextWithValue(arg1, 5), contextWithValue(arg2, null))); // arg1 < arg2 but arg2 is null, expected result: false
        assertEquals(Boolean.FALSE, operation.computeValue(contextWithValue(arg1, null), contextWithValue(arg2, 10))); // arg1 > arg2 but arg1 is null, expected result: false
        assertEquals(Boolean.FALSE, operation.computeValue(contextWithValue(arg1, null), contextWithValue(arg2, null))); // both arg1 and arg2 are null, expected result: false
    }

    @Test
    public void testGetSymbol() {
        // Test typical use case
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        assertEquals("<=", operation.getSymbol());
    }

    // Helper method to create eval context with given variable values
    private EvalContext contextWithValue(String variable, Object value) {
        EvalContext context = new EvalContext();
        context.setVariable(variable, value);
        return context;
    }
}
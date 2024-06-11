import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        operation = new CoreOperationGreaterThan(new Constant(-1), new Constant(0));
        result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);

        // Add more test cases for different scenarios
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        assertEquals(">", operation.getSymbol());
    }

    // Add more test cases for other public methods of CoreOperationGreaterThan
    
    // Edge cases and exceptions to be tested
    
    // Test when both arguments are null

    // Test when one argument is null
    
    // Test when both arguments are non-numeric

    // Test when both arguments are equal

    // Test for large numeric values

}
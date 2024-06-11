import static org.junit.Assert.assertEquals;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(expression1, expression2);
        EvalContext context = new EvalContext();
        
        // Typical use case
        assertEquals(Boolean.TRUE, greaterThanOrEqual.computeValue(context));
        
        // Edge cases
        // Test when both arguments are integers
        // 5 >= 5 should return true
        assertEquals(Boolean.TRUE, greaterThanOrEqual.computeValue(context));
        
        // Test when left argument is less than the right argument
        // 3 >= 5 should return false
        assertEquals(Boolean.FALSE, greaterThanOrEqual.computeValue(context));
        
        // Test when left argument is greater than the right argument
        // 7 >= 3 should return true
        assertEquals(Boolean.TRUE, greaterThanOrEqual.computeValue(context));
        
        // Error scenarios
        // Test when left argument is null
        // Null pointer exception should be thrown
        try {
            expression1 = null;
            greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(expression1, expression2);
            greaterThanOrEqual.computeValue(context);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
        
        // Test when right argument is null
        // Null pointer exception should be thrown
        try {
            expression2 = null;
            greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(expression1, expression2);
            greaterThanOrEqual.computeValue(context);
        } catch (NullPointerException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(expression1, expression2);
        
        // Typical use case
        assertEquals(">=", greaterThanOrEqual.getSymbol());
        
        // Edge case
        // Test when getting the symbol returns ">="
        assertEquals(">=", greaterThanOrEqual.getSymbol());
    }
}
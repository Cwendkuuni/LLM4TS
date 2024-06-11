import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test basic functionality
        // Test when arg1 is greater than arg2
        context.setNode(arg1, 5);
        context.setNode(arg2, 2);
        assertTrue((boolean)greaterThan.computeValue(context));
        
        // Test when arg1 is equal to arg2
        context.setNode(arg1, 5);
        context.setNode(arg2, 5);
        assertFalse((boolean)greaterThan.computeValue(context));
        
        // Test when arg1 is less than arg2
        context.setNode(arg1, 2);
        context.setNode(arg2, 5);
        assertFalse((boolean)greaterThan.computeValue(context));
        
        // Test edge cases and exception handling
        // Test when arg1 is a decimal number
        context.setNode(arg1, 5.5);
        context.setNode(arg2, 2);
        assertTrue((boolean)greaterThan.computeValue(context));
        
        // Test when arg2 is a decimal number
        context.setNode(arg1, 5);
        context.setNode(arg2, 2.5);
        assertFalse((boolean)greaterThan.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(arg1, arg2);
        assertEquals(">", greaterThan.getSymbol());
    }
}
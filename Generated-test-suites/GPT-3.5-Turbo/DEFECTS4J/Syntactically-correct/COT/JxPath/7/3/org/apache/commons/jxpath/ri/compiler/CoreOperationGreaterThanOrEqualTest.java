import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(expression1, expression2);
        // Test basic functionality
        EvalContext context = new EvalContext();
        context.setVariable("expression1", 5);
        context.setVariable("expression2", 3);
        assertEquals(true, operation.computeValue(context));
        
        // Test edge cases
        context.setVariable("expression1", 3);
        context.setVariable("expression2", 3);
        assertEquals(true, operation.computeValue(context));
        
        context.setVariable("expression1", -3);
        context.setVariable("expression2", 3);
        assertEquals(false, operation.computeValue(context));
        
        context.setVariable("expression1", 3);
        context.setVariable("expression2", -3);
        assertEquals(true, operation.computeValue(context));
        
        context.setVariable("expression1", Double.POSITIVE_INFINITY);
        context.setVariable("expression2", Double.NEGATIVE_INFINITY);
        assertEquals(true, operation.computeValue(context));
        
        context.setVariable("expression1", Double.NaN);
        context.setVariable("expression2", 5);
        assertEquals(false, operation.computeValue(context));
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue_LessThanOrEqualTo_ReturnsTrue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual();
        EvalContext context = new EvalContext();
        context.setContextNodeSet(new NodeSet());
        context.getContextPositionStack().push(1);
        
        // Test with l <= r
        double l = 2.5;
        double r = 3.5;
        context.getContextNodeSet().addNumber(l);
        context.getContextNodeSet().addNumber(r);
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_GreaterThan_ReturnsFalse() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual();
        EvalContext context = new EvalContext();
        context.setContextNodeSet(new NodeSet());
        context.getContextPositionStack().push(1);
        
        // Test with l > r
        double l = 4.5;
        double r = 3.5;
        context.getContextNodeSet().addNumber(l);
        context.getContextNodeSet().addNumber(r);
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
    
    // Add more test cases for other methods, edge cases, and exception handling scenarios
    
}



// Test cases for CoreOperationLessThanOrEqual methods


import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    
    @Test
    public void testComputeValue_notEqual_returnsTrue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test case 1
        context.setValue(arg1, "value1");
        context.setValue(arg2, "value2");
        assertEquals(Boolean.TRUE, operation.computeValue(context));
        
        // Test case 2
        context.setValue(arg1, 10);
        context.setValue(arg2, 20);
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }
    
    @Test
    public void testComputeValue_equal_returnsFalse() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        // Test case 1
        context.setValue(arg1, "value");
        context.setValue(arg2, "value");
        assertEquals(Boolean.FALSE, operation.computeValue(context));
        
        // Test case 2
        context.setValue(arg1, 10);
        context.setValue(arg2, 10);
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }
    
    @Test
    public void testGetSymbol_returnsNotEqualSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        
        assertEquals("!=", operation.getSymbol());
    }
    
}
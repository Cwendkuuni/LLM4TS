import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {
    
    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        
        // Test case 1
        EvalContext context1 = new EvalContext();
        boolean result1 = operation.computeValue(context1);
        assertFalse(result1);
        
        // Test case 2
        EvalContext context2 = new EvalContext();
        context2.setArg1(null);
        context2.setArg2(null);
        boolean result2 = operation.computeValue(context2);
        assertFalse(result2);
        
        // Test case 3
        EvalContext context3 = new EvalContext();
        context3.setArg1(5);
        context3.setArg2(5);
        boolean result3 = operation.computeValue(context3);
        assertTrue(result3);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        String symbol = operation.getSymbol();
        
        assertEquals("!=", symbol);
    }
}
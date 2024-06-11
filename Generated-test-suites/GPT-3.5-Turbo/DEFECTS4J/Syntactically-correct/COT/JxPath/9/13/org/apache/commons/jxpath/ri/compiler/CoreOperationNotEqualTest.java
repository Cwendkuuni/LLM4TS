import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        // Test case 1: arg1 = null, arg2 = null
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        boolean result = (boolean) operation.computeValue(context);
        assertFalse(result);
        
        // Test case 2: arg1 = "apple", arg2 = "orange"
        arg1 = new Expression("apple");
        arg2 = new Expression("orange");
        operation = new CoreOperationNotEqual(arg1, arg2);
        result = (boolean) operation.computeValue(context);
        assertTrue(result);
        
        // Test case 3: arg1 = "apple", arg2 = "apple"
        arg1 = new Expression("apple");
        arg2 = new Expression("apple");
        operation = new CoreOperationNotEqual(arg1, arg2);
        result = (boolean) operation.computeValue(context);
        assertFalse(result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression("apple");
        Expression arg2 = new Expression("orange");
        CoreOperationNotEqual operation = new CoreOperationNotEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}
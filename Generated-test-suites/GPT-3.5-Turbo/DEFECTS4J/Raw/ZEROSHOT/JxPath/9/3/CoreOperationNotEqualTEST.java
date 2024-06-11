import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        
        // Test when arg1 and arg2 are equal
        EvalContext context1 = new EvalContext();
        Object result1 = coreOperationNotEqual.computeValue(context1);
        assertTrue(result1 instanceof Boolean);
        assertFalse((boolean) result1);
        
        // Test when arg1 and arg2 are not equal
        EvalContext context2 = new EvalContext();
        Object result2 = coreOperationNotEqual.computeValue(context2);
        assertTrue(result2 instanceof Boolean);
        assertTrue((boolean) result2);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        
        String symbol = coreOperationNotEqual.getSymbol();
        assertNotNull(symbol);
    }
}
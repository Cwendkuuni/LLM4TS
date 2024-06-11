import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        
        // Test when equal
        Object result1 = coreOperationEqual.computeValue(context);
        assertTrue("Invalid result", (Boolean)result1);
        
        // Test when not equal
        Object result2 = coreOperationEqual.computeValue(context);
        assertFalse("Invalid result", (Boolean)result2);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);
        
        String symbol = coreOperationEqual.getSymbol();
        assertEquals("Invalid symbol", "=", symbol);
    }

}
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        // Create test objects
        Expression arg1 = new Constant("test1");
        Expression arg2 = new Constant("test2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        
        // Test computeValue method
        EvalContext context = new EvalContext(null);
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue((Boolean)result);
    }

    @Test
    public void testGetSymbol() {
        // Create test objects
        Expression arg1 = new Constant("test1");
        Expression arg2 = new Constant("test2");
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);
        
        // Test getSymbol method
        String symbol = coreOperationNotEqual.getSymbol();
        assertEquals("!=", symbol);
    }
}
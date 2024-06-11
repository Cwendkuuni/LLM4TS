import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValueNotEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setContextNodePointer(new NodePointer());
        
        Object result = coreOperationNotEqual.computeValue(context);
        assertTrue((boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(), new Expression());
        
        String symbol = coreOperationNotEqual.getSymbol();
        assertEquals("!=", symbol);
    }
    
    // Edge cases and exception handling scenarios to be tested:
    // - Null argument for arg1 or arg2
    
    // Testing for null argument for arg1
    @Test(expected = NullPointerException.class)
    public void testNullArg1() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(null, new Expression());
        coreOperationNotEqual.computeValue(new EvalContext());
    }
    
    // Testing for null argument for arg2
    @Test(expected = NullPointerException.class)
    public void testNullArg2() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(), null);
        coreOperationNotEqual.computeValue(new EvalContext());
    }
}
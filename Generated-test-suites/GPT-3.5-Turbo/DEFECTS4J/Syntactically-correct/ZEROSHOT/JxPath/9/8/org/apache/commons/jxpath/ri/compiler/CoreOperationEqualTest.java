import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValueEqual() {
        CoreOperationEqual core = new CoreOperationEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        context.setArg((Object)"value1");
        context.setArg((Object)"value1");
        context.setArg((Object)"value2");
        
        Object result = core.computeValue(context);
        
        // Evaluate if result is TRUE as expected
        assertTrue((Boolean)result);
    }
    
    @Test
    public void testComputeValueNotEqual() {
        CoreOperationEqual core = new CoreOperationEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        context.setArg((Object)"value1");
        context.setArg((Object)"value2");
        
        Object result = core.computeValue(context);
        
        // Evaluate if result is FALSE as expected
        assertFalse((Boolean)result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationEqual core = new CoreOperationEqual(new Expression(), new Expression());
        
        String symbol = core.getSymbol();
        
        // Evaluate if symbol is "=" as expected
        assertEquals("=", symbol);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {
    
    @Test
    public void testComputeValue_NotEqual() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setContextNodeSet(new NodeSet());
        
        Object result = coreOperationNotEqual.computeValue(context);
        
        assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(new Expression(), new Expression());
        
        String symbol = coreOperationNotEqual.getSymbol();
        
        assertEquals("!=", symbol);
    }
}
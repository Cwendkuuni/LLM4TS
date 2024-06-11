import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        
        // Test when args[0] is equal to args[1]
        context.setContextNode(args[0]);
        boolean result = operation.computeValue(context);
        assertFalse(result);
        
        // Test when args[0] is not equal to args[1]
        context.setContextNode(args[1]);
        result = operation.computeValue(context);
        assertTrue(result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        
        // Test if the symbol is "!="
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        EvalContext context = new EvalContext();
        
        // Test when arg1 and arg2 are both null
        // The result should be false
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
        
        // Test when arg1 and arg2 are both non-null and equal
        // The result should be false
        operation = new CoreOperationNotEqual(new Expression(), new Expression());
        result = operation.computeValue(context);
        assertFalse((Boolean) result);
        
        // Test when arg1 and arg2 are both non-null and not equal
        // The result should be true
        operation = new CoreOperationNotEqual(new Expression(), new Expression());
        operation.addArgument("arg1");
        operation.addArgument("arg2");
        result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(null, null);
        
        // Test that the symbol is "!="
        String symbol = operation.getSymbol();
        assertEquals("!=", symbol);
    }
}
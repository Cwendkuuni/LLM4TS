import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
    
    private CoreOperationGreaterThanOrEqual operation;
    
    @Before
    public void setUp() {
        operation = new CoreOperationGreaterThanOrEqual(null, null);
    }
    
    @Test
    public void testComputeValue_ValidArguments_ReturnsTrue() {
        double arg1 = 5.0;
        double arg2 = 3.0;
        EvalContext context = new EvalContext();
        context.setSingleNodeValue(arg1);
        context.setSingleNodeValue(arg2);
        
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_ValidArguments_ReturnsFalse() {
        double arg1 = 3.0;
        double arg2 = 5.0;
        EvalContext context = new EvalContext();
        context.setSingleNodeValue(arg1);
        context.setSingleNodeValue(arg2);
        
        Object result = operation.computeValue(context);
        
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_NullArgument_ReturnsFalse() {
        double arg1 = 3.0;
        EvalContext context = new EvalContext();
        context.setSingleNodeValue(arg1);
        context.setSingleNodeValue(null);
        
        Object result = operation.computeValue(context);
        
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_NonNumericArguments_ReturnsFalse() {
        String arg1 = "abc";
        String arg2 = "def";
        EvalContext context = new EvalContext();
        context.setSingleNodeValue(arg1);
        context.setSingleNodeValue(arg2);
        
        Object result = operation.computeValue(context);
        
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_EqualArguments_ReturnsTrue() {
        double arg1 = 5.0;
        double arg2 = 5.0;
        EvalContext context = new EvalContext();
        context.setSingleNodeValue(arg1);
        context.setSingleNodeValue(arg2);
        
        Object result = operation.computeValue(context);
        
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol_ReturnsCorrectSymbol() {
        String expectedSymbol = ">=";
        
        String symbol = operation.getSymbol();
        
        assertEquals(expectedSymbol, symbol);
    }
}
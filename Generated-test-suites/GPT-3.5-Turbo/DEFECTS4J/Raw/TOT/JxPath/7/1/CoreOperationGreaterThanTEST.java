import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan operationGreaterThan;
    
    @Before
    public void setUp() {
        operationGreaterThan = new CoreOperationGreaterThan(new Expression(), new Expression());
    }
    
    @Test
    public void testComputeValue_WithGreaterValues_ShouldReturnTrue() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 2);
        
        boolean result = (boolean) operationGreaterThan.computeValue(context);
        
        assertTrue(result);
    }
    
    @Test
    public void testComputeValue_WithSameValues_ShouldReturnFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 10);
        context.setVariable("arg2", 10);
        
        boolean result = (boolean) operationGreaterThan.computeValue(context);
        
        assertFalse(result);
    }
    
    @Test
    public void testComputeValue_WithSmallerValues_ShouldReturnFalse() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 1);
        context.setVariable("arg2", 3);
        
        boolean result = (boolean) operationGreaterThan.computeValue(context);
        
        assertFalse(result);
    }
    
    @Test
    public void testComputeValue_WithNullValues_ShouldThrowNullPointerException() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", null);
        context.setVariable("arg2", null);
        
        try {
            operationGreaterThan.computeValue(context);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception
        }
    }
    
    @Test
    public void testGetSymbol_ShouldReturnGreaterThanSymbol() {
        String symbol = operationGreaterThan.getSymbol();
        
        assertEquals(">", symbol);
    }
}
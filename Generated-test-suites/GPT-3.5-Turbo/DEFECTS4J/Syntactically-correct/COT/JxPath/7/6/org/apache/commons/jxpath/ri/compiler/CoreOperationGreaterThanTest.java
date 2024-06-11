import org.junit.Test;

public class CoreOperationGreaterThanTest {
    
    @Test
    public void testComputeValue() {
        // Test with arg1 greater than arg2 
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();
        context.setValue(expression.args[0], 5);
        context.setValue(expression.args[1], 3);
        
        boolean result = (boolean) expression.computeValue(context);
        assertTrue(result);
        
        // Test with arg1 equal to arg2
        context.setValue(expression.args[0], 3);
        context.setValue(expression.args[1], 3);
        
        result = (boolean) expression.computeValue(context);
        assertFalse(result);
        
        // Test with arg1 less than arg2
        context.setValue(expression.args[0], 1);
        context.setValue(expression.args[1], 3);
        
        result = (boolean) expression.computeValue(context);
        assertFalse(result);
    }
    
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(), new Expression());
        String symbol = expression.getSymbol();
        
        assertEquals(">", symbol);
    }
}
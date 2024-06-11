import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CoreOperationGreaterThanTest {

    //Test case for computeValue method - Typical use case
    @Test
    void testComputeValueTypical() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        arg1.computeValue(context);
        arg2.computeValue(context);
        
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);
    }

    //Test case for computeValue method - Edge case with negative values
    @Test
    void testComputeValueEdgeNegative() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        arg1.computeValue(context);
        arg2.computeValue(context);

        // Setting negative values
        arg1.setValue(-5);
        arg2.setValue(-10);
        
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(true, result);
    }
    
    //Test case for computeValue method - Edge case with zero
    @Test
    void testComputeValueEdgeZero() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        
        EvalContext context = new EvalContext();
        arg1.computeValue(context);
        arg2.computeValue(context);

        // Setting zero values
        arg1.setValue(0);
        arg2.setValue(10);
        
        Object result = op.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertEquals(false, result);
    }
    
    //Test case for computeValue method - Error scenario with null context
    @Test
    void testComputeValueError() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        
        // Null context
        EvalContext context = null;
        
        assertThrows(NullPointerException.class, () -> {
            op.computeValue(context);
        });
    }
    
    //Test case for getSymbol method - Typical use case
    @Test
    void testGetSymbolTypical() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan op = new CoreOperationGreaterThan(arg1, arg2);
        
        String symbol = op.getSymbol();
        assertEquals(">", symbol);
    }

}
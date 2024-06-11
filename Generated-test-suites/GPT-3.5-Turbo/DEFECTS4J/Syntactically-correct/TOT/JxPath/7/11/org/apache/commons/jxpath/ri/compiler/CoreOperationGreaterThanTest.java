import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {
    
    @Test
    public void testComputeValue_typicalCase() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        // Test when arg1.computeValue(context) returns 5.0 
        // and arg2.computeValue(context) returns 3.0
        // Expected result is true
        EvalContext context = new EvalContext();
        arg1.setComputeValueResult(context, 5.0);
        arg2.setComputeValueResult(context, 3.0);
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_sameValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        // Test when arg1.computeValue(context) returns 4.0 
        // and arg2.computeValue(context) returns 4.0
        // Expected result is false
        EvalContext context = new EvalContext();
        arg1.setComputeValueResult(context, 4.0);
        arg2.setComputeValueResult(context, 4.0);
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_negativeValues() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        // Test when arg1.computeValue(context) returns -2.5 
        // and arg2.computeValue(context) returns -1.5
        // Expected result is false
        EvalContext context = new EvalContext();
        arg1.setComputeValueResult(context, -2.5);
        arg2.setComputeValueResult(context, -1.5);
        Object result = operation.computeValue(context);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_edgeCase() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        // Test when arg1.computeValue(context) returns largest positive double value 
        // and arg2.computeValue(context) returns negative infinity
        // Expected result is true
        EvalContext context = new EvalContext();
        arg1.setComputeValueResult(context, Double.MAX_VALUE);
        arg2.setComputeValueResult(context, Double.NEGATIVE_INFINITY);
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        // Test that the symbol returned is ">" as expected
        assertEquals(">", symbol);
    }
}
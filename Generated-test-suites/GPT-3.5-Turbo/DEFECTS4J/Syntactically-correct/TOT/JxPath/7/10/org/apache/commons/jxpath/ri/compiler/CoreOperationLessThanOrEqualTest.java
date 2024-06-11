import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue_TypicalCase_Less() {
        Expression arg1 = new ExpressionImpl(5);
        Expression arg2 = new ExpressionImpl(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_TypicalCase_Greater() {
        Expression arg1 = new ExpressionImpl(10);
        Expression arg2 = new ExpressionImpl(5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_TypicalCase_Equal() {
        Expression arg1 = new ExpressionImpl(5);
        Expression arg2 = new ExpressionImpl(5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_EdgeCase_NegativeInfinity() {
        Expression arg1 = new ExpressionImpl(Double.NEGATIVE_INFINITY);
        Expression arg2 = new ExpressionImpl(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testComputeValue_EdgeCase_PositiveInfinity() {
        Expression arg1 = new ExpressionImpl(10);
        Expression arg2 = new ExpressionImpl(Double.POSITIVE_INFINITY);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        Object result = operation.computeValue(context);
        
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
    
    @Test
    public void testComputeValue_ErrorCase_NullArg1() {
        Expression arg1 = null;
        Expression arg2 = new ExpressionImpl(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        try {
            Object result = operation.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }
    
    @Test
    public void testComputeValue_ErrorCase_NullArg2() {
        Expression arg1 = new ExpressionImpl(10);
        Expression arg2 = null;
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        
        try {
            Object result = operation.computeValue(context);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Passed
        }
    }
    
    @Test
    public void testGetSymbol() {
        Expression arg1 = new ExpressionImpl(10);
        Expression arg2 = new ExpressionImpl(5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        
        String symbol = operation.getSymbol();
        
        assertEquals("<=", symbol);
    }
}
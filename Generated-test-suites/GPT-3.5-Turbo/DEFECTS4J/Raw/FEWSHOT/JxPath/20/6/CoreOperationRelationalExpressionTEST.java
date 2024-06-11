import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest(new Expression[]{});
        
        // Test cases for greater than operation
        Object result1 = expression.computeValue(createEvalContext(5, 3));
        assertTrue(result1 instanceof Boolean);
        assertTrue((Boolean) result1);
        
        Object result2 = expression.computeValue(createEvalContext(3, 5));
        assertTrue(result2 instanceof Boolean);
        assertFalse((Boolean) result2);
        
        Object result3 = expression.computeValue(createEvalContext(5, 5));
        assertTrue(result3 instanceof Boolean);
        assertFalse((Boolean) result3);
        
        // Test cases for greater than or equal to operation
        Object result4 = expression.computeValue(createEvalContext(5, 3, true));
        assertTrue(result4 instanceof Boolean);
        assertTrue((Boolean) result4);
        
        Object result5 = expression.computeValue(createEvalContext(3, 5, true));
        assertTrue(result5 instanceof Boolean);
        assertFalse((Boolean) result5);
        
        Object result6 = expression.computeValue(createEvalContext(5, 5, true));
        assertTrue(result6 instanceof Boolean);
        assertTrue((Boolean) result6);
        
        // Test cases for less than operation
        Object result7 = expression.computeValue(createEvalContext(3, 5, false));
        assertTrue(result7 instanceof Boolean);
        assertTrue((Boolean) result7);
        
        Object result8 = expression.computeValue(createEvalContext(5, 3, false));
        assertTrue(result8 instanceof Boolean);
        assertFalse((Boolean) result8);
        
        Object result9 = expression.computeValue(createEvalContext(5, 5, false));
        assertTrue(result9 instanceof Boolean);
        assertFalse((Boolean) result9);
        
        // Test cases for less than or equal to operation
        Object result9 = expression.computeValue(createEvalContext(3, 5, true, false));
        assertTrue(result9 instanceof Boolean);
        assertTrue((Boolean) result9);
        
        Object result10 = expression.computeValue(createEvalContext(5, 3, true, false));
        assertTrue(result10 instanceof Boolean);
        assertFalse((Boolean) result10);
        
        Object result11 = expression.computeValue(createEvalContext(5, 5, true, false));
        assertTrue(result11 instanceof Boolean);
        assertTrue((Boolean) result11);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest(new Expression[]{});
        assertEquals(Expression.RELATIONAL_EXPR_PRECEDENCE, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionTest(new Expression[]{});
        assertFalse(expression.isSymmetric());
    }

    private EvalContext createEvalContext(Object left, Object right) {
        EvalContext evalContext = new EvalContext();
        evalContext.setSingleNodePointer(left);
        evalContext.setSingleNodePointer(right);
        return evalContext;
    }
    
    private EvalContext createEvalContext(Object left, Object right, boolean isGreaterThanOrEqualTo) {
        EvalContext evalContext = createEvalContext(left, right);
        evalContext.setGreaterThanOrEqualTo(isGreaterThanOrEqualTo);
        return evalContext;
    }
    
    private EvalContext createEvalContext(Object left, Object right, boolean isGreaterThanOrEqualTo, boolean isLessThanOrEqualTo) {
        EvalContext evalContext = createEvalContext(left, right, isGreaterThanOrEqualTo);
        evalContext.setLessThanOrEqualTo(isLessThanOrEqualTo);
        return evalContext;
    }
}
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    private class TestExpression extends CoreOperationRelationalExpression {
        
        public TestExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare == 0;
        }
    }
    
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression(null);
        args[1] = new TestExpression(null);
        
        CoreOperationRelationalExpression expression = new TestExpression(args);
        
        assertTrue(expression.computeValue(null));
        
        args[0] = new TestExpression(null);
        args[1] = new TestExpression(null);
        
        expression = new TestExpression(args);
        
        assertFalse(expression.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression(null);
        args[1] = new TestExpression(null);
        
        CoreOperationRelationalExpression expression = new TestExpression(args);
        
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression(null);
        args[1] = new TestExpression(null);
        
        CoreOperationRelationalExpression expression = new TestExpression(args);
        
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression(null);
        args[1] = new TestExpression(null);
        
        CoreOperationRelationalExpression expression = new TestExpression(args);
        
        assertTrue(expression.compute(null, null));
        
        args[0] = new TestExpression(null);
        args[1] = new TestExpression(null);
        
        expression = new TestExpression(args);
        
        assertFalse(expression.compute(null, null));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression(null);
        args[1] = new TestExpression(null);
        
        CoreOperationRelationalExpression expression = new TestExpression(args);
        
        // Test with SelfContext
        Object result = expression.reduce(new SelfContext());
        assertEquals(null, result);
        
        // Test with Collection
        result = expression.reduce(new HashSet<Object>());
        assertEquals(null, result);
        
        // Test with other object
        result = expression.reduce("test");
        assertEquals("test", result);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression(null);
        args[1] = new TestExpression(null);
        
        CoreOperationRelationalExpression expression = new TestExpression(args);
        
        assertFalse(expression.containsMatch(null, null));
        
        args[0] = new TestExpression(null);
        args[1] = new TestExpression(null);
        
        expression = new TestExpression(args);
        
        assertFalse(expression.containsMatch(null, null));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression(null);
        args[1] = new TestExpression(null);
        
        CoreOperationRelationalExpression expression = new TestExpression(args);
        
        assertFalse(expression.findMatch(null, null));
        
        args[0] = new TestExpression(null);
        args[1] = new TestExpression(null);
        
        expression = new TestExpression(args);
        
        assertFalse(expression.findMatch(null, null));
    }
}
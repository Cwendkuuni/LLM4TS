import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
  
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        // Test when compute returns true
        args[0] = new ExpressionImpl();
        args[1] = new ExpressionImpl();
        assertTrue(expression.computeValue(new EvalContext()));
        
        // Test when compute returns false
        args[0] = new ExpressionImpl();
        args[1] = new ExpressionImpl();
        assertFalse(expression.computeValue(new EvalContext()));
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        assertEquals(3, expression.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        assertFalse(expression.isSymmetric());
    }
    
    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        assertTrue(expression.evaluateCompare(0));
        assertTrue(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(1));
    }
    
    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        assertFalse(expression.compute(1, 2));
        assertTrue(expression.compute(2, 1));
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        Object object = new Object();
        assertEquals(object, expression.reduce(object));
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        Iterator<Object> it = new IteratorImpl<>();
        assertFalse(expression.containsMatch(it, new Object()));
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionImpl(args);
        
        Iterator<Object> lit = new IteratorImpl<>();
        Iterator<Object> rit = new IteratorImpl<>();
        assertFalse(expression.findMatch(lit, rit));
    }
    
    private class CoreOperationRelationalExpressionImpl extends CoreOperationRelationalExpression {
      
        public CoreOperationRelationalExpressionImpl(Expression[] args) {
            super(args);
        }
        
        protected boolean evaluateCompare(int compare) {
            return compare < 0;
        }
    }
  
    private class ExpressionImpl extends Expression {
      
        public Object computeValue(EvalContext context) {
            return true;
        }
    }
  
    private class IteratorImpl<T> implements Iterator<T> {
      
        public boolean hasNext() {
            return false;
        }
        
        public T next() {
            return null;
        }
        
        public void remove() {
        }
    }
}
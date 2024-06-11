import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(args);
        
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertTrue(result instanceof Boolean);
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(args);
        
        int precedence = expression.getPrecedence();
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(args);
        
        boolean isSymmetric = expression.isSymmetric();
        assertFalse(isSymmetric);
    }
    
    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(args);
        
        Object left = new Object();
        Object right = new Object();
        
        boolean result = expression.compute(left, right);
        assertTrue(result);
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(args);
        
        Object o = new Object();
        
        Object result = expression.reduce(o);
        assertEquals(o, result);
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(args);
        
        Iterator it = new DummyIterator();
        Object value = new Object();
        
        boolean result = expression.containsMatch(it, value);
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new ConcreteCoreOperationRelationalExpression(args);
        
        Iterator lit = new DummyIterator();
        Iterator rit = new DummyIterator();
        
        boolean result = expression.findMatch(lit, rit);
        assertFalse(result);
    }
    
    private class DummyExpression extends Expression {
        public Object compute(EvalContext context) {
            return new Object();
        }
    }
    
    private class DummyIterator implements Iterator {
        public boolean hasNext() {
            return false;
        }
        public Object next() {
            return null;
        }
    }
    
    private class ConcreteCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        public ConcreteCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }
        protected boolean evaluateCompare(int compare) {
            return true;
        }
    }

}
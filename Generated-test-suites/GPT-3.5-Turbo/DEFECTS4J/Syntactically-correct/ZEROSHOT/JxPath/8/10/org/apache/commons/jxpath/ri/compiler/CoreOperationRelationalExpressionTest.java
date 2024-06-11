import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[]{new TestExpression(), new TestExpression()};
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        EvalContext context = new TestEvalContext();
        Object result = expression.computeValue(context);
        
        assertNotNull(result);
        assertEquals(Boolean.class, result.getClass());
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[]{new TestExpression(), new TestExpression()};
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        int precedence = expression.getPrecedence();
        
        assertEquals(3, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[]{new TestExpression(), new TestExpression()};
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        boolean isSymmetric = expression.isSymmetric();
        
        assertFalse(isSymmetric);
    }
    
    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[]{new TestExpression(), new TestExpression()};
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        boolean result = expression.evaluateCompare(0);
        
        assertTrue(result);
    }
    
    @Test
    public void testCompute() {
        Expression[] args = new Expression[]{new TestExpression(), new TestExpression()};
        TestCoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        Object left = new Object();
        Object right = new Object();
        boolean result = expression.compute(left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[]{new TestExpression(), new TestExpression()};
        TestCoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        Object o = new Object();
        Object result = expression.reduce(o);
        
        assertSame(o, result);
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[]{new TestExpression(), new TestExpression()};
        TestCoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        TestIterator it = new TestIterator();
        Object value = new Object();
        boolean result = expression.containsMatch(it, value);
        
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[]{new TestExpression(), new TestExpression()};
        TestCoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        TestIterator lit = new TestIterator();
        TestIterator rit = new TestIterator();
        boolean result = expression.findMatch(lit, rit);
        
        assertFalse(result);
    }
    
    private static class TestExpression extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }
    }
    
    private static class TestEvalContext extends EvalContext {
        @Override
        public Object getSingleNodePointer() {
            return null;
        }
    }
    
    private static class TestIterator implements Iterator {
        @Override
        public boolean hasNext() {
            return false;
        }
        @Override
        public Object next() {
            return null;
        }
        @Override
        public void remove() { }
    }
    
    private static class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        public TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }
        @Override
        protected boolean evaluateCompare(int compare) {
            return true;
        }
    }
}
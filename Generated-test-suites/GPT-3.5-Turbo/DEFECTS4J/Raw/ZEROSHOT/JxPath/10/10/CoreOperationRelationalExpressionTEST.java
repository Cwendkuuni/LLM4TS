import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        
        assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        int precedence = expression.getPrecedence();
        
        assertEquals(3, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        boolean symmetric = expression.isSymmetric();
        
        assertEquals(false, symmetric);
    }
    
    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        boolean compare = expression.evaluateCompare(3);
        
        assertEquals(true, compare);
    }
    
    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        boolean compute = expression.compute(1, 2);
        
        assertEquals(true, compute);
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        Object reduce = expression.reduce(new Object());
        
        assertNotNull(reduce);
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        Iterator<Object> it = new TestIterator();
        Object value = new Object();
        boolean containsMatch = expression.containsMatch(it, value);
        
        assertEquals(false, containsMatch);
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);
        
        Iterator<Object> lit = new TestIterator();
        Iterator<Object> rit = new TestIterator();
        
        boolean findMatch = expression.findMatch(lit, rit);
        
        assertEquals(false, findMatch);
    }
    
    private class TestExpression implements Expression {
        public Object computeValue(EvalContext context) { return null; }
    }
    
    private class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        public TestCoreOperationRelationalExpression(Expression[] args) { super(args); }
        protected boolean evaluateCompare(int compare) { return true; }
    }
    
    private class TestIterator implements Iterator<Object> {
        public boolean hasNext() { return false; }
        public Object next() { return null; }
        public void remove() {}
    }

}
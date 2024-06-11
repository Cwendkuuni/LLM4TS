import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression coreOperation = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SelfContext());
        
        assertTrue(coreOperation.computeValue(context));
    }
    
    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression coreOperation = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        assertTrue(coreOperation.evaluateCompare(1));
        assertFalse(coreOperation.evaluateCompare(-1));
    }
    
    @Test
    public void testContainsMatch() {
        Iterator it = new MockIterator();
        Object value = new Object();
        
        CoreOperationRelationalExpression coreOperation = new CoreOperationRelationalExpression(null) {};
        
        assertFalse(coreOperation.containsMatch(it, value));
    }
    
    @Test
    public void testFindMatch() {
        Iterator lit = new MockIterator();
        Iterator rit = new MockIterator();
        
        CoreOperationRelationalExpression coreOperation = new CoreOperationRelationalExpression(null) {};
        
        assertFalse(coreOperation.findMatch(lit, rit));
    }
}

class MockExpression extends Expression {
    @Override
    public Object compute(EvalContext context) {
        return null;
    }
}

class MockIterator implements Iterator {
    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        return null;
    }
}
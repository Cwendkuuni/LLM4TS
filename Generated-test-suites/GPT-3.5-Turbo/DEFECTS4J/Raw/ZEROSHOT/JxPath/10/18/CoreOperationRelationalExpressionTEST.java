import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        
        assertTrue(result instanceof Boolean);
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        int precedence = expression.getPrecedence();
        
        assertEquals(3, precedence);
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        boolean symmetric = expression.isSymmetric();
        
        assertFalse(symmetric);
    }
    
    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Object left = new Object();
        Object right = new Object();
        
        boolean result = expression.compute(left, right);
        
        assertTrue(result);
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Object o = new Object();
        
        Object result = expression.reduce(o);
        
        assertEquals(o, result);
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Iterator it = new MockIterator();
        Object value = new Object();
        
        boolean containsMatch = expression.containsMatch(it, value);
        
        assertFalse(containsMatch);
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Iterator lit = new MockIterator();
        Iterator rit = new MockIterator();
        
        boolean findMatch = expression.findMatch(lit, rit);
        
        assertFalse(findMatch);
    }
}

class MockExpression extends Expression {
    public Object computeValue(EvalContext context) {
        return new Object();
    }
}

class MockIterator implements Iterator {
    public boolean hasNext() {
        return false;
    }
    
    public Object next() {
        return null;
    }
    
    public void remove() {
        
    }
}
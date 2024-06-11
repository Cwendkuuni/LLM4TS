import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        EvalContext context = new TestEvalContext();
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        
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
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        
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
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Object left = new Object();
        Object right = new Object();
        boolean result = expression.compute(left, right);
        assertFalse(result);
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        
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
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Iterator it = new TestIterator();
        Object value = new Object();
        boolean result = expression.containsMatch(it, value);
        assertFalse(result);
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        Iterator lit = new TestIterator();
        Iterator rit = new TestIterator();
        boolean result = expression.findMatch(lit, rit);
        assertFalse(result);
    }
    
    private class TestExpression extends Expression {
        public Object computeValue(EvalContext context) {
            return null;
        }
        protected void checkContextNodeSetType(EvalContext context) {
        }
        public int getPrecedence() {
            return 0;
        }
    }
    
    private class TestEvalContext extends EvalContext {
        public Object getSingleNodePointer() {
            return null;
        }
    }
    
    private class TestIterator implements Iterator {
        public boolean hasNext() {
            return false;
        }
        public Object next() {
            return null;
        }
        public void remove() {
        }
    }
}
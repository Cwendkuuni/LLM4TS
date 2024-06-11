import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        
        assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }
    
    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) { 
                return compare > 0; 
            }
        };
        
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, operation.getPrecedence());
    }
    
    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) { 
                return compare > 0; 
            }
        };
        
        assertFalse(operation.isSymmetric());
    }
    
    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) { 
                return compare > 0; 
            }
        };
        
        assertTrue(operation.compute(10, 5));
        assertFalse(operation.compute(5, 10));
    }
    
    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) { 
                return compare > 0; 
            }
        };
        
        assertEquals(10, operation.reduce(10));
    }
    
    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) { 
                return compare > 0; 
            }
        };
        
        assertTrue(operation.containsMatch(new Iterator() {
            public boolean hasNext() { return true; }
            public Object next() { return 10; }
        }, 10));
        assertFalse(operation.containsMatch(new Iterator() {
            public boolean hasNext() { return true; }
            public Object next() { return 5; }
        }, 10));
    }
    
    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) { 
                return compare > 0; 
            }
        };
        
        assertTrue(operation.findMatch(new Iterator() {
            public boolean hasNext() { return true; }
            public Object next() { return 10; }
        }, new Iterator() {
            public boolean hasNext() { return true; }
            public Object next() { return 5; }
        }));
        assertFalse(operation.findMatch(new Iterator() {
            public boolean hasNext() { return true; }
            public Object next() { return 5; }
        }, new Iterator() {
            public boolean hasNext() { return true; }
            public Object next() { return 10; }
        }));
    }
}
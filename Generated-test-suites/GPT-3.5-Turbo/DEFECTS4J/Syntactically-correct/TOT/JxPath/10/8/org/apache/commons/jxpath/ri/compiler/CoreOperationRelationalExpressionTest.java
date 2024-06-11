import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext context = new EvalContext();
        
        // Test when compute returns true
        args[0] = new Expression() {
            public Object computeValue(EvalContext context) {
                return true;
            }
        };
        args[1] = new Expression() {
            public Object computeValue(EvalContext context) {
                return true;
            }
        };
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Test when compute returns false
        args[0] = new Expression() {
            public Object computeValue(EvalContext context) {
                return false;
            }
        };
        args[1] = new Expression() {
            public Object computeValue(EvalContext context) {
                return true;
            }
        };
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext context = new EvalContext();
        
        // Test when left and right are equal
        Object left = 5;
        Object right = 5;
        assertTrue(expression.compute(left, right));
        
        // Test when left is less than right
        left = 3;
        right = 5;
        assertTrue(expression.compute(left, right));
        
        // Test when left is greater than right
        left = 5;
        right = 3;
        assertFalse(expression.compute(left, right));
        
        // Test when left and right are NaN
        left = Double.NaN;
        right = Double.NaN;
        assertFalse(expression.compute(left, right));
        
        // Test when left and right are Iterators and findMatch returns true
        left = new MockIterator(new Object[] {1, 2, 3});
        right = new MockIterator(new Object[] {3, 4, 5});
        assertTrue(expression.compute(left, right));
        
        // Test when left and right are Iterators and findMatch returns false
        left = new MockIterator(new Object[] {1, 2, 3});
        right = new MockIterator(new Object[] {4, 5, 6});
        assertFalse(expression.compute(left, right));
        
        // Test when left is Iterator and containsMatch returns true
        left = new MockIterator(new Object[] {1, 2, 3});
        right = 2;
        assertTrue(expression.compute(left, right));
        
        // Test when right is Iterator and containsMatch returns true
        left = 2;
        right = new MockIterator(new Object[] {1, 2, 3});
        assertTrue(expression.compute(left, right));
        
        // Test when left is Iterator and containsMatch returns false
        left = new MockIterator(new Object[] {1, 2, 3});
        right = 4;
        assertFalse(expression.compute(left, right));
        
        // Test when right is Iterator and containsMatch returns false
        left = 4;
        right = new MockIterator(new Object[] {1, 2, 3});
        assertFalse(expression.compute(left, right));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        // Test when o is SelfContext
        Object o = new SelfContext();
        assertEquals(((EvalContext) o).getSingleNodePointer(), expression.reduce(o));
        
        // Test when o is Collection
        o = new HashSet<>();
        assertEquals(((Collection) o).iterator(), expression.reduce(o));
        
        // Test when o is neither SelfContext nor Collection
        o = new Object();
        assertEquals(o, expression.reduce(o));
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        // Test when Iterator contains match
        Object value = 2;
        Iterator it = new MockIterator(new Object[] {1, 2, 3});
        assertTrue(expression.containsMatch(it, value));
        
        // Test when Iterator does not contain match
        value = 4;
        it = new MockIterator(new Object[] {1, 2, 3});
        assertFalse(expression.containsMatch(it, value));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        
        // Test when findMatch returns true
        Iterator lit = new MockIterator(new Object[] {1, 2, 3});
        Iterator rit = new MockIterator(new Object[] {3, 4, 5});
        assertTrue(expression.findMatch(lit, rit));
        
        // Test when findMatch returns false
        lit = new MockIterator(new Object[] {1, 2, 3});
        rit = new MockIterator(new Object[] {4, 5, 6});
        assertFalse(expression.findMatch(lit, rit));
    }
    
    // MockIterator class for testing
    private class MockIterator implements Iterator {
        private Object[] elements;
        private int index;
        
        public MockIterator(Object[] elements) {
            this.elements = elements;
        }

        public boolean hasNext() {
            return index < elements.length;
        }

        public Object next() {
            return elements[index++];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
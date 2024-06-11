import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test typical use cases
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertTrue(operation.computeValue(new EvalContext()));
        assertFalse(operation.computeValue(new EvalContext()));

        // Test edge cases
        args[0] = null;
        args[1] = null;
        operation = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertFalse(operation.computeValue(new EvalContext()));

        args[0] = new Expression();
        args[1] = null;
        operation = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertFalse(operation.computeValue(new EvalContext()));

        args[0] = null;
        args[1] = new Expression();
        operation = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertFalse(operation.computeValue(new EvalContext()));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertEquals(3, operation.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
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
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        assertTrue(operation.compute(new Object(), new Object()));
        assertFalse(operation.compute(new Object(), new Object()));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        Object o = new Object();
        Object result = operation.reduce(o);
        assertSame(o, result);

        o = new SelfContext();
        result = operation.reduce(o);
        assertSame(o, result);

        o = new HashSet();
        result = operation.reduce(o);
        assertTrue(result instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        Iterator it = new Iterator() {
            int count = 0;

            @Override
            public boolean hasNext() {
                return count < 4;
            }

            @Override
            public Object next() {
                count++;
                return count;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

        assertTrue(operation.containsMatch(it, 2));
        assertFalse(operation.containsMatch(it, 5));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression operation = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };

        Iterator lit = new Iterator() {
            int count = 0;

            @Override
            public boolean hasNext() {
                return count < 4;
            }

            @Override
            public Object next() {
                count++;
                return count;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

        Iterator rit = new Iterator() {
            int count = 0;

            @Override
            public boolean hasNext() {
                return count < 4;
            }

            @Override
            public Object next() {
                count++;
                return count;
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };

        assertTrue(operation.findMatch(lit, rit));
        assertFalse(operation.findMatch(lit, rit));
    }
}
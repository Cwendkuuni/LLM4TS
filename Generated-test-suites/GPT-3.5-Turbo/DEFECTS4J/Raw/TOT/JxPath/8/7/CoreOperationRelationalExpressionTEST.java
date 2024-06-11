import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.compiler.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Object[] args = new Object[2];
        args[0] = 10;
        args[1] = 5;
        Expression[] expressions = new Expression[1];
        expressions[0] = new ConstantExpression(args[0]);
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        EvalContext context = new EvalContext();
        Object result = coreOperationRelationalExpression.computeValue(context);
        assertTrue((boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] expressions = new Expression[1];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare < 0;
            }
        };
        int precedence = coreOperationRelationalExpression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] expressions = new Expression[1];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare == 0;
            }
        };
        boolean isSymmetric = coreOperationRelationalExpression.isSymmetric();
        assertFalse(isSymmetric);
    }

    @Test
    public void testCompute() {
        Expression[] expressions = new Expression[1];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        boolean leftGreaterThanRight = coreOperationRelationalExpression.compute(10, 5);
        assertTrue(leftGreaterThanRight);

        boolean leftSmallerThanRight = coreOperationRelationalExpression.compute(5, 10);
        assertFalse(leftSmallerThanRight);

        boolean leftEqualToRight = coreOperationRelationalExpression.compute(5, 5);
        assertTrue(leftEqualToRight);
    }

    @Test
    public void testReduce() {
        Expression[] expressions = new Expression[1];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        Object initialContext = coreOperationRelationalExpression.reduce(new InitialContext());
        assertTrue(initialContext instanceof InitialContext);

        Object collection = coreOperationRelationalExpression.reduce(new HashSet<>());
        assertTrue(collection instanceof Iterator);
    }

    @Test
    public void testContainsMatch() {
        Expression[] expressions = new Expression[1];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        Iterator iterator = new Iterator() {
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < 5;
            }

            @Override
            public Object next() {
                count++;
                return count;
            }
        };
        boolean containsMatch = coreOperationRelationalExpression.containsMatch(iterator, 3);
        assertTrue(containsMatch);
    }

    @Test
    public void testFindMatch() {
        Expression[] expressions = new Expression[1];
        CoreOperationRelationalExpression coreOperationRelationalExpression = new CoreOperationRelationalExpression(expressions) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
        Iterator leftIterator = new Iterator() {
            private int count = 0;

            @Override
            public boolean hasNext() {
                return count < 10;
            }

            @Override
            public Object next() {
                count++;
                return count;
            }
        };

        Iterator rightIterator = new Iterator() {
            private int count = 11;

            @Override
            public boolean hasNext() {
                return count < 16;
            }

            @Override
            public Object next() {
                count++;
                return count;
            }
        };

        boolean findMatch = coreOperationRelationalExpression.findMatch(leftIterator, rightIterator);
        assertTrue(findMatch);
    }
}
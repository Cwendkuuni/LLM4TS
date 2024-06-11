import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        boolean symmetric = expression.isSymmetric();
        assertFalse(symmetric);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
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
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Object o = new Object();
        Object result = expression.reduce(o);
        assertEquals(o, result);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Iterator it = new Iterator() {

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Object value = new Object();
        boolean result = expression.containsMatch(it, value);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new Expression();
        args[1] = new Expression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {

            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Iterator lit = new Iterator() {

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Iterator rit = new Iterator() {

            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        boolean result = expression.findMatch(lit, rit);
        assertFalse(result);
    }
}
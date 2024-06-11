import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        EvalContext context = null;
        Object result = expression.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        int precedence = expression.getPrecedence();

        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
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
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object left = null;
        Object right = null;
        boolean result = expression.compute(left, right);

        assertFalse(result);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Object o = null;
        Object result = expression.reduce(o);

        assertNull(result);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Iterator it = null;
        Object value = null;
        boolean result = expression.containsMatch(it, value);

        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[2]) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        Iterator lit = null;
        Iterator rit = null;
        boolean result = expression.findMatch(lit, rit);

        assertFalse(result);
    }
    
}
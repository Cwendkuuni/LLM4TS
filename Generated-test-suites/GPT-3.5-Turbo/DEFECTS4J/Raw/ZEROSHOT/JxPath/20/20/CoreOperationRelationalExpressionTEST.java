import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationRelationalExpressionTest {
    private CoreOperationRelationalExpression coreOperationRelationalExpression;

    @Before
    public void setUp() {
        coreOperationRelationalExpression = new CoreOperationRelationalExpression(null) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
    }

    @Test
    public void testComputeValue() {
        EvalContext context = null;

        Object result = coreOperationRelationalExpression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = coreOperationRelationalExpression.getPrecedence();

        Assert.assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean symmetric = coreOperationRelationalExpression.isSymmetric();

        Assert.assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        boolean evaluateCompare = coreOperationRelationalExpression.evaluateCompare(0);

        Assert.assertFalse(evaluateCompare);
    }

    @Test
    public void testCompute() {
        Object left = null;
        Object right = null;

        boolean result = coreOperationRelationalExpression.compute(left, right);

        Assert.assertFalse(result);
    }

    @Test
    public void testReduce() {
        Object operand = null;

        Object result = coreOperationRelationalExpression.reduce(operand);

        Assert.assertNull(result);
    }

    @Test
    public void testContainsMatch() {
        Iterator iterator = new ArrayList().iterator();
        Object value = null;

        boolean containsMatch = coreOperationRelationalExpression.containsMatch(iterator, value);

        Assert.assertFalse(containsMatch);
    }

    @Test
    public void testFindMath() {
        Iterator leftIterator = new ArrayList().iterator();
        Iterator rightIterator = new ArrayList().iterator();

        boolean findMatch = coreOperationRelationalExpression.findMatch(leftIterator, rightIterator);

        Assert.assertFalse(findMatch);
    }
}
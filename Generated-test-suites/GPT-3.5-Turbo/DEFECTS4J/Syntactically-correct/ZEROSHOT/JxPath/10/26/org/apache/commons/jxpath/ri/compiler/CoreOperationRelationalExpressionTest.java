import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        EvalContext context = new EvalContext(null);
        Object result = expression.computeValue(context);
        Assert.assertEquals(result, Boolean.FALSE);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        int precedence = expression.getPrecedence();
        Assert.assertEquals(precedence, 3);
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        boolean symmetric = expression.isSymmetric();
        Assert.assertFalse(symmetric);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Object left = null;
        Object right = null;
        boolean result = expression.compute(left, right);
        Assert.assertFalse(result);
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Object o = null;
        Object result = expression.reduce(o);
        Assert.assertNull(result);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Iterator it = null;
        Object value = null;
        boolean result = expression.containsMatch(it, value);
        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(new Expression[]{new Expression(), new Expression()}) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };
        Iterator lit = null;
        Iterator rit = null;
        boolean result = expression.findMatch(lit, rit);
        Assert.assertFalse(result);
    }
}
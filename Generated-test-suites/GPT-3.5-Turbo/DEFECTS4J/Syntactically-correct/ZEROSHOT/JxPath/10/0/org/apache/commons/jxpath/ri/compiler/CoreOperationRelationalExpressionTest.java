import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new XPathExpression();
        args[1] = new XPathExpression();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new SingleNodePointer());

        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new XPathExpression();
        args[1] = new XPathExpression();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        int precedence = expression.getPrecedence();

        Assert.assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new XPathExpression();
        args[1] = new XPathExpression();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        boolean isSymmetric = expression.isSymmetric();

        Assert.assertEquals(false, isSymmetric);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new XPathExpression();
        args[1] = new XPathExpression();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        Object left = new Object();
        Object right = new Object();

        boolean result = expression.compute(left, right);

        Assert.assertEquals(false, result);
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new XPathExpression();
        args[1] = new XPathExpression();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        Object o = new Object();

        Object result = expression.reduce(o);

        Assert.assertEquals(o, result);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new XPathExpression();
        args[1] = new XPathExpression();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        Iterator it = new ArrayList().iterator();
        Object value = new Object();

        boolean result = expression.containsMatch(it, value);

        Assert.assertEquals(false, result);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new XPathExpression();
        args[1] = new XPathExpression();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return compare >= 0;
            }
        };

        Iterator lit = new ArrayList().iterator();
        Iterator rit = new ArrayList().iterator();

        boolean result = expression.findMatch(lit, rit);

        Assert.assertEquals(false, result);
    }

}
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];

        // Test case 1
        args[0] = new CoreOperationRelationalExpressionTest.CustomExpression();
        args[1] = new CoreOperationRelationalExpressionTest.CustomExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        Assert.assertTrue(expression.computeValue(CoreOperationRelationalExpressionTest.EvalContext.EMPTY));

        // Test case 2
        args[0] = new CoreOperationRelationalExpressionTest.CustomExpression();
        args[1] = new CoreOperationRelationalExpressionTest.AnotherCustomExpression();
        expression = new CoreOperationRelationalExpression(args);
        Assert.assertFalse(expression.computeValue(CoreOperationRelationalExpressionTest.EvalContext.EMPTY));

        // Test case 3
        args[0] = new CoreOperationRelationalExpressionTest.AnotherCustomExpression();
        args[1] = new CoreOperationRelationalExpressionTest.CustomExpression();
        expression = new CoreOperationRelationalExpression(args);
        Assert.assertFalse(expression.computeValue(CoreOperationRelationalExpressionTest.EvalContext.EMPTY));

        // Test case 4
        args[0] = new CoreOperationRelationalExpressionTest.CustomExpression();
        args[1] = new CoreOperationRelationalExpressionTest.CustomExpression();
        expression = new CoreOperationRelationalExpression(args);
        Assert.assertTrue(expression.computeValue(CoreOperationRelationalExpressionTest.EvalContext.EMPTY));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.CustomExpression();
        args[1] = new CoreOperationRelationalExpressionTest.AnotherCustomExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        Assert.assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.CustomExpression();
        args[1] = new CoreOperationRelationalExpressionTest.AnotherCustomExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        Assert.assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.CustomExpression();
        args[1] = new CoreOperationRelationalExpressionTest.AnotherCustomExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        // Test case 1
        Assert.assertTrue(expression.evaluateCompare(0));

        // Test case 2
        Assert.assertFalse(expression.evaluateCompare(1));

        // Test case 3
        Assert.assertFalse(expression.evaluateCompare(-1));
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.CustomExpression();
        args[1] = new CoreOperationRelationalExpressionTest.AnotherCustomExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        // Test case 1
        Assert.assertTrue(expression.compute(CoreOperationRelationalExpressionTest.EvalContext.EMPTY, CoreOperationRelationalExpressionTest.EvalContext.EMPTY));

        // Test case 2
        Assert.assertFalse(expression.compute(CoreOperationRelationalExpressionTest.EvalContext.EMPTY, CoreOperationRelationalExpressionTest.EvalContext.EMPTY));

        // Test case 3
        Assert.assertFalse(expression.compute(CoreOperationRelationalExpressionTest.EvalContext.EMPTY, CoreOperationRelationalExpressionTest.EvalContext.EMPTY));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.CustomExpression();
        args[1] = new CoreOperationRelationalExpressionTest.AnotherCustomExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        Object o = new Object();
        Object result = expression.reduce(o);
        Assert.assertEquals(o, result);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.CustomExpression();
        args[1] = new CoreOperationRelationalExpressionTest.AnotherCustomExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        // Test case 1
        Assert.assertFalse(expression.containsMatch(CoreOperationRelationalExpressionTest.Iterator.EMPTY, new Object()));

        // Test case 2
        Assert.assertFalse(expression.containsMatch(CoreOperationRelationalExpressionTest.Iterator.EMPTY, new Object()));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.CustomExpression();
        args[1] = new CoreOperationRelationalExpressionTest.AnotherCustomExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        // Test case 1
        Assert.assertFalse(expression.findMatch(CoreOperationRelationalExpressionTest.Iterator.EMPTY, CoreOperationRelationalExpressionTest.Iterator.EMPTY));

        // Test case 2
        Assert.assertFalse(expression.findMatch(CoreOperationRelationalExpressionTest.Iterator.EMPTY, CoreOperationRelationalExpressionTest.Iterator.EMPTY));
    }

    private static class CustomExpression extends Expression {
        @Override
        public Object computeValue(EvalContext var1) {
            return null;
        }
    }

    private static class AnotherCustomExpression extends Expression {
        @Override
        public Object computeValue(EvalContext var1) {
            return null;
        }
    }

    private static class EvalContext extends org.apache.commons.jxpath.ri.EvalContext {
        public static final EvalContext EMPTY = new EvalContext();

        @Override
        public Object getSingleNodePointer() {
            return null;
        }
    }

    private static class Iterator implements java.util.Iterator {
        public static final Iterator EMPTY = new Iterator();

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public Object next() {
            return null;
        }
    }
}
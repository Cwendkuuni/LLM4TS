import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.TestExpression();
        args[1] = new CoreOperationRelationalExpressionTest.TestExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        Object result = expression.computeValue(null);
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.TestExpression();
        args[1] = new CoreOperationRelationalExpressionTest.TestExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        int precedence = expression.getPrecedence();
        Assert.assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.TestExpression();
        args[1] = new CoreOperationRelationalExpressionTest.TestExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        boolean isSymmetric = expression.isSymmetric();
        Assert.assertFalse(isSymmetric);
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.TestExpression();
        args[1] = new CoreOperationRelationalExpressionTest.TestExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        boolean evaluateCompare = expression.evaluateCompare(0);
        Assert.assertTrue(evaluateCompare);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.TestExpression();
        args[1] = new CoreOperationRelationalExpressionTest.TestExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        boolean compute = expression.compute(null, null);
        Assert.assertFalse(compute);
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.TestExpression();
        args[1] = new CoreOperationRelationalExpressionTest.TestExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        Object reduce = expression.reduce(null);
        Assert.assertNull(reduce);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.TestExpression();
        args[1] = new CoreOperationRelationalExpressionTest.TestExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        boolean containsMatch = expression.containsMatch(null, null);
        Assert.assertFalse(containsMatch);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new CoreOperationRelationalExpressionTest.TestExpression();
        args[1] = new CoreOperationRelationalExpressionTest.TestExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);
        boolean findMatch = expression.findMatch(null, null);
        Assert.assertFalse(findMatch);
    }

    private static class TestExpression implements Expression {

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }
    }
}
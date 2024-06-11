import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();

        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();

        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        int precedence = expression.getPrecedence();

        Assert.assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();

        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        boolean symmetric = expression.isSymmetric();

        Assert.assertFalse(symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();

        DummyCoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        boolean result = expression.evaluateCompare(0);

        Assert.assertTrue(result);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();

        DummyCoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        boolean result = expression.containsMatch(new DummyIterator(), "value");

        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();

        DummyCoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        boolean result = expression.findMatch(new DummyIterator(), new DummyIterator());

        Assert.assertFalse(result);
    }

    private class DummyCoreOperationRelationalExpression extends CoreOperationRelationalExpression {

        public DummyCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        protected boolean evaluateCompare(int compare) {
            return compare == 0;
        }
    }

    private class DummyExpression extends Expression {

        public Object compute(EvalContext context) {
            return "dummy";
        }
    }

    private class DummyIterator implements Iterator {

        public boolean hasNext() {
            return false;
        }

        public Object next() {
            return null;
        }
    }
}
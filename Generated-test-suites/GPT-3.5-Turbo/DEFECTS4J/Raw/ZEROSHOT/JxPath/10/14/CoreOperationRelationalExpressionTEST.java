import org.junit.Assert;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();

        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new TestSingleNodePointer());

        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();

        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        int precedence = expression.getPrecedence();

        Assert.assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();

        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        boolean symmetric = expression.isSymmetric();

        Assert.assertEquals(false, symmetric);
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();

        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        boolean compare = expression.evaluateCompare(0);

        Assert.assertEquals(true, compare);
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();

        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        Object left = new TestObject();
        Object right = new TestObject();

        boolean compute = expression.compute(left, right);

        Assert.assertEquals(true, compute);
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();

        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        Object o = new TestObject();

        Object reduce = expression.reduce(o);

        Assert.assertEquals(o, reduce);
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();

        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        Iterator it = new TestIterator();
        Object value = new TestObject();

        boolean containsMatch = expression.containsMatch(it, value);

        Assert.assertEquals(false, containsMatch);
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();

        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        Iterator lit = new TestIterator();
        Iterator rit = new TestIterator();

        boolean findMatch = expression.findMatch(lit, rit);

        Assert.assertEquals(false, findMatch);
    }

    private class TestExpression extends Expression {

        public Object computeValue(EvalContext context) {
            return null;
        }

        public int getPrecedence() {
            return 0;
        }

        public boolean isSymmetric() {
            return false;
        }

        public boolean evaluateCompare(int compare) {
            return true;
        }
    }

    private class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {

        public TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        protected boolean evaluateCompare(int compare) {
            return true;
        }
    }

    private class TestSingleNodePointer extends SingleNodePointer {

        public TestSingleNodePointer() {
            super(null, null, 0);
        }

        public Object getValue() {
            return null;
        }
    }

    private class TestObject {

    }

    private class TestIterator implements Iterator {

        public boolean hasNext() {
            return false;
        }

        public Object next() {
            return null;
        }
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        assertEquals(Boolean.TRUE, expression.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        assertTrue(expression.evaluateCompare(0));
        assertFalse(expression.evaluateCompare(-1));
        assertFalse(expression.evaluateCompare(1));
    }

    @Test
    public void testCompute() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        assertTrue(expression.compute(null, null));
    }

    @Test
    public void testReduce() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        assertEquals(null, expression.reduce(null));
    }

    @Test
    public void testContainsMatch() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        assertFalse(expression.containsMatch(null, null));
    }

    @Test
    public void testFindMatch() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        assertFalse(expression.findMatch(null, null));
    }

    private class TestExpression extends Expression {
        public TestExpression() {
            super(null);
        }

        public Object computeValue(EvalContext context) {
            return null;
        }

        protected int getPrecedence() {
            return 0;
        }

        protected boolean isSymmetric() {
            return false;
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
}
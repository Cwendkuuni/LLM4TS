import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CoreOperationRelationalExpressionTest {
    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        assertTrue((Boolean) expression.computeValue(null));
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new DummyExpression();
        args[1] = new DummyExpression();
        CoreOperationRelationalExpression expression = new DummyCoreOperationRelationalExpression(args);

        assertFalse(expression.isSymmetric());
    }

    private class DummyExpression extends Expression {
        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }

        @Override
        public String toString() {
            return null;
        }
    }

    private class DummyCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        public DummyCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return true;
        }
    }
}
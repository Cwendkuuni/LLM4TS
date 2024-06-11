import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {
    
    private CoreOperationRelationalExpression op;

    @Before
    public void setUp() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();
        op = new TestCoreOperationRelationalExpression(args);
    }

    @Test
    public void testComputeValue_returnsTrue_whenComparisonIsSuccessful() {
        EvalContext context = new TestEvalContext();
        Object result = op.computeValue(context);

        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_returnsFalse_whenComparisonFails() {
        EvalContext context = new TestEvalContext();
        Object result = op.computeValue(context);

        assertFalse((Boolean) result);
    }

    @Test
    public void testGetPrecedence_returnsCorrectPrecedence() {
        int precedence = op.getPrecedence();

        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric_returnsFalse() {
        boolean isSymmetric = op.isSymmetric();

        assertFalse(isSymmetric);
    }

    private static class TestExpression extends Expression {
        @Override
        public Object compute(EvalContext context) {
            return null;
        }
    }

    private static class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {
        public TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare < 0;
        }
    }

    private static class TestEvalContext extends EvalContext {
        @Override
        public Object compute(Object obj) {
            return null;
        }
    }
}
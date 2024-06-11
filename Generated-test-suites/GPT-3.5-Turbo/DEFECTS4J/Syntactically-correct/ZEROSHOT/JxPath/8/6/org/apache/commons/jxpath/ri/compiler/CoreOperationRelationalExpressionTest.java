import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationRelationalExpression;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        CoreOperationRelationalExpression coreOperation = new CoreOperationMock(args);

        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);

        assertNotNull(result);
        assertEquals(Boolean.class, result.getClass());
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        CoreOperationRelationalExpression coreOperation = new CoreOperationMock(args);

        int precedence = coreOperation.getPrecedence();

        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        CoreOperationRelationalExpression coreOperation = new CoreOperationMock(args);

        boolean symmetric = coreOperation.isSymmetric();

        assertFalse(symmetric);
    }

    private class CoreOperationMock extends CoreOperationRelationalExpression {

        protected CoreOperationMock(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return false;
        }
    }

    private class MockExpression extends Expression {

        @Override
        public Object computeValue(EvalContext context) {
            return null;
        }

        @Override
        protected int getPrecedence() {
            return 0;
        }
    }
}
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new TestExpression();
        args[1] = new TestExpression();

        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext();
        context.setSingleNodePointer(new TestPointer(1));

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression();

        assertEquals(3, expression.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression();

        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testEvaluateCompare() {
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression();

        boolean result = expression.evaluateCompare(0);

        assertTrue(result);
    }

    @Test
    public void testCompute() {
        CoreOperationRelationalExpression expression = new TestCoreOperationRelationalExpression();

        boolean result = expression.compute(2, 2);

        assertTrue(result);
    }

    private class TestExpression extends Expression {

        @Override
        public Object computeValue(EvalContext context) {
            return new TestPointer(2);
        }

    }

    private class TestCoreOperationRelationalExpression extends CoreOperationRelationalExpression {

        public TestCoreOperationRelationalExpression(Expression[] args) {
            super(args);
        }

        public TestCoreOperationRelationalExpression() {
            super(new Expression[0]);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return true;
        }
    }

    private class TestPointer extends InitialContext {

        private Object value;

        public TestPointer(Object value) {
            this.value = value;
        }

        @Override
        public Object getNode() {
            return value;
        }

    }
}
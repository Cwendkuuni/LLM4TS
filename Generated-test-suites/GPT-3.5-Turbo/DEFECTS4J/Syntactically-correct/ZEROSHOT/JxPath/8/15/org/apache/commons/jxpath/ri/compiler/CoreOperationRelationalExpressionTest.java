import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpressionStub(null);
    }

    @Test
    public void testComputeValue_returnsTrue() {
        EvalContext context = new EvalContextStub();
        context.setValue(true);

        Object result = expression.computeValue(context);

        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue_returnsFalse() {
        EvalContext context = new EvalContextStub();
        context.setValue(false);

        Object result = expression.computeValue(context);

        assertFalse((boolean) result);
    }

    private class CoreOperationRelationalExpressionStub extends CoreOperationRelationalExpression {

        protected CoreOperationRelationalExpressionStub(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return false;
        }
    }

    private class EvalContextStub extends EvalContext {

        private Object value;

        public void setValue(Object value) {
            this.value = value;
        }

        @Override
        public Object computeValue(Expression expression) {
            return value;
        }
    }
}
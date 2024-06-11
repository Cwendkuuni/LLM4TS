import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue_GreaterThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(new Expression[2]);
        EvalContext context = new EvalContext();
        
        expression.args[0] = new ExpressionMock();
        expression.args[1] = new ExpressionMock();
        
        // Test when left object is greater than right object
        expression.args[0].value = 5;
        expression.args[1].value = 2;
        assertEquals(Boolean.TRUE, expression.computeValue(context));

        // Test when left object is equal to right object
        expression.args[0].value = 2;
        expression.args[1].value = 2;
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        // Test when left object is less than right object
        expression.args[0].value = 1;
        expression.args[1].value = 2;
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testComputeValue_GreaterThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(new Expression[2]);
        EvalContext context = new EvalContext();

        expression.args[0] = new ExpressionMock();
        expression.args[1] = new ExpressionMock();

        // Test when left object is greater than right object
        expression.args[0].value = 5;
        expression.args[1].value = 2;
        assertEquals(Boolean.TRUE, expression.computeValue(context));

        // Test when left object is equal to right object
        expression.args[0].value = 2;
        expression.args[1].value = 2;
        assertEquals(Boolean.TRUE, expression.computeValue(context));

        // Test when left object is less than right object
        expression.args[0].value = 1;
        expression.args[1].value = 2;
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testComputeValue_LessThan() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(new Expression[2]);
        EvalContext context = new EvalContext();

        expression.args[0] = new ExpressionMock();
        expression.args[1] = new ExpressionMock();

        // Test when left object is greater than right object
        expression.args[0].value = 5;
        expression.args[1].value = 2;
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        // Test when left object is equal to right object
        expression.args[0].value = 2;
        expression.args[1].value = 2;
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        // Test when left object is less than right object
        expression.args[0].value = 1;
        expression.args[1].value = 2;
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testComputeValue_LessThanOrEqual() {
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpressionMock(new Expression[2]);
        EvalContext context = new EvalContext();

        expression.args[0] = new ExpressionMock();
        expression.args[1] = new ExpressionMock();

        // Test when left object is greater than right object
        expression.args[0].value = 5;
        expression.args[1].value = 2;
        assertEquals(Boolean.FALSE, expression.computeValue(context));

        // Test when left object is equal to right object
        expression.args[0].value = 2;
        expression.args[1].value = 2;
        assertEquals(Boolean.TRUE, expression.computeValue(context));

        // Test when left object is less than right object
        expression.args[0].value = 1;
        expression.args[1].value = 2;
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    private class ExpressionMock extends Expression {
        private Object value;

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }

        @Override
        protected int getPrecedence() {
            return 0;
        }

        @Override
        protected boolean isSymmetric() {
            return false;
        }
    }

    private class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {

        public CoreOperationRelationalExpressionMock(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return false;
        }
    }
}
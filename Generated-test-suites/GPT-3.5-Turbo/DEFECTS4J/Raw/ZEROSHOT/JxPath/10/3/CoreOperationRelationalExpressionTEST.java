import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        EvalContext context = new EvalContext();
        Object value = expression.computeValue(context);

        assertEquals(Boolean.FALSE, value);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();

        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return false;
            }
        };

        int precedence = expression.getPrecedence();

        assertEquals(3, precedence);
    }
}

class MockExpression extends Expression {

    @Override
    public Object computeValue(EvalContext context) {
        return null;
    }
}
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;
    private EvalContext context;

    @Before
    public void setup() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        expression = new CoreOperationRelationalExpression(args) {
            protected boolean evaluateCompare(int compare) {
                return compare > 0;
            }
        };
        context = new EvalContext();
    }

    @Test
    public void testComputeValue_shouldReturnTrue() {
        Object result = expression.computeValue(context);
        assertEquals(true, result);
    }

    @Test
    public void testComputeValue_shouldReturnFalse() {
        expression = new CoreOperationRelationalExpression(new Expression[2]) {
            protected boolean evaluateCompare(int compare) {
                return compare <= 0;
            }
        };
        Object result = expression.computeValue(context);
        assertEquals(false, result);
    }

    @Test
    public void testGetPrecedence_shouldReturn3() {
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric_shouldReturnFalse() {
        boolean symmetric = expression.isSymmetric();
        assertEquals(false, symmetric);
    }

    private class MockExpression extends Expression {
        public Object computeValue(EvalContext context) {
            return null;
        }
    }
}
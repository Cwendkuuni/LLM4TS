import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new CoreVariable(1);
        args[1] = new CoreVariable(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new CoreVariable(1);
        args[1] = new CoreVariable(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };

        int precedence = expression.getPrecedence();

        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new CoreVariable(1);
        args[1] = new CoreVariable(2);
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args) {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };

        boolean isSymmetric = expression.isSymmetric();

        assertEquals(false, isSymmetric);
    }

}
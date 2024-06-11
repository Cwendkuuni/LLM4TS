import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpression() {
            @Override
            protected boolean evaluateCompare(int compare) {
                return true;
            }
        };
    }

    @Test
    public void testComputeValue() {
        // Test case 1
        EvalContext context1 = createContext();
        Object result1 = expression.computeValue(context1);
        assertEquals(Boolean.TRUE, result1);

        // Test case 2
        EvalContext context2 = createContext();
        Object result2 = expression.computeValue(context2);
        assertEquals(Boolean.FALSE, result2);
    }

    @Test
    public void testGetPrecedence() {
        int precedence = expression.getPrecedence();
        assertEquals(CoreOperation.RELATIONAL_EXPR_PRECEDENCE, precedence);
    }

    @Test
    public void testIsSymmetric() {
        boolean symmetric = expression.isSymmetric();
        assertEquals(false, symmetric);
    }

    private EvalContext createContext() {
        // Create your own implementation of EvalContext for testing purposes
        return null;
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);

        assertNotNull(result);
        assertTrue(result instanceof Boolean);
    }

    @Test
    public void testGetPrecedence() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        int precedence = expression.getPrecedence();

        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        Expression[] args = new Expression[2];
        args[0] = new MockExpression();
        args[1] = new MockExpression();
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        boolean isSymmetric = expression.isSymmetric();

        assertFalse(isSymmetric);
    }

    // Add more test methods to cover all methods in CoreOperationRelationalExpression class

}
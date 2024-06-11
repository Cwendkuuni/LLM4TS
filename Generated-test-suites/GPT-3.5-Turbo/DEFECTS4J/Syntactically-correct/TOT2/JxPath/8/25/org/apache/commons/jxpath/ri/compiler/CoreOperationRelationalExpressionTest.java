import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Typical case where arguments evaluate to true
        Expression[] args = new Expression[]{new Literal(5), new Literal(3)};
        CoreOperationRelationalExpression expression = new GreaterThan(args);
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);

        // Edge case where arguments evaluate to false
        args = new Expression[]{new Literal(2), new Literal(2)};
        expression = new GreaterThan(args);
        context = new EvalContext();
        result = expression.computeValue(context);
        assertFalse((Boolean) result);

        // Error scenario where one argument is null
        args = new Expression[]{new Literal(5), null};
        expression = new GreaterThan(args);
        context = new EvalContext();
        try {
            result = expression.computeValue(context);
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // as expected
        }
    }

    @Test
    public void testGetPrecedence() {
        // Typical case where precedence is 3
        Expression[] args = new Expression[]{new Literal(5), new Literal(3)};
        CoreOperationRelationalExpression expression = new GreaterThan(args);
        int precedence = expression.getPrecedence();
        assertEquals(3, precedence);
    }

    @Test
    public void testIsSymmetric() {
        // Typical case where isSymmetric returns false
        Expression[] args = new Expression[]{new Literal(5), new Literal(3)};
        CoreOperationRelationalExpression expression = new GreaterThan(args);
        assertFalse(expression.isSymmetric());
    }

    // Additional test cases for other methods in CoreOperationRelationalExpression class can be added here

}
import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test typical values
        Expression[] args = {new Constant(5), new Constant(2)};
        CoreOperationRelationalExpression expression = new LessThan(args);
        EvalContext context = new EvalContext(new Object());
        assertEquals(Boolean.FALSE, expression.computeValue(context));
        
        // Test edge case where left is equal to right
        Expression[] argsEqual = {new Constant(5), new Constant(5)};
        CoreOperationRelationalExpression expressionEqual = new LessThan(argsEqual);
        assertEquals(Boolean.FALSE, expressionEqual.computeValue(context));
        
        // Test error scenario with non-numeric values
        Expression[] argsNonNumeric = {new Constant("abc"), new Constant("def")};
        CoreOperationRelationalExpression expressionNonNumeric = new LessThan(argsNonNumeric);
        assertEquals(Boolean.FALSE, expressionNonNumeric.computeValue(context));
        
        // Further test cases can be added to cover more scenarios
    }

    @Test
    public void testIsSymmetric() {
        // Test the isSymmetric method
        Expression[] args = {new Constant(5), new Constant(2)};
        CoreOperationRelationalExpression expression = new LessThan(args);
        assertFalse(expression.isSymmetric());
    }

    @Test
    public void testGetPrecedence() {
        // Test the getPrecedence method
        Expression[] args = {new Constant(5), new Constant(2)};
        CoreOperationRelationalExpression expression = new LessThan(args);
        assertEquals(3, expression.getPrecedence());
    }

    // Additional test cases for other methods can be added here 

}
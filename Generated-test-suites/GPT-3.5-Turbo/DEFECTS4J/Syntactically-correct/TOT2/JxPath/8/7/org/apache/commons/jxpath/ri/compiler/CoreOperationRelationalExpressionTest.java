import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test basic functionality
        // Define test input
        TestEvalContext context = new TestEvalContext();
        TestExpression[] args = {new TestExpression(), new TestExpression()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        // Set up context with values
        context.setComputedValues(new Object[]{5, 10});

        // Compute the result
        Object result = expression.computeValue(context);

        // Verify the result
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testEdgeCases() {
        // Test for edge cases
        // Define test input
        TestEvalContext context = new TestEvalContext();
        TestExpression[] args = {new TestExpression(), new TestExpression()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        // Set up context with edge case values
        context.setComputedValues(new Object[]{null, 0});

        // Compute the result
        Object result = expression.computeValue(context);

        // Verify the result
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testExceptionHandling() {
        // Test exception handling
        // Define test input
        TestEvalContext context = new TestEvalContext();
        TestExpression[] args = {new TestExpression(), new TestExpression()};
        CoreOperationRelationalExpression expression = new CoreOperationRelationalExpression(args);

        // Set up context with exception values
        context.setComputedValues(new Object[]{"string", 10});

        // Compute the result
        try {
            Object result = expression.computeValue(context);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            // Success
            assertTrue(true);
        }
    }

    @Test
    public void testAdditionalCases() {
        // Add more test cases here for comprehensive testing
    }
}
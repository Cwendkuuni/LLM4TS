import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    // Basic test case for computeValue(EvalContext context)
    @Test
    public void testComputeValue() {
        // Create a valid context and test if the expected output is returned
    }

    // Test case for computeValue(EvalContext context) with null context
    @Test(expected = NullPointerException.class)
    public void testComputeValueWithNullContext() {
        // Call computeValue method with null context and check if the expected exception is thrown
    }

    // Test case for computeValue(EvalContext context) with an invalid context
    @Test(expected = InvalidContextException.class)
    public void testComputeValueWithInvalidContext() {
        // Call computeValue method with invalid context and check if the expected exception is thrown
    }

    // Test case for computeValue(EvalContext context) with invalid input values
    @Test(expected = IllegalArgumentException.class)
    public void testComputeValueWithInvalidInput() {
        // Call computeValue method with invalid input values and check if the expected exception is thrown
    }

    // 
    // All the individual test cases go here
    // 
}
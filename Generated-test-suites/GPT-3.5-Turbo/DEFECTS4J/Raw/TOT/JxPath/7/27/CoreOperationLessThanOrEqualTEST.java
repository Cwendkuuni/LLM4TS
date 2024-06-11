import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue_TypicalCase() {
        // Create a CoreOperationLessThanOrEqual instance with arg1 = 5 and arg2 = 10
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock(5), new ExpressionMock(10)); 

        // Create a mock EvalContext
        EvalContext context = new EvalContextMock();

        // Compute the value
        Object result = operation.computeValue(context);

        // Assert that the result is false
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_LessThan_Success() {
        // Create a CoreOperationLessThanOrEqual instance with arg1 = 5 and arg2 = 5
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock(5), new ExpressionMock(5)); 

        // Create a mock EvalContext
        EvalContext context = new EvalContextMock();

        // Compute the value
        Object result = operation.computeValue(context);

        // Assert that the result is true
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_LessThan_Failure() {
        // Create a CoreOperationLessThanOrEqual instance with arg1 = 10 and arg2 = 5
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock(10), new ExpressionMock(5)); 

        // Create a mock EvalContext
        EvalContext context = new EvalContextMock();

        // Compute the value
        Object result = operation.computeValue(context);

        // Assert that the result is false
        assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        // Create a CoreOperationLessThanOrEqual instance with any arguments
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock(0), new ExpressionMock(0)); 

        // Assert that the symbol is "<="
        assertEquals("<=", operation.getSymbol());
    }

    // Additional test cases for other methods in the class can be added here

}
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_typicalCase() {
        // Create the Expression arguments
        Expression arg1 = new TestExpression("test1");
        Expression arg2 = new TestExpression("test2");
        // Create the CoreOperationEqual object
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        // Create the EvalContext
        EvalContext context = new TestEvalContext();
        // Compute the value
        Object result = operation.computeValue(context);
        // Verify the result
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_bothArgumentsNull() {
        // Create the CoreOperationEqual object
        CoreOperationEqual operation = new CoreOperationEqual(null, null);
        // Create the EvalContext
        EvalContext context = new TestEvalContext();
        // Compute the value
        Object result = operation.computeValue(context);
        // Verify the result
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_firstArgumentNull() {
        // Create the Expression arguments
        Expression arg1 = null;
        Expression arg2 = new TestExpression("test");
        // Create the CoreOperationEqual object
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        // Create the EvalContext
        EvalContext context = new TestEvalContext();
        // Compute the value
        Object result = operation.computeValue(context);
        // Verify the result
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_secondArgumentNull() {
        // Create the Expression arguments
        Expression arg1 = new TestExpression("test");
        Expression arg2 = null;
        // Create the CoreOperationEqual object
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        // Create the EvalContext
        EvalContext context = new TestEvalContext();
        // Compute the value
        Object result = operation.computeValue(context);
        // Verify the result
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        // Create the Expression arguments
        Expression arg1 = new TestExpression("test1");
        Expression arg2 = new TestExpression("test2");
        // Create the CoreOperationEqual object
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        // Get the symbol
        String symbol = operation.getSymbol();
        // Verify the symbol
        assertEquals("=", symbol);
    }
    
    // Additional test cases can be added for edge cases and error scenarios

}
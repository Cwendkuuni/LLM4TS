import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testConstructor() {
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        assertNotNull(operation);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new DummyEvalContext();
        Expression arg1 = new ConstantExpression(5);
        Expression arg2 = new ConstantExpression(10);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        assertTrue((Boolean) operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new DummyExpression();
        Expression arg2 = new DummyExpression();
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        assertEquals("<", operation.getSymbol());
    }

    // Add test cases for edge cases and error scenarios here

    // Example of edge case testing
    @Test
    public void testComputeValueEdgeCase() {
        EvalContext context = new DummyEvalContext();
        Expression arg1 = new ConstantExpression(10);
        Expression arg2 = new ConstantExpression(5);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        assertFalse((Boolean) operation.computeValue(context));
    }

    // Example of exception handling testing
    @Test(expected = NullPointerException.class)
    public void testNullEvalContext() {
        Expression arg1 = new ConstantExpression(5);
        Expression arg2 = new ConstantExpression(10);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);
        operation.computeValue(null); // This should throw a NullPointerException
    }
}

class DummyExpression implements Expression {
    // Dummy implementation for testing purposes
}

class DummyEvalContext implements EvalContext {
    // Dummy implementation for testing purposes
}
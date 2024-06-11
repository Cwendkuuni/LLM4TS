import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Test with arg1 less than arg2
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock(5), new ExpressionMock(10));
        EvalContext context = new EvalContext();
        assertTrue((Boolean) operation.computeValue(context));

        // Test with arg1 equal to arg2
        operation = new CoreOperationLessThanOrEqual(new ExpressionMock(5), new ExpressionMock(5));
        assertTrue((Boolean) operation.computeValue(context));

        // Test with arg1 greater than arg2
        operation = new CoreOperationLessThanOrEqual(new ExpressionMock(10), new ExpressionMock(5));
        assertFalse((Boolean) operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock(5), new ExpressionMock(10));
        assertEquals("<=", operation.getSymbol());
    }

    // Additional test cases for potential edge cases and exception handling scenarios

    @Test
    public void testComputeValueWithNullArguments() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock(null), new ExpressionMock(10));
        EvalContext context = new EvalContext();
        try {
            operation.computeValue(context);
            fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Expected exception to be thrown
        }
    }

    @Test
    public void testComputeValueWithUnexpectedValueType() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new ExpressionMock("5"), new ExpressionMock(10));
        EvalContext context = new EvalContext();
        try {
            operation.computeValue(context);
            fail("Expected ClassCastException to be thrown");
        } catch (ClassCastException e) {
            // Expected exception to be thrown
        }
    }

    // Mock class for Expression
    private static class ExpressionMock extends Expression {
        private Object value;

        public ExpressionMock(Object value) {
            this.value = value;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}


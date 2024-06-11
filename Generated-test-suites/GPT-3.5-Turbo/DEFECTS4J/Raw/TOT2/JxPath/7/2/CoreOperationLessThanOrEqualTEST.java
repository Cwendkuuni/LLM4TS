import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(
                new MockExpression(5), new MockExpression(10));

        EvalContext context = new MockEvalContext();
        Object result = coreOperation.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(
                new MockExpression(5), new MockExpression(10));

        assertEquals("<=", coreOperation.getSymbol());
    }

    @Test
    public void testComputeValueWithNullContext() {
        CoreOperationLessThanOrEqual coreOperation = new CoreOperationLessThanOrEqual(
                new MockExpression(null), new MockExpression(10));

        EvalContext context = null;
        try {
            coreOperation.computeValue(context);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // test passed
        }
    }

    // Add more test cases as needed...

    // Mock classes for testing
    private class MockExpression extends Expression {
        private Object value;

        public MockExpression(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }

    private class MockEvalContext implements EvalContext {
        // Implement necessary methods for testing
    }
}
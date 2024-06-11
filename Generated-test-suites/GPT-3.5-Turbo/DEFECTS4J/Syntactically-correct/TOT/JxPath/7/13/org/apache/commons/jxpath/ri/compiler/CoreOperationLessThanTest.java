import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    private CoreOperationLessThan operation;

    @Before
    public void setUp() {
        Expression arg1 = new MockExpression(5);
        Expression arg2 = new MockExpression(10);
        operation = new CoreOperationLessThan(arg1, arg2);
    }

    @Test
    public void testComputeValue() {
        EvalContext context = new MockEvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }

    @Test
    public void testComputeValueWithDoubleArgs() {
        Expression arg1 = new MockExpression(5.5);
        Expression arg2 = new MockExpression(10.2);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new MockEvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValueWithNegativeArgs() {
        Expression arg1 = new MockExpression(-5);
        Expression arg2 = new MockExpression(-10);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new MockEvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithEqualArgs() {
        Expression arg1 = new MockExpression(5);
        Expression arg2 = new MockExpression(5);
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new MockEvalContext();
        Object result = operation.computeValue(context);
        assertTrue(result instanceof Boolean);
        // When args are equal, return value should be false
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueWithErrorArgs() {
        Expression arg1 = new MockExpression("5");
        Expression arg2 = new MockExpression("10");
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new MockEvalContext();
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    @Test
    public void testComputeValueWithNullArgs() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationLessThan operation = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new MockEvalContext();
        Object result = operation.computeValue(context);
        assertNull(result);
    }

    // Helper class to mock Expression and EvalContext
    private class MockExpression extends Expression {
        private Object value;

        public MockExpression(Object value) {
            this.value = value;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }
    }

    private class MockEvalContext implements EvalContext {
        // Implement required methods for mocking EvalContext
    }
}
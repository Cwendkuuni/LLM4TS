import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new MockExpression(5), new MockExpression(10));
        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);
        assertTrue((Boolean) result);

        CoreOperationLessThanOrEqual operation2 = new CoreOperationLessThanOrEqual(new MockExpression(10), new MockExpression(5));
        EvalContext context2 = new EvalContext();
        Object result2 = operation2.computeValue(context2);
        assertFalse((Boolean) result2);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(new MockExpression(5), new MockExpression(10));
        assertEquals("<=", operation.getSymbol());
    }

    // Additional test cases for edge cases and exceptions can be added here

    // Mock Expression class for testing purposes
    private static class MockExpression extends Expression {
        private Object value;

        public MockExpression(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}
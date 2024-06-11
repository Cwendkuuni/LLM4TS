import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new MockExpression(5), new MockExpression(3));
        assertTrue((Boolean) greaterThanOrEqual.computeValue(null));

        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new MockExpression(3), new MockExpression(5));
        assertFalse((Boolean) greaterThanOrEqual.computeValue(null));

        // Edge cases
        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new MockExpression(3), new MockExpression(3));
        assertTrue((Boolean) greaterThanOrEqual.computeValue(null));

        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new MockExpression(Double.MAX_VALUE), new MockExpression(Double.MIN_VALUE));
        assertTrue((Boolean) greaterThanOrEqual.computeValue(null));

        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new MockExpression(Double.NEGATIVE_INFINITY), new MockExpression(Double.POSITIVE_INFINITY));
        assertFalse((Boolean) greaterThanOrEqual.computeValue(null));

        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new MockExpression(Double.NaN), new MockExpression(10));
        assertFalse((Boolean) greaterThanOrEqual.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new MockExpression(5), new MockExpression(3));
        assertEquals(">=", greaterThanOrEqual.getSymbol());
    }

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
}
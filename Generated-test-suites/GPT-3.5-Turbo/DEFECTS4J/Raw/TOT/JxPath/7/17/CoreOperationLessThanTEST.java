import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue_typicalCase() {
        // Typical case: arg1 = 5, arg2 = 10
        CoreOperationLessThan expression = new CoreOperationLessThan(new ExpressionMock(5), new ExpressionMock(10));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_sameValue() {
        // Edge case: arg1 = 5, arg2 = 5
        CoreOperationLessThan expression = new CoreOperationLessThan(new ExpressionMock(5), new ExpressionMock(5));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_negativeValues() {
        // Edge case: arg1 = -10, arg2 = -5
        CoreOperationLessThan expression = new CoreOperationLessThan(new ExpressionMock(-10), new ExpressionMock(-5));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_decimalValues() {
        // Edge case: arg1 = 5.5, arg2 = 5.6
        CoreOperationLessThan expression = new CoreOperationLessThan(new ExpressionMock(5.5), new ExpressionMock(5.6));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertTrue((Boolean) result);
    }

    @Test(expected = NullPointerException.class)
    public void testComputeValue_nullValue() {
        // Error scenario: arg1 = null, arg2 = 5
        CoreOperationLessThan expression = new CoreOperationLessThan(new ExpressionMock(null), new ExpressionMock(5));
        EvalContext context = new EvalContext();
        expression.computeValue(context);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan expression = new CoreOperationLessThan(new ExpressionMock(5), new ExpressionMock(10));
        assertEquals("<", expression.getSymbol());
    }

    // Helper class to mock Expression
    private class ExpressionMock extends Expression {
        private Object value;

        public ExpressionMock(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }
    }

}
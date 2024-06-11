import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_typical() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new ExpressionMock(5), new ExpressionMock(3));

        EvalContext contextMock = new EvalContext();
        Object result = operation.computeValue(contextMock);

        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_edge() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new ExpressionMock(Double.MAX_VALUE), new ExpressionMock(Double.MAX_VALUE));

        EvalContext contextMock = new EvalContext();
        Object result = operation.computeValue(contextMock);

        Assert.assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_error() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new ExpressionMock("5"), new ExpressionMock(3));

        EvalContext contextMock = new EvalContext();
        Object result = operation.computeValue(contextMock);

        Assert.assertFalse((Boolean) result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(
                new ExpressionMock(5), new ExpressionMock(3));

        String symbol = operation.getSymbol();

        Assert.assertEquals(">=", symbol);
    }

    private class ExpressionMock extends Expression {
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
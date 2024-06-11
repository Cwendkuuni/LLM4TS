import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThan;
import org.apache.commons.jxpath.ri.compiler.Expression;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        // Test case 1: Check if left argument is greater than right argument
        Expression arg1 = new ExpressionMock(5);
        Expression arg2 = new ExpressionMock(2);
        CoreOperationGreaterThan operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        EvalContext context = new EvalContext();

        Object result = operationGreaterThan.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);

        // Test case 2: Check if left argument is less than right argument
        arg1 = new ExpressionMock(2);
        arg2 = new ExpressionMock(5);
        operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();

        result = operationGreaterThan.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);

        // Test case 3: Check if left argument is equal to right argument
        arg1 = new ExpressionMock(5);
        arg2 = new ExpressionMock(5);
        operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);
        context = new EvalContext();

        result = operationGreaterThan.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new ExpressionMock(5);
        Expression arg2 = new ExpressionMock(2);
        CoreOperationGreaterThan operationGreaterThan = new CoreOperationGreaterThan(arg1, arg2);

        String symbol = operationGreaterThan.getSymbol();

        Assert.assertEquals(">", symbol);
    }

    private class ExpressionMock implements Expression {
        private final Object value;

        public ExpressionMock(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}
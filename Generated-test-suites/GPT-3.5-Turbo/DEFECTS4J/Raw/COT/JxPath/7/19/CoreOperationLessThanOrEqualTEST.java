
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
            new ExpressionMock(10),
            new ExpressionMock(20)
        );

        EvalContext context = new EvalContextMock();
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueEqual() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
            new ExpressionMock(10),
            new ExpressionMock(10)
        );

        EvalContext context = new EvalContextMock();
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueGreaterThan() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
            new ExpressionMock(20),
            new ExpressionMock(10)
        );

        EvalContext context = new EvalContextMock();
        Object result = expression.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(
            new ExpressionMock(10),
            new ExpressionMock(20)
        );

        String symbol = expression.getSymbol();

        Assert.assertEquals("<=", symbol);
    }

    private class ExpressionMock extends Expression {
        private final Object value;

        public ExpressionMock(Object value) {
            this.value = value;
        }
        
        public Object computeValue(EvalContext context) {
            return value;
        }
    }

    private class EvalContextMock extends EvalContext {
        public EvalContextMock() {
            super(null);
        }
    }
}

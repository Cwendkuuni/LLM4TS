
import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValueReturnsTrueWhenArg1IsGreaterThanOrEqualToArg2() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ExpressionMock(5), new ExpressionMock(3));
        EvalContext context = new EvalContextMock();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValueReturnsFalseWhenArg1IsLessThanArg2() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ExpressionMock(3), new ExpressionMock(5));
        EvalContext context = new EvalContextMock();
        assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testComputeValueReturnsTrueWhenArg1IsEqualToArg2() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ExpressionMock(5), new ExpressionMock(5));
        EvalContext context = new EvalContextMock();
        assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbolReturnsGreaterThanOrEqualSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(new ExpressionMock(5), new ExpressionMock(3));
        assertEquals(">=", operation.getSymbol());
    }

    private static class ExpressionMock extends Expression {
        private Object value;

        public ExpressionMock(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }

        public Expression[] getChildren() {
            return null;
        }
    }

    private static class EvalContextMock extends EvalContext {
        public Object getValue(String xpath) {
            return null;
        }
    }
}


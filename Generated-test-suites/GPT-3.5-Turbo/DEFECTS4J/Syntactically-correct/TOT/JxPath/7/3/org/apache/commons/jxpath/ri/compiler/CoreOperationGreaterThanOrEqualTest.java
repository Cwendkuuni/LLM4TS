import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_typicalCase() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ExpressionStub(5), new ExpressionStub(3));

        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_edgeCase() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ExpressionStub(Double.MAX_VALUE), new ExpressionStub(Double.MAX_VALUE));

        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_edgeCase2() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ExpressionStub(Double.MIN_VALUE), new ExpressionStub(Double.MIN_VALUE));

        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_errorCase() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(
                new ExpressionStub("abc"), new ExpressionStub("def"));

        EvalContext context = new EvalContext();

        Object result = expression.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    private static class ExpressionStub extends Expression {

        private Object value;

        public ExpressionStub(Object value) {
            this.value = value;
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }

        @Override
        public String getSymbol() {
            return "";
        }
    }
}
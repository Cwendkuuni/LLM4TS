
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;

import static org.junit.Assert.*;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue_typicalCase() {
        Expression arg1 = new ExpressionStub(5);
        Expression arg2 = new ExpressionStub(7);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContextStub();

        Object result = operation.computeValue(context);

        assertNotNull(result);
        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue_EqualValues() {
        Expression arg1 = new ExpressionStub(7);
        Expression arg2 = new ExpressionStub(7);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContextStub();

        Object result = operation.computeValue(context);

        assertNotNull(result);
        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue_GreaterValue() {
        Expression arg1 = new ExpressionStub(9);
        Expression arg2 = new ExpressionStub(7);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContextStub();

        Object result = operation.computeValue(context);

        assertNotNull(result);
        assertFalse((boolean) result);
    }

    @Test
    public void testComputeValue_NegativeValues() {
        Expression arg1 = new ExpressionStub(-5);
        Expression arg2 = new ExpressionStub(-7);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContextStub();

        Object result = operation.computeValue(context);

        assertNotNull(result);
        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue_EdgeCases() {
        Expression arg1 = new ExpressionStub(Double.MIN_VALUE);
        Expression arg2 = new ExpressionStub(Double.MIN_VALUE);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContextStub();

        Object result = operation.computeValue(context);
        
        assertNotNull(result);
        assertTrue((boolean) result);
    }

    @Test
    public void testComputeValue_ErrorScenario() {
        Expression arg1 = new ExpressionStub("invalid");
        Expression arg2 = new ExpressionStub(7);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContextStub();

        try {
            operation.computeValue(context);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Cannot convert argument to double: invalid", e.getMessage());
        }
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
    }

    private static class EvalContextStub extends EvalContext {
        @Override
        public double doubleValue(Object object) {
            if (object instanceof Number) {
                return ((Number) object).doubleValue();
            }
            throw new IllegalArgumentException("Cannot convert argument to double: " + object);
        }
    }
}

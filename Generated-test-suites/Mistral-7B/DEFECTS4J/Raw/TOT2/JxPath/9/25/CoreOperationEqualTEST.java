import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.expression.Expression;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationEqualTest {

    @Test
    public void testEqual_BothNull() {
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result.equals(Boolean.FALSE));
    }

    @Test
    public void testEqual_BothSameValue() {
        Expression arg1 = new LiteralExpression(5);
        Expression arg2 = new LiteralExpression(5);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testEqual_DifferentValues() {
        Expression arg1 = new LiteralExpression(5);
        Expression arg2 = new LiteralExpression(10);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result.equals(Boolean.FALSE));
    }

    @Test
    public void testEqual_NullAndValue() {
        Expression arg1 = null;
        Expression arg2 = new LiteralExpression(5);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result.equals(Boolean.FALSE));
    }

    @Test
    public void testEqual_ValueAndNull() {
        Expression arg1 = new LiteralExpression(5);
        Expression arg2 = null;
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result.equals(Boolean.FALSE));
    }

    @Test
    public void testEqual_BothNaN() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(Double.NaN);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testEqual_NaNAndValue() {
        Expression arg1 = new LiteralExpression(Double.NaN);
        Expression arg2 = new LiteralExpression(5);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result.equals(Boolean.FALSE));
    }

    @Test
    public void testEqual_ValueAndNaN() {
        Expression arg1 = new LiteralExpression(5);
        Expression arg2 = new LiteralExpression(Double.NaN);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(new EvalContext());
        assertTrue(result.equals(Boolean.FALSE));
    }

    @Test
    public void testComputeValue_BothNull() {
        EvalContext context = new EvalContext();
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result.equals(Boolean.FALSE));
    }

    @Test
    public void testComputeValue_BothSameValue() {
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(5);
        Expression arg2 = new LiteralExpression(5);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test
    public void testComputeValue_DifferentValues() {
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(5);
        Expression arg2 = new LiteralExpression(10);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result.equals(Boolean.FALSE));
    }

    @Test
    public void testComputeValue_MixedTypes() {
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(5);
        Expression arg2 = new LiteralExpression("5");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result.equals(Boolean.TRUE));
    }

    @Test(expected = RuntimeException.class)
    public void testComputeValue_TypeMismatch() {
        EvalContext context = new EvalContext();
        Expression arg1 = new LiteralExpression(5);
        Expression arg2 = new LiteralExpression(true);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        operation.computeValue(context); // should throw a RuntimeException
    }

    @Test
    public void testComputeValue_NullAndNull_WithCustomEvalContext() {
        EvalContext context = new EvalContext() {
            @Override
            public Boolean equal(Object x, Object y) {
                return x == y;
            }
        };
        Expression arg1 = null;
        Expression arg2 = null;
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        Object result = operation.computeValue(context);
        assertTrue(result.equals(Boolean.FALSE));
    }
}
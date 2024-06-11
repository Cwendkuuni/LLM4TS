
import org.apache.commons.jxpath.Expression;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValueWithEqualStrings() {
        Expression arg1 = new DummyExpression("test");
        Expression arg2 = new DummyExpression("test");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithNotEqualStrings() {
        Expression arg1 = new DummyExpression("test");
        Expression arg2 = new DummyExpression("not_test");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithEqualIntegers() {
        Expression arg1 = new DummyExpression(5);
        Expression arg2 = new DummyExpression(5);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueWithNotEqualIntegers() {
        Expression arg1 = new DummyExpression(5);
        Expression arg2 = new DummyExpression(10);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithMixedTypes() {
        Expression arg1 = new DummyExpression("5");
        Expression arg2 = new DummyExpression(5);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithNullArg1() {
        Expression arg1 = new DummyExpression(null);
        Expression arg2 = new DummyExpression(5);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValueWithNullArg2() {
        Expression arg1 = new DummyExpression(5);
        Expression arg2 = new DummyExpression(null);
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        Object result = operation.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    class DummyExpression implements Expression {

        private Object value;

        public DummyExpression(Object value) {
            this.value = value;
        }

        @Override
        public void reset() {
        }

        @Override
        public Object computeValue(EvalContext context) {
            return value;
        }

        @Override
        public String toString() {
            return value.toString();
        }
    }
}


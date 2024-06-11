import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue_BothArgsPositive() {
        Expression arg1 = new ExpressionStub(4);
        Expression arg2 = new ExpressionStub(6);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(null));
    }

    @Test
    public void testComputeValue_ArgsEqual() {
        Expression arg1 = new ExpressionStub(5);
        Expression arg2 = new ExpressionStub(5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(null));
    }

    @Test
    public void testComputeValue_FirstArgNegative() {
        Expression arg1 = new ExpressionStub(-2);
        Expression arg2 = new ExpressionStub(1);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(null));
    }

    @Test
    public void testComputeValue_SecondArgNegative() {
        Expression arg1 = new ExpressionStub(3);
        Expression arg2 = new ExpressionStub(-5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(null));
    }

    @Test
    public void testComputeValue_NaN() {
        Expression arg1 = new ExpressionStub(Double.NaN);
        Expression arg2 = new ExpressionStub(10);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(null));
    }

    @Test
    public void testComputeValue_NegativeInfinity() {
        Expression arg1 = new ExpressionStub(Double.NEGATIVE_INFINITY);
        Expression arg2 = new ExpressionStub(100);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(null));
    }

    @Test
    public void testComputeValue_PositiveInfinity() {
        Expression arg1 = new ExpressionStub(Double.POSITIVE_INFINITY);
        Expression arg2 = new ExpressionStub(1000);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(null));
    }

    @Test
    public void testComputeValue_ErrorHandling() {
        Expression arg1 = new ExpressionStub("abc");
        Expression arg2 = new ExpressionStub(5);
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(null));
    }

    private class ExpressionStub extends Expression {
        private Object value;

        public ExpressionStub(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}
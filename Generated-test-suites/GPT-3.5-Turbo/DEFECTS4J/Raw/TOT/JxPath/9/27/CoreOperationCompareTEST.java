import org.junit.Assert;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression arg1 = new MockExpression("value");
        Expression arg2 = new MockExpression("value");
        CoreOperationCompare op = new MockCoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        boolean result = op.equal(context, arg1, arg2);

        Assert.assertTrue(result);
    }

    @Test
    public void testNotEqual() {
        Expression arg1 = new MockExpression("value1");
        Expression arg2 = new MockExpression("value2");
        CoreOperationCompare op = new MockCoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        boolean result = op.equal(context, arg1, arg2);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithNull() {
        Expression arg1 = new MockExpression(null);
        Expression arg2 = new MockExpression(null);
        CoreOperationCompare op = new MockCoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        boolean result = op.equal(context, arg1, arg2);

        Assert.assertTrue(result);
    }

    @Test
    public void testNotEqualWithNull() {
        Expression arg1 = new MockExpression("value1");
        Expression arg2 = new MockExpression(null);
        CoreOperationCompare op = new MockCoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        boolean result = op.equal(context, arg1, arg2);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithBoolean() {
        Expression arg1 = new MockExpression(true);
        Expression arg2 = new MockExpression(Boolean.TRUE);
        CoreOperationCompare op = new MockCoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        boolean result = op.equal(context, arg1, arg2);

        Assert.assertTrue(result);
    }

    @Test
    public void testNotEqualWithBoolean() {
        Expression arg1 = new MockExpression(true);
        Expression arg2 = new MockExpression(false);
        CoreOperationCompare op = new MockCoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        boolean result = op.equal(context, arg1, arg2);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithNumber() {
        Expression arg1 = new MockExpression(10);
        Expression arg2 = new MockExpression(10.0);
        CoreOperationCompare op = new MockCoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        boolean result = op.equal(context, arg1, arg2);

        Assert.assertTrue(result);
    }

    @Test
    public void testNotEqualWithNumber() {
        Expression arg1 = new MockExpression(10);
        Expression arg2 = new MockExpression(5);
        CoreOperationCompare op = new MockCoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        boolean result = op.equal(context, arg1, arg2);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithString() {
        Expression arg1 = new MockExpression("value");
        Expression arg2 = new MockExpression("value");
        CoreOperationCompare op = new MockCoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        boolean result = op.equal(context, arg1, arg2);

        Assert.assertTrue(result);
    }

    @Test
    public void testNotEqualWithString() {
        Expression arg1 = new MockExpression("value");
        Expression arg2 = new MockExpression("otherValue");
        CoreOperationCompare op = new MockCoreOperationCompare(arg1, arg2);

        EvalContext context = new EvalContext();
        boolean result = op.equal(context, arg1, arg2);

        Assert.assertFalse(result);
    }
}
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.test.ExpressionTestCase;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        Expression arg1 = new Const(1.0);
        Expression arg2 = new Const(2.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());

        boolean result = coreOperationGreaterThanOrEqual.computeValue(context).booleanValue();
        Assert.assertTrue(result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithFalseResult() {
        Expression arg1 = new Const(1.0);
        Expression arg2 = new Const(3.0);
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());

        boolean result = coreOperationGreaterThanOrEqual.computeValue(context).booleanValue();
        Assert.assertFalse(result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithInvalidTypes() {
        Expression arg1 = new Const(1L);
        Expression arg2 = new Const("text");
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext(new InfoSet());

        coreOperationGreaterThanOrEqual.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArg() {
        Expression arg1 = new Const(1.0);
        Expression arg2 = null;
        CoreOperationGreaterThanOrEqual coreOperationGreaterThanOrEqual = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        coreOperationGreaterThanOrEqual.computeValue(new EvalContext(new InfoSet()));
    }
}
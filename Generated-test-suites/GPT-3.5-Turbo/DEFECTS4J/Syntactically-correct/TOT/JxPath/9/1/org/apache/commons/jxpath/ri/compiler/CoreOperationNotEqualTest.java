import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationNotEqualTest {

    @Test
    public void testComputeValue_TypicalCase() {
        // Typical case: arg1 = 5, arg2 = 7
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new Expression(5), new Expression(7));
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_ArgumentsEqual() {
        // Arguments are equal: arg1 = "test", arg2 = "test"
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new Expression("test"), new Expression("test"));
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_OneArgumentNull() {
        // One argument is null
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new Expression("test"), new Expression(null));
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Reverse order
        coreOperation = new CoreOperationNotEqual(new Expression(null), new Expression("test"));
        context = new EvalContext();
        result = coreOperation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_BothArgumentsNull() {
        // Both arguments are null
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new Expression(null), new Expression(null));
        EvalContext context = new EvalContext();
        Object result = coreOperation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual coreOperation = new CoreOperationNotEqual(new Expression(5), new Expression(7));
        Assert.assertEquals("!=", coreOperation.getSymbol());
    }
}
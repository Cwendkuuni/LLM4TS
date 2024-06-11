import org.junit.Assert;
import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        EvalContext context = new EvalContext();
        CoreOperationEqual operation = new CoreOperationEqual(new Expression("arg1"), new Expression("arg2"));
        context.setVariable("arg1", "abc");
        context.setVariable("arg2", "abc");
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Edge case
        context.setVariable("arg1", Integer.valueOf(100));
        context.setVariable("arg2", Integer.valueOf(100));
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Error scenario
        context.setVariable("arg1", null);
        context.setVariable("arg2", "abc");
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        // Typical use case
        CoreOperationEqual operation = new CoreOperationEqual(new Expression("arg1"), new Expression("arg2"));
        Assert.assertEquals("=", operation.getSymbol());
    }
}
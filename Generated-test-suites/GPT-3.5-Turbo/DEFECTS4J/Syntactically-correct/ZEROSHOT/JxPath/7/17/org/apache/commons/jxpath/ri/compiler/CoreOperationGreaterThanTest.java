import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue_GreaterThan_ReturnsTrue() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();

        context.setValue("arg1", 5);
        context.setValue("arg2", 3);

        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));
    }

    @Test
    public void testComputeValue_LessThan_ReturnsFalse() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());
        EvalContext context = new EvalContext();

        context.setValue("arg1", 3);
        context.setValue("arg2", 5);

        Assert.assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol_ReturnsGreaterThanSymbol() {
        CoreOperationGreaterThan operation = new CoreOperationGreaterThan(new Expression(), new Expression());

        Assert.assertEquals(">", operation.getSymbol());
    }
}
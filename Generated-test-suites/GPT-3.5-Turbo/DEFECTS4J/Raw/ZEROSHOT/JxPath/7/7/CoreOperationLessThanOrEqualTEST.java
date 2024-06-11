import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    private CoreOperationLessThanOrEqual operation;

    @Test
    public void testComputeValue() {
        operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();

        // Test when l is less than r
        double l = 2;
        double r = 5;
        context.setContextNode(l);
        context.setContextPosition(1);
        context.setContextSize(1);

        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Test when l is equal to r
        l = 5;
        r = 5;
        context.setContextNode(l);
        context.setContextPosition(1);
        context.setContextSize(1);

        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Test when l is greater than r
        l = 10;
        r = 5;
        context.setContextNode(l);
        context.setContextPosition(1);
        context.setContextSize(1);

        Assert.assertEquals(Boolean.FALSE, operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        operation = new CoreOperationLessThanOrEqual(new Expression(), new Expression());
        Assert.assertEquals("<=", operation.getSymbol());
    }
}
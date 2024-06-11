
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationNotEqualTest {

    // Test computeValue() method
    @Test
    public void testComputeValue() {
        // Create test objects
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        // Test case 1: args[0] != args[1]
        context.setNodeSet(new Object[]{1});
        Object result = coreOperationNotEqual.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test case 2: args[0] == args[1]
        context.setNodeSet(new Object[]{1, 1});
        result = coreOperationNotEqual.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    // Test getSymbol() method
    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationNotEqual coreOperationNotEqual = new CoreOperationNotEqual(arg1, arg2);

        String symbol = coreOperationNotEqual.getSymbol();
        Assert.assertEquals("!=", symbol);
    }
}

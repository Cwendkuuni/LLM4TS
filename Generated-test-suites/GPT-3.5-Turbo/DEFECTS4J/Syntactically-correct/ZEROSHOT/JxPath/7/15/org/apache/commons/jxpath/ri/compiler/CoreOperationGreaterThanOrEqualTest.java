import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Test case 1
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        context.setNodeSet(new NodeSet());

        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);

        // Test case 2
        arg1 = new Expression();
        arg2 = new Expression();
        arg1.setValue(10);
        arg2.setValue(5);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);

        // Test case 3
        arg1 = new Expression();
        arg2 = new Expression();
        arg1.setValue(5);
        arg2.setValue(10);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);

        String symbol = operation.getSymbol();
        Assert.assertEquals(">=", symbol);
    }

}
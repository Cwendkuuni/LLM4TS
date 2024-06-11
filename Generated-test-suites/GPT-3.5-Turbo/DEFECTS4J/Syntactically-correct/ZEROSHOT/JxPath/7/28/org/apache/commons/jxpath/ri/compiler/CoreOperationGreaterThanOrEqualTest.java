import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();

        // Test case 1
        arg1.setValue(10);
        arg2.setValue(5);
        Assert.assertTrue(coreOperation.computeValue(context));

        // Test case 2
        arg1.setValue(5);
        arg2.setValue(10);
        Assert.assertFalse(coreOperation.computeValue(context));

        // Test case 3
        arg1.setValue(5);
        arg2.setValue(5);
        Assert.assertTrue(coreOperation.computeValue(context));

        // Add more test cases to cover all possible scenarios
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Assert.assertEquals(">=", coreOperation.getSymbol());
    }
}
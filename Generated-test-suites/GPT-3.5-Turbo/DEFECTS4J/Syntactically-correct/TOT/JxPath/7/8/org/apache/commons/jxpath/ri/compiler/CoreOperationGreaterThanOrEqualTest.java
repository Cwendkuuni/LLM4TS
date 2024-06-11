import org.junit.Assert;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // typical use case
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        arg1.setValue(10);
        arg2.setValue(5);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(null));

        // edge case
        arg1.setValue(5);
        arg2.setValue(5);
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(null));

        // error scenario
        arg1.setValue("10"); // invalid argument
        arg2.setValue(5);
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(null, null);
        Assert.assertEquals(">=", operation.getSymbol());
    }
}
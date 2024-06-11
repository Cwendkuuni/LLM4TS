import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        // Typical use case
        arg1.setValue(5);
        arg2.setValue(10);
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));

        // Edge case: arg1 and arg2 are equal
        arg1.setValue(5);
        arg2.setValue(5);
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));

        // Edge case: arg1 is less than arg2
        arg1.setValue(5);
        arg2.setValue(3);
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));

        // Edge case: arg1 is greater than arg2
        arg1.setValue(10);
        arg2.setValue(15);
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(new EvalContext()));

        // Error scenario: arg1 or arg2 is null
        arg1.setValue(null);
        arg2.setValue(5);
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));

        arg1.setValue(5);
        arg2.setValue(null);
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));

        arg1.setValue(null);
        arg2.setValue(null);
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(new EvalContext()));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);

        Assert.assertEquals("<=", operation.getSymbol());
    }
}
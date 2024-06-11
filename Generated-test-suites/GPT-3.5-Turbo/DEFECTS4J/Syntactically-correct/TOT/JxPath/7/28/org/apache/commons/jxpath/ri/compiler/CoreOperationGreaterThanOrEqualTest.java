import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.*;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case: arg1 is greater than arg2
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Typical use case: arg1 is not greater than arg2
        arg1 = new Constant(3);
        arg2 = new Constant(5);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(context));

        // Edge case: arg1 and arg2 are equal
        arg1 = new Constant(5);
        arg2 = new Constant(5);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        Assert.assertEquals(Boolean.TRUE, operation.computeValue(context));

        // Edge case: arg1 and arg2 are negative numbers
        arg1 = new Constant(-5);
        arg2 = new Constant(-3);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        Assert.assertEquals(Boolean.FALSE, operation.computeValue(context));

        // Error scenario: arg1 is null
        arg1 = null;
        arg2 = new Constant(3);
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        Assert.assertNull(operation.computeValue(context));

        // Error scenario: arg2 is null
        arg1 = new Constant(5);
        arg2 = null;
        operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        context = new EvalContext();
        Assert.assertNull(operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Constant(5);
        Expression arg2 = new Constant(3);
        CoreOperationGreaterThanOrEqual operation = new CoreOperationGreaterThanOrEqual(arg1, arg2);
        Assert.assertEquals(">=", operation.getSymbol());
    }
}
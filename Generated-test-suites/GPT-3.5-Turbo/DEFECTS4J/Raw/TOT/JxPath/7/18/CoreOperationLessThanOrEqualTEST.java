import org.junit.Assert;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    private final CoreOperationLessThanOrEqual operation = new CoreOperationLessThanOrEqual(arg1, arg2);

    // Test cases for computeValue method

    @Test
    public void testComputeValue_WillReturnTrue_WhenArg1LesserThanArg2() {
        EvalContext context = new EvalContext();
        context.setValue("arg1", 5);
        context.setValue("arg2", 10);
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_WillReturnTrue_WhenArg1EqualsArg2() {
        EvalContext context = new EvalContext();
        context.setValue("arg1", 10);
        context.setValue("arg2", 10);
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_WillReturnFalse_WhenArg1GreaterThanArg2() {
        EvalContext context = new EvalContext();
        context.setValue("arg1", 15);
        context.setValue("arg2", 10);
        Object result = operation.computeValue(context);
        Assert.assertEquals(Boolean.FALSE, result);
    }

    // Test cases for getSymbol method

    @Test
    public void testGetSymbol_WillReturnLessThanOrEqualSymbol() {
        String symbol = operation.getSymbol();
        Assert.assertEquals("<=", symbol);
    }
}
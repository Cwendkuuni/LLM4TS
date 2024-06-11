import org.junit.Test;
import org.junit.Assert;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValue_typicalUseCase() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setVariable("arg1", "value1");
        context.setVariable("arg2", "value2");

        Object result = coreOperationEqual.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testComputeValue_edgeCase() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setVariable("arg1", "");
        context.setVariable("arg2", "");

        Object result = coreOperationEqual.computeValue(context);

        Assert.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_errorScenario() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        EvalContext context = new EvalContext();
        context.setVariable("arg1", null);
        context.setVariable("arg2", "value2");

        Object result = coreOperationEqual.computeValue(context);

        Assert.assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol_typicalUseCase() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(arg1, arg2);

        String symbol = coreOperationEqual.getSymbol();

        Assert.assertEquals("=", symbol);
    }
}
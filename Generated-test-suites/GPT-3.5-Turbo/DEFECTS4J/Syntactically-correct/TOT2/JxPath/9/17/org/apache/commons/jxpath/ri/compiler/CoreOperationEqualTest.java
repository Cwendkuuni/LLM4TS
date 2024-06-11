import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class CoreOperationEqualTest {

    @Test
    public void testComputeValueEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(arg1), new Expression(arg2));
        EvalContext context = new EvalContext();
        context.setVariable(arg1, "value1");
        context.setVariable(arg2, "value1");

        Object result = coreOperationEqual.computeValue(context);

        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValueNotEqual() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(arg1), new Expression(arg2));
        EvalContext context = new EvalContext();
        context.setVariable(arg1, "value1");
        context.setVariable(arg2, "value2");

        Object result = coreOperationEqual.computeValue(context);

        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationEqual coreOperationEqual = new CoreOperationEqual(new Expression(arg1), new Expression(arg2));

        String symbol = coreOperationEqual.getSymbol();

        assertEquals("=", symbol);
    }

}
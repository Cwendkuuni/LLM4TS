import org.junit.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValueTypicalCase() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        EvalContext context = new EvalContext();
        // Set the values of arg1 and arg2 in the context to typical values
        context.setVariableValue("arg1", 5);
        context.setVariableValue("arg2", 10);
        Object result = lessThan.computeValue(context);
        // Verify that the result is false
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueEdgeCase() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        EvalContext context = new EvalContext();
        // Set the values of arg1 and arg2 in the context to edge values
        context.setVariableValue("arg1", Double.MAX_VALUE);
        context.setVariableValue("arg2", Double.MIN_VALUE);
        Object result = lessThan.computeValue(context);
        // Verify that the result is false
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValueErrorScenario() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        EvalContext context = new EvalContext();
        // Set the values of arg1 and arg2 in the context to invalid values
        context.setVariableValue("arg1", "invalid");
        context.setVariableValue("arg2", 10);
        Object result = lessThan.computeValue(context);
        // Verify that an exception is thrown
        assertThrows(ExceptionType.class, () -> lessThan.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan lessThan = new CoreOperationLessThan(new Expression(arg1), new Expression(arg2));
        String symbol = lessThan.getSymbol();
        // Verify that the symbol is "<"
        assertEquals("<", symbol);
    }
}
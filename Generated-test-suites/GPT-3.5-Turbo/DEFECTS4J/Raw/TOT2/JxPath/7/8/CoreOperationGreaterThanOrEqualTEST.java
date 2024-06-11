import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    private CoreOperationGreaterThanOrEqual core = new CoreOperationGreaterThanOrEqual(null, null);

    @Test
    public void testComputeValue_case1() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 10);
        context.setVariable("arg2", 5);
        Object result = core.computeValue(context);
        assertEquals(result, Boolean.TRUE);
    }

    @Test
    public void testComputeValue_case2() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 10);
        Object result = core.computeValue(context);
        assertEquals(result, Boolean.FALSE);
    }

    @Test
    public void testComputeValue_case3() {
        EvalContext context = new EvalContext();
        context.setVariable("arg1", 5);
        context.setVariable("arg2", 5);
        Object result = core.computeValue(context);
        assertEquals(result, Boolean.TRUE);
    }

    @Test
    public void testGetSymbol() {
        String symbol = core.getSymbol();
        assertEquals(symbol, ">=");
    }

    // Additional test cases for edge cases and exceptions can be added here

}
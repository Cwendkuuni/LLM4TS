
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {
    @Test
    public void testComputeValue() {
        // Test with arg1 > arg2
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new Expression(arg1), new Expression(arg2));
        EvalContext context = new EvalContext();
        context.setVariable(arg1, 5);
        context.setVariable(arg2, 3);
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Test with arg1 < arg2
        expression = new CoreOperationGreaterThanOrEqual(new Expression(arg1), new Expression(arg2));
        context.setVariable(arg1, 3);
        context.setVariable(arg2, 5);
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Test with arg1 = arg2
        expression = new CoreOperationGreaterThanOrEqual(new Expression(arg1), new Expression(arg2));
        context.setVariable(arg1, 5);
        context.setVariable(arg2, 5);
        result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual expression = new CoreOperationGreaterThanOrEqual(new Expression(arg1), new Expression(arg2));
        String symbol = expression.getSymbol();
        assertEquals(">=", symbol);
    }
}

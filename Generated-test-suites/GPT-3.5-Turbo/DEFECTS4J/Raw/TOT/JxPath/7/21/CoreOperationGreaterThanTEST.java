import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        // Typical use case: l > r is true
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(4), new Expression(3));
        EvalContext context = new EvalContext();
        Object result = expression.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Typical use case: l > r is false
        expression = new CoreOperationGreaterThan(new Expression(3), new Expression(4));
        context = new EvalContext();
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Edge case: l = r
        expression = new CoreOperationGreaterThan(new Expression(5), new Expression(5));
        context = new EvalContext();
        result = expression.computeValue(context);
        assertEquals(Boolean.FALSE, result);

        // Error scenario: null values
        expression = new CoreOperationGreaterThan(null, new Expression(3));
        context = new EvalContext();
        result = expression.computeValue(context);
        assertNull(result);

       expression = new CoreOperationGreaterThan(new Expression(3), null);
        context = new EvalContext();
        result = expression.computeValue(context);
        assertNull(result);

    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(new Expression(4), new Expression(3));
        String symbol = expression.getSymbol();
        assertEquals(">", symbol);
    }
}
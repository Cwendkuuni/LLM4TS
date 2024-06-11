import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Literal(5), new Literal(3));
        EvalContext context = new EvalContext();
        Object result = greaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);

        // Edge case: equal values
        greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Literal(5), new Literal(5));
        context = new EvalContext();
        result = greaterThanOrEqual.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual greaterThanOrEqual = new CoreOperationGreaterThanOrEqual(new Literal(5), new Literal(3));
        assertEquals(">=", greaterThanOrEqual.getSymbol());
    }
}
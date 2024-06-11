import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        // Typical use case
        EvalContext context = new EvalContext();
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new Expression(5), new Expression(3));
        assertEquals(Boolean.TRUE, greaterThan.computeValue(context));

        // Edge cases
        CoreOperationGreaterThan greaterThan2 = new CoreOperationGreaterThan(new Expression(Double.MAX_VALUE), new Expression(Double.MIN_VALUE));
        assertEquals(Boolean.TRUE, greaterThan2.computeValue(context));

        CoreOperationGreaterThan greaterThan3 = new CoreOperationGreaterThan(new Expression(0), new Expression(0));
        assertEquals(Boolean.FALSE, greaterThan3.computeValue(context));

        // Error scenario
        CoreOperationGreaterThan greaterThan4 = new CoreOperationGreaterThan(null, new Expression(3));
        assertNull(greaterThan4.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new Expression(5), new Expression(3));
        assertEquals(">", greaterThan.getSymbol());
    }
}
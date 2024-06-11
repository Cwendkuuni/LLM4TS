import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        // Typical use cases
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new Expression(5), new Expression(2));
        assertFalse((Boolean) greaterThan.computeValue(new EvalContext()));

        // Edge cases
        CoreOperationGreaterThan greaterThan1 = new CoreOperationGreaterThan(new Expression(Double.MAX_VALUE), new Expression(Double.MIN_VALUE));
        assertTrue((Boolean) greaterThan1.computeValue(new EvalContext()));

        CoreOperationGreaterThan greaterThan2 = new CoreOperationGreaterThan(new Expression(Double.NaN), new Expression(Double.NaN));
        assertFalse((Boolean) greaterThan2.computeValue(new EvalContext()));

        CoreOperationGreaterThan greaterThan3 = new CoreOperationGreaterThan(new Expression(0), new Expression(0));
        assertFalse((Boolean) greaterThan3.computeValue(new EvalContext()));

        // Error scenarios
        CoreOperationGreaterThan greaterThan4 = new CoreOperationGreaterThan(null, new Expression(5));
        assertNull(greaterThan4.computeValue(new EvalContext()));

        CoreOperationGreaterThan greaterThan5 = new CoreOperationGreaterThan(new Expression(5), null);
        assertNull(greaterThan5.computeValue(new EvalContext()));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new Expression(5), new Expression(2));
        assertEquals(">", greaterThan.getSymbol());
    }
}
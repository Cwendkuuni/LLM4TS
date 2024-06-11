import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        // Typical use case: both arguments are less than
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(5), new Expression(10));
        boolean result = operation.computeValue(context);
        assertTrue(result);

        // Edge case: both arguments are equal
        CoreOperationLessThan operation2 = new CoreOperationLessThan(new Expression(5), new Expression(5));
        boolean result2 = operation2.computeValue(context);
        assertFalse(result2);

        // Error scenario: one argument is null
        CoreOperationLessThan operation3 = new CoreOperationLessThan(new Expression(null), new Expression(10));
        boolean result3 = operation3.computeValue(context);
        assertFalse(result3);

        // Error scenario: one argument is not a number
        CoreOperationLessThan operation4 = new CoreOperationLessThan(new Expression("five"), new Expression(10));
        boolean result4 = operation4.computeValue(context);
        assertFalse(result4);

        // Error scenario: both arguments are null
        CoreOperationLessThan operation5 = new CoreOperationLessThan(new Expression(null), new Expression(null));
        boolean result5 = operation5.computeValue(context);
        assertFalse(result5);
    }

    @Test
    public void testGetSymbol() {
        CoreOperationLessThan operation = new CoreOperationLessThan(new Expression(5), new Expression(10));
        String symbol = operation.getSymbol();
        assertEquals("<", symbol);
    }
}
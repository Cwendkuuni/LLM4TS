import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue() {
        // Typical use cases
        // Test when left argument is greater than the right argument
        CoreOperationGreaterThanOrEqual operation1 = new CoreOperationGreaterThanOrEqual(new Expression(5), new Expression(3));
        assertTrue(operation1.computeValue() == Boolean.TRUE);
        
        // Test when left argument is equal to the right argument
        CoreOperationGreaterThanOrEqual operation2 = new CoreOperationGreaterThanOrEqual(new Expression(3), new Expression(3));
        assertTrue(operation2.computeValue() == Boolean.TRUE);
        
        // Test when left argument is less than the right argument
        CoreOperationGreaterThanOrEqual operation3 = new CoreOperationGreaterThanOrEqual(new Expression(1), new Expression(3));
        assertTrue(operation3.computeValue() == Boolean.FALSE);

        // Edge cases
        // Test when both arguments are maximum double value
        CoreOperationGreaterThanOrEqual operation4 = new CoreOperationGreaterThanOrEqual(new Expression(Double.MAX_VALUE), new Expression(Double.MAX_VALUE));
        assertTrue(operation4.computeValue() == Boolean.TRUE);
        
        // Test when both arguments are minimum double value
        CoreOperationGreaterThanOrEqual operation5 = new CoreOperationGreaterThanOrEqual(new Expression(Double.MIN_VALUE), new Expression(Double.MIN_VALUE));
        assertTrue(operation5.computeValue() == Boolean.TRUE);
        
        // Test when left argument is positive infinity
        CoreOperationGreaterThanOrEqual operation6 = new CoreOperationGreaterThanOrEqual(new Expression(Double.POSITIVE_INFINITY), new Expression(5));
        assertTrue(operation6.computeValue() == Boolean.TRUE);
        
        // Test when right argument is negative infinity
        CoreOperationGreaterThanOrEqual operation7 = new CoreOperationGreaterThanOrEqual(new Expression(5), new Expression(Double.NEGATIVE_INFINITY));
        assertTrue(operation7.computeValue() == Boolean.FALSE);
    }

    @Test
    public void testGetSymbol() {
        // Typical use case
        CoreOperationGreaterThanOrEqual operation1 = new CoreOperationGreaterThanOrEqual(new Expression(5), new Expression(3));
        assertTrue(operation1.getSymbol().equals(">="));
        
        // Edge case
        CoreOperationGreaterThanOrEqual operation2 = new CoreOperationGreaterThanOrEqual(new Expression(3), new Expression(3));
        assertTrue(operation2.getSymbol().equals(">="));
    }

}
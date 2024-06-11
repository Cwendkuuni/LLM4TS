import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    // Test computeValue method
    @Test
    public void testComputeValue() {
        // Test with two integer values
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
            new Expression(5), new Expression(3));
        assertEquals(Boolean.TRUE, expression.computeValue(null));

        // Test with two double values
        CoreOperationGreaterThan expression2 = new CoreOperationGreaterThan(
            new Expression(5.0), new Expression(3.0));
        assertEquals(Boolean.TRUE, expression2.computeValue(null));

        // Test with two negative integer values
        CoreOperationGreaterThan expression3 = new CoreOperationGreaterThan(
            new Expression(-5), new Expression(-3));
        assertEquals(Boolean.FALSE, expression3.computeValue(null));
    }

    // Test getSymbol method
    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(null, null);
        assertEquals(">", expression.getSymbol());
    }

    // Test invalid input cases
    @Test(expected = NullPointerException.class)
    public void testInvalidInput() {
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(null, null);
        expression.computeValue(null);
    }

    // Test edge cases
    @Test
    public void testEdgeCases() {
        // Test with two very large integer values
        CoreOperationGreaterThan expression = new CoreOperationGreaterThan(
            new Expression(Integer.MAX_VALUE), new Expression(Integer.MAX_VALUE - 1));
        assertEquals(Boolean.TRUE, expression.computeValue(null));

        // Test with three identical double values
        CoreOperationGreaterThan expression2 = new CoreOperationGreaterThan(
            new Expression(10.0), new Expression(10.0));
        assertEquals(Boolean.FALSE, expression2.computeValue(null));
    }
}
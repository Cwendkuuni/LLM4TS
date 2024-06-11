import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    @Test
    public void testComputeValue() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new Constant(5), new Constant(3));
        assertEquals(Boolean.TRUE, greaterThan.computeValue(null));

        CoreOperationGreaterThan equalThan = new CoreOperationGreaterThan(new Constant(3), new Constant(3));
        assertEquals(Boolean.FALSE, equalThan.computeValue(null));

        CoreOperationGreaterThan lessThan = new CoreOperationGreaterThan(new Constant(1), new Constant(3));
        assertEquals(Boolean.FALSE, lessThan.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(null, null);
        assertEquals(">", greaterThan.getSymbol());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorNullArgument() {
        new CoreOperationGreaterThan(null, null);
    }

    @Test
    public void testComputeValueEdgeCases() {
        CoreOperationGreaterThan greaterThan = new CoreOperationGreaterThan(new Constant(Double.MAX_VALUE), new Constant(Double.MIN_VALUE));
        assertEquals(Boolean.TRUE, greaterThan.computeValue(null));

        CoreOperationGreaterThan lessThan = new CoreOperationGreaterThan(new Constant(Double.MIN_VALUE), new Constant(Double.MAX_VALUE));
        assertEquals(Boolean.FALSE, lessThan.computeValue(null));
    }

    @Test
    public void testComputeValueExceptions() {
        // Handle non-numeric values
        CoreOperationGreaterThan nonNumeric = new CoreOperationGreaterThan(new Constant("a"), new Constant(3));
        try {
            Boolean result = (Boolean) nonNumeric.computeValue(null);
            fail("Expected ClassCastException");
        } catch (ClassCastException e) {
            assertTrue(true);
        }
    }
}
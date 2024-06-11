import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testComputeValue_basicCase() {
        CoreOperationGreaterThanOrEqual core = new CoreOperationGreaterThanOrEqual(new Constant(5), new Constant(3));
        assertTrue((Boolean) core.computeValue(null));
    }

    @Test
    public void testComputeValue_equalValues() {
        CoreOperationGreaterThanOrEqual core = new CoreOperationGreaterThanOrEqual(new Constant(4), new Constant(4));
        assertTrue((Boolean) core.computeValue(null));
    }

    @Test
    public void testComputeValue_reverseOrder() {
        CoreOperationGreaterThanOrEqual core = new CoreOperationGreaterThanOrEqual(new Constant(2), new Constant(4));
        assertFalse((Boolean) core.computeValue(null));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationGreaterThanOrEqual core = new CoreOperationGreaterThanOrEqual(null, null);
        assertEquals(">=", core.getSymbol());
    }

    // Edge Cases and Exception Handling
    @Test(expected = NullPointerException.class)
    public void testComputeValue_nullArgs() {
        CoreOperationGreaterThanOrEqual core = new CoreOperationGreaterThanOrEqual(null, null);
        core.computeValue(null);
    }

    @Test
    public void testComputeValue_doubleArgs() {
        CoreOperationGreaterThanOrEqual core = new CoreOperationGreaterThanOrEqual(new Constant(2.5), new Constant(2.5));
        assertTrue((Boolean) core.computeValue(null));
    }
}
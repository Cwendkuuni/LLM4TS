
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor_WithNegativeSize_ThrowsException() {
        try {
            new MultidimensionalCounter(-1, 3, 5);
            fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            assertTrue(true);
        }
    }

    @Test
    public void testGetCounts_OutOfRangeIndex_ThrowsException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        try {
            counter.getCounts(7); // Total size is 6
            fail("Expected OutOfRangeException");
        } catch (OutOfRangeException e) {
            assertTrue(true);
        }
    }

    // More test cases for other methods
}

import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        // Typical use case
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
            assertNotNull(counter);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException should not be thrown");
        }

        // Edge case: Size length is 0
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter();
            fail("NotStrictlyPositiveException should be thrown");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        // Typical use case
        assertEquals(2, counter.getDimension());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        // Typical use case
        try {
            int[] counts = counter.getCounts(4);
            assertArrayEquals(new int[]{2, 1}, counts);
        } catch (OutOfRangeException e) {
            fail("OutOfRangeException should not be thrown");
        }

        // Edge case: Index is out of range
        try {
            int[] counts = counter.getCounts(8);
            fail("OutOfRangeException should be thrown");
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        // Typical use case
        try {
            int result = counter.getCount(0, 1);
            assertEquals(1, result);
        } catch (OutOfRangeException | DimensionMismatchException e) {
            fail("Exception should not be thrown");
        }

        // Edge case: Dimension mismatch
        try {
            counter.getCount(0);
            fail("DimensionMismatchException should be thrown");
        } catch (DimensionMismatchException e) {
            // expected
        }

        // Edge case: Index is out of range
        try {
            counter.getCount(2, 1);
            fail("OutOfRangeException should be thrown");
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        // Typical use case
        assertEquals(6, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        // Typical use case
        assertArrayEquals(new int[]{2, 3}, counter.getSizes());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        // Typical use case
        assertEquals("[0][1]", counter.toString());
    }

}
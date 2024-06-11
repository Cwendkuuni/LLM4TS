import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    // Test constructor with positive sizes
    @Test
    public void testConstructorPositiveSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertNotNull(counter);
    }

    // Test constructor with zero size
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 0, 4);
    }

    // Test constructor with negative size
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, -3, 4);
    }

    // Test iterator
    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertNotNull(iterator);
    }

    // Test getCounts with valid index
    @Test
    public void testGetCountsValidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] expectedCounts = {1, 2, 3};
        int[] counts = counter.getCounts(23);
        assertArrayEquals(expectedCounts, counts);
    }

    // Test getCounts with invalid index
    @Test(expected = OutOfRangeException.class)
    public void testGetCountsInvalidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = counter.getCounts(30);
    }

    // Test getCount with valid indices
    @Test
    public void testGetCountValidIndices() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(1, 2, 3);
        assertEquals(23, count);
    }

    // Test getCount with invalid indices (out of range)
    @Test(expected = OutOfRangeException.class)
    public void testGetCountInvalidIndices() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(3, 4, 5);
        assertEquals(23, count);
    }

    // Test getCount with invalid indices (dimension mismatch)
    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(1, 2);
        assertEquals(23, count);
    }

    // Test getSize
    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int size = counter.getSize();
        assertEquals(24, size);
    }

    // Test getSizes
    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] sizes = counter.getSizes();
        int[] expectedSizes = {2, 3, 4};
        assertArrayEquals(expectedSizes, sizes);
    }

    // Test toString
    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        String string = counter.toString();
        String expectedString = "[0][0][0]";
        assertEquals(expectedString, string);
    }
}
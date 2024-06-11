import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {
    @Test
    public void testConstructor() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            assertNotNull(counter);
        } catch (Exception e) {
            fail("Constructor test failed");
        }
    }

    @Test
    public void testIterator() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            assertNotNull(iterator);
        } catch (Exception e) {
            fail("Iterator test failed");
        }
    }

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(2, counter.getDimension());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] counts = counter.getCounts(4);
        int[] expected = new int[] {1, 1}; // Corresponds to unidimensional count 4
        assertArrayEquals(expected, counts);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int index = counter.getCount(1, 2); // Multidimensional count (1, 2)
        assertEquals(5, index); // Corresponds to unidimensional count 5
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(6, counter.getSize()); // Total number of elements
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] sizes = counter.getSizes();
        int[] expected = new int[] {2, 3};
        assertArrayEquals(expected, sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizeConstructor() {
        MultidimensionalCounter counter = new MultidimensionalCounter(-2, 3);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(1, 2, 3); // Incorrect number of dimensions
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] counts = counter.getCounts(6); // Index out of bounds
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int index = counter.getCount(4, 5); // Value in dimension out of bounds
    }
}
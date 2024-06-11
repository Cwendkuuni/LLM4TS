import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertNotNull(counter);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(sizes, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testIterator() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertNotNull(iterator);
        assertTrue(iterator.hasNext());
        int count = iterator.next();
        assertEquals(0, count);
        int[] counts = iterator.getCounts();
        assertArrayEquals(new int[]{0, 0}, counts);
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] counts = counter.getCounts(4);
        assertArrayEquals(new int[]{1, 1}, counts);
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = counter.getCount(1, 2);
        assertEquals(5, index);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsException() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] counts = counter.getCounts(6); // Out of Range
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountException() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = counter.getCount(1); // Dimension Mismatch
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSize() {
        int[] sizes = {2, -3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes); // Not Strictly Positive
    }
    
    @Test(expected = UnsupportedOperationException.class)
    public void testRemove() {
        int[] sizes = {2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove(); // Remove should throw UnsupportedOperationException
    }

}
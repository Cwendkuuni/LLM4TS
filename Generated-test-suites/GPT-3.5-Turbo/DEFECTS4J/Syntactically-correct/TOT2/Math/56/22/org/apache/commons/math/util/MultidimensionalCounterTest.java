import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        
        int count = 0;
        while(iterator.hasNext()) {
            assertEquals(count++, (int) iterator.next());
        }
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        
        assertArrayEquals(new int[] {0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[] {1, 0}, counter.getCounts(1));
        assertArrayEquals(new int[] {0, 1}, counter.getCounts(2));
        assertArrayEquals(new int[] {1, 2}, counter.getCounts(5));
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);

        assertEquals(0, counter.getCount(0, 0));
        assertEquals(1, counter.getCount(1, 0));
        assertEquals(2, counter.getCount(0, 1));
        assertEquals(5, counter.getCount(1, 2));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithInvalidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);

        counter.getCount(2, 2); // Out of range for dimension 0
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidDimensionSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);

        counter.getCount(1); // Dimension mismatch
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);

        assertEquals(6, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);

        assertArrayEquals(new int[] {2, 3}, counter.getSizes());
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = counter.getCounts(17);
        assertEquals(1, counts[0]);
        assertEquals(2, counts[1]);
        assertEquals(1, counts[2]);
    }

    @Test
    public void testGetCount_OutOfRangeException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        try {
            counter.getCount(2, 3, 4);
            fail("Expected OutOfRangeException to be thrown");
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(1, 2, 1);
        assertEquals(17, count);
    }

    @Test
    public void testGetCount_DimensionMismatchException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        try {
            counter.getCount(1, 2);
            fail("Expected DimensionMismatchException to be thrown");
        } catch (DimensionMismatchException e) {
            // expected
        }
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(2, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(3, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(4, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(5, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }
}
import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructorAndGetters() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(sizes, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.getCount());
        assertEquals(1, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testCountsConversion() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 2);
        assertArrayEquals(new int[]{1, 2, 1}, counter.getCounts(15));
        assertArrayEquals(new int[]{2, 1, 0}, counter.getCounts(16));
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        assertEquals(2, counter.getCount(1, 2));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(2, 3);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(1, 2, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSize() {
        new MultidimensionalCounter(-2, 3);
    }

}
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(2, 4, 3);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetCounts() {
        int[] counts = counter.getCounts(12);
        assertArrayEquals(new int[] {1, 0, 0}, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        counter.getCounts(24);
    }

    @Test
    public void testGetCount() {
        int count = counter.getCount(1, 3, 2);
        assertEquals(23, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountMismatch() {
        counter.getCount(1, 3);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        counter.getCount(2, 4, 3);
    }

    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[] {2, 4, 3}, sizes);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertEquals(1, iterator.next().intValue());
        assertEquals(2, iterator.next().intValue());
        assertEquals(3, iterator.next().intValue());
        assertEquals(4, iterator.next().intValue());
        assertEquals(5, iterator.next().intValue());
        assertEquals(6, iterator.next().intValue());
        assertEquals(7, iterator.next().intValue());
        assertEquals(8, iterator.next().intValue());
        assertEquals(9, iterator.next().intValue());
        assertEquals(10, iterator.next().intValue());
        assertEquals(11, iterator.next().intValue());
        assertEquals(12, iterator.next().intValue());
        assertEquals(13, iterator.next().intValue());
        assertEquals(14, iterator.next().intValue());
        assertEquals(15, iterator.next().intValue());
        assertEquals(16, iterator.next().intValue());
        assertEquals(17, iterator.next().intValue());
        assertEquals(18, iterator.next().intValue());
        assertEquals(19, iterator.next().intValue());
        assertEquals(20, iterator.next().intValue());
        assertEquals(21, iterator.next().intValue());
        assertEquals(22, iterator.next().intValue());
        assertEquals(23, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }
}
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter multidimensionalCounter;

    @Before
    public void setUp() {
        multidimensionalCounter = new MultidimensionalCounter(3, 4, 3);
    }

    @Test
    public void testIteratorNextBeforeHasNext() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
        assertEquals(0, iterator.next().intValue());
    }

    @Test
    public void testIteratorNextAfterHasNext() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
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

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
        iterator.remove();
    }

    @Test
    public void testIteratorGetCount() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
        assertEquals(-1, iterator.getCount());
        iterator.next();
        assertEquals(0, iterator.getCount());
    }

    @Test
    public void testIteratorGetCounts() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        iterator.next();
        assertArrayEquals(new int[]{0, 0, 1}, iterator.getCounts());
    }

    @Test
    public void testIteratorGetCountWithDimension() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
        assertEquals(0, iterator.getCount(0));
        iterator.next();
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        iterator.next();
        assertEquals(0, iterator.getCount(0));
        assertEquals(1, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, multidimensionalCounter.getDimension());
    }

    @Test
    public void testGetCounts() {
        assertArrayEquals(new int[]{0, 0, 0}, multidimensionalCounter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, multidimensionalCounter.getCounts(1));
        assertArrayEquals(new int[]{0, 0, 2}, multidimensionalCounter.getCounts(2));
        assertArrayEquals(new int[]{0, 1, 0}, multidimensionalCounter.getCounts(3));
        assertArrayEquals(new int[]{1, 0, 0}, multidimensionalCounter.getCounts(12));
        assertArrayEquals(new int[]{1, 3, 2}, multidimensionalCounter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsWhenIndexOutOfRange() {
        multidimensionalCounter.getCounts(24);
    }

    @Test
    public void testGetCount() {
        assertEquals(0, multidimensionalCounter.getCount(0, 0, 0));
        assertEquals(1, multidimensionalCounter.getCount(0, 0, 1));
        assertEquals(2, multidimensionalCounter.getCount(0, 0, 2));
        assertEquals(3, multidimensionalCounter.getCount(0, 1, 0));
        assertEquals(12, multidimensionalCounter.getCount(1, 0, 0));
        assertEquals(23, multidimensionalCounter.getCount(1, 3, 2));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWhenDimensionsNotMatched() {
        multidimensionalCounter.getCount(0, 0, 0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWhenIndexOutOfRange() {
        multidimensionalCounter.getCount(2, 4, 3);
    }

    @Test
    public void testGetSize() {
        assertEquals(24, multidimensionalCounter.getSize());
    }

    @Test
    public void testGetSizes() {
        assertArrayEquals(new int[]{3, 4, 3}, multidimensionalCounter.getSizes());
    }

    @Test
    public void testToString() {
        assertEquals("[0][0][0]", multidimensionalCounter.toString());
    }
}
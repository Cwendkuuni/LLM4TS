import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {
    

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[]{2, 4, 3}, sizes);
    }

    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        for (int i = 0; i < 23; i++) {
            iterator.next();
        }
        assertTrue(iterator.hasNext());
        iterator.next();
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertEquals(Integer.valueOf(3), iterator.next());
        assertEquals(Integer.valueOf(4), iterator.next());
        assertEquals(Integer.valueOf(5), iterator.next());
        assertEquals(Integer.valueOf(6), iterator.next());
        assertEquals(Integer.valueOf(7), iterator.next());
        assertEquals(Integer.valueOf(8), iterator.next());
        assertEquals(Integer.valueOf(9), iterator.next());
        assertEquals(Integer.valueOf(10), iterator.next());
        assertEquals(Integer.valueOf(11), iterator.next());
        assertEquals(Integer.valueOf(12), iterator.next());
        assertEquals(Integer.valueOf(13), iterator.next());
        assertEquals(Integer.valueOf(14), iterator.next());
        assertEquals(Integer.valueOf(15), iterator.next());
        assertEquals(Integer.valueOf(16), iterator.next());
        assertEquals(Integer.valueOf(17), iterator.next());
        assertEquals(Integer.valueOf(18), iterator.next());
        assertEquals(Integer.valueOf(19), iterator.next());
        assertEquals(Integer.valueOf(20), iterator.next());
        assertEquals(Integer.valueOf(21), iterator.next());
        assertEquals(Integer.valueOf(22), iterator.next());
        assertEquals(Integer.valueOf(23), iterator.next());
    }

    @Test
    public void testIteratorGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount());
        iterator.next();
        assertEquals(1, iterator.getCount());
        for (int i = 0; i < 23; i++) {
            iterator.next();
        }
        assertEquals(24, iterator.getCount());
    }

    @Test
    public void testIteratorGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        iterator.next();
        assertArrayEquals(new int[]{0, 0, 1}, iterator.getCounts());
        for (int i = 0; i < 22; i++) {
            iterator.next();
        }
        assertArrayEquals(new int[]{1, 3, 2}, iterator.getCounts());
    }

    @Test
    public void testIteratorGetCountForDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));
        iterator.next();
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(1, iterator.getCount(2));
        for (int i = 0; i < 22; i++) {
            iterator.next();
        }
        assertEquals(1, iterator.getCount(0));
        assertEquals(3, iterator.getCount(1));
        assertEquals(2, iterator.getCount(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(3));
        assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(24);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(3, counter.getCount(0, 1, 0));
        assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(2, 3, 4);
    }

    
}
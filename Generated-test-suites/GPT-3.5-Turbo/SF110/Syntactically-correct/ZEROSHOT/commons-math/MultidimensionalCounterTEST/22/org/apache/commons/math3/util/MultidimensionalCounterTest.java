
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter multidimensionalCounter;

    @Before
    public void setUp() {
        int[] size = {5, 5, 5};
        this.multidimensionalCounter = new MultidimensionalCounter(size);
    }

    @After
    public void tearDown() {
        this.multidimensionalCounter = null;
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, multidimensionalCounter.getDimension());
    }

    @Test
    public void testGetCounts() {
        int[] counts = multidimensionalCounter.getCounts(0);
        assertArrayEquals(new int[]{0, 0, 0}, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] counts = multidimensionalCounter.getCounts(125);
    }

    @Test
    public void testGetCount() {
        int count = multidimensionalCounter.getCount(0, 0, 0);
        assertEquals(0, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int count = multidimensionalCounter.getCount(0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int count = multidimensionalCounter.getCount(3, 7, 2);
    }

    @Test
    public void testGetSize() {
        assertEquals(125, multidimensionalCounter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] sizes = multidimensionalCounter.getSizes();
        assertArrayEquals(new int[]{5, 5, 5}, sizes);
    }

    @Test
    public void testToString() {
        assertEquals("[0][0][0]", multidimensionalCounter.toString());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(0), iterator.next());
        assertEquals(0, iterator.getCount());
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
        iterator.remove();
    }

    @Test
    public void testIteratorNextAll() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            assertEquals(Integer.valueOf(count), iterator.next());
            count++;
        }
        assertEquals(125, count);
    }

    @Test
    public void testIteratorNextUntilEnd() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
        for (int i = 0; i < 124; i++) {
            iterator.next();
        }
        assertEquals(Integer.valueOf(124), iterator.next());
    }

    @Test
    public void testIteratorNextAfterEnd() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
        for (int i = 0; i < 125; i++) {
            iterator.next();
        }
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }
}
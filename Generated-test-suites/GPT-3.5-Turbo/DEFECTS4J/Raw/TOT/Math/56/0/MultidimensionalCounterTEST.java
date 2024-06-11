import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator_hasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());

        for (int i = 0; i < counter.getSize() - 1; i++) {
            iterator.next();
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator_next() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
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
        assertEquals(Integer.valueOf(24), iterator.next());

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIterator_getCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        for (int i = 0; i < counter.getSize(); i++) {
            int count = iterator.getCount();
            int[] counts = iterator.getCounts();

            assertEquals(i, count);
            assertArrayEquals(counter.getCounts(i), counts);

            iterator.next();
        }
    }

    @Test
    public void testIterator_getCountForDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        for (int i = 0; i < counter.getDimension(); i++) {
            for (int j = 0; j < counter.getSizes()[i]; j++) {
                assertEquals(j, iterator.getCount(i));
                iterator.next();
            }
        }
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(12));
        assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_outOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(25);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 1, 0));
        assertEquals(12, counter.getCount(1, 0, 0));
        assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_dimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_outOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(2, 4, 3);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
    }
}
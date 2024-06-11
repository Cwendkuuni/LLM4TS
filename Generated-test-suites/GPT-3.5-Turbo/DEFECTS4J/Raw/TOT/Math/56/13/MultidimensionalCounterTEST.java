import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] expectedSizes = {2, 4, 3};
        assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        int[] expectedCounts1 = {0, 0, 0};
        int[] expectedCounts2 = {1, 3, 2};
        int[] expectedCounts3 = {0, 2, 1};

        assertArrayEquals(expectedCounts1, counter.getCounts(0));
        assertArrayEquals(expectedCounts2, counter.getCounts(23));
        assertArrayEquals(expectedCounts3, counter.getCounts(10));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsIndexOutOfBoundsException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(24);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        int[] counts1 = {0, 0, 0};
        int[] counts2 = {1, 3, 2};
        int[] counts3 = {0, 2, 1};

        assertEquals(0, counter.getCount(counts1));
        assertEquals(23, counter.getCount(counts2));
        assertEquals(10, counter.getCount(counts3));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatchException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] counts = {0, 0};
        counter.getCount(counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        int[] counts = {0, 0, 5};
        counter.getCount(counts);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(0, (int) iterator.next());
        assertEquals(1, (int) iterator.next());
        assertEquals(2, (int) iterator.next());
        assertEquals(3, (int) iterator.next());
        assertEquals(4, (int) iterator.next());
        assertEquals(5, (int) iterator.next());

        int[] expectedCounts = {0, 0, 0};
        assertArrayEquals(expectedCounts, iterator.getCounts());

        assertEquals(6, iterator.getCount());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));

        assertTrue(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testHasNext() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
        iterator.next();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testHasNextFalse() {
        MultidimensionalCounter counter = new MultidimensionalCounter(0, 0, 0);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals("[0][0][0]", counter.toString());
    }
}
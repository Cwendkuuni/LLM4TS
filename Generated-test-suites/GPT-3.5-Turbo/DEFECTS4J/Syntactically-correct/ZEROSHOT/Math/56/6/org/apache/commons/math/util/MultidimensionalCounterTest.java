import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // Test hasNext method
        assertTrue(iterator.hasNext());

        // Test next method
        assertEquals(Integer.valueOf(0), iterator.next());

        // Test getCount method
        assertEquals(0, iterator.getCount());

        // Test getCounts method
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());

        // Test getCount(int dim) method
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));

        // Test remove method
        try {
            iterator.remove();
            fail("Should have thrown UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Test valid index
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(12));
        assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));

        // Test invalid index
        try {
            counter.getCounts(-1);
            fail("Should have thrown OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        try {
            counter.getCounts(24);
            fail("Should have thrown OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Test valid counts
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 1, 0));
        assertEquals(12, counter.getCount(1, 0, 0));
        assertEquals(23, counter.getCount(1, 3, 2));

        // Test invalid counts
        try {
            counter.getCount(0, 0, -1);
            fail("Should have thrown OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        try {
            counter.getCount(0, 0, 3);
            fail("Should have thrown OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        try {
            counter.getCount(0, -1, 0);
            fail("Should have thrown OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        try {
            counter.getCount(0, 4, 0);
            fail("Should have thrown OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        try {
            counter.getCount(2, 0, 0);
            fail("Should have thrown OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
        try {
            counter.getCount(-1, 0, 0);
            fail("Should have thrown OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Test getSize method
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Test getSizes method
        assertArrayEquals(new int[]{2, 4, 3}, counter.getSizes());
    }
}
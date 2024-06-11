
import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        int count = 0;
        while (iterator.hasNext()) {
            int nextCount = iterator.next();
            assertEquals(count, nextCount);
            count++;
        }
        assertEquals(24, count);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        assertEquals(new int[]{0, 1, 0}, counter.getCounts(3));
        // ...
        assertEquals(new int[]{1, 3, 2}, counter.getCounts(23));
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 1, 0));
        // ...
        assertEquals(12, counter.getCount(1, 0, 0));
        // ...
        assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange1() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(2, 0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange2() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 5, 0);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertArrayEquals(new int[]{2, 4, 3}, counter.getSizes());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals("[0][0][0]", counter.toString());
    }
}

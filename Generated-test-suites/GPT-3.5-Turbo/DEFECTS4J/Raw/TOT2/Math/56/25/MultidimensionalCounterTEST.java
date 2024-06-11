import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        int count = 0;
        while (iterator.hasNext()) {
            assertEquals(count, iterator.getCount());
            int[] counts = iterator.getCounts();
            assertEquals(counts[0], iterator.getCount(0));
            assertEquals(counts[1], iterator.getCount(1));
            assertEquals(counts[2], iterator.getCount(2));
            count++;
            iterator.next();
        }

        assertEquals(counter.getSize(), count);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        int[] counts = counter.getCounts(11);
        assertArrayEquals(new int[]{1, 3, 0}, counts);

        counts = counter.getCounts(0);
        assertArrayEquals(new int[]{0, 0, 0}, counts);

        counts = counter.getCounts(23);
        assertArrayEquals(new int[]{1, 3, 2}, counts);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        int index = counter.getCount(1, 3, 2);
        assertEquals(23, index);

        index = counter.getCount(0, 0, 0);
        assertEquals(0, index);

        try {
            counter.getCount(2, 3, 4);
            fail("Should have thrown OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Success
        }
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
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        assertEquals("[0][0][0]", counter.toString());

        counter.iterator().next();
        assertEquals("[0][0][1]", counter.toString());
    }

}
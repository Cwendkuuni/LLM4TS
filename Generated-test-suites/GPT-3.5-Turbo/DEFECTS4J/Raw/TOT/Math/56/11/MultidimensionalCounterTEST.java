import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Typical use case
        int[] result = counter.getCounts(23);
        assertEquals(1, result[0]);
        assertEquals(3, result[1]);
        assertEquals(2, result[2]);

        // Edge case
        result = counter.getCounts(0);
        assertEquals(0, result[0]);
        assertEquals(0, result[1]);
        assertEquals(0, result[2]);

        // Error scenario
        try {
            result = counter.getCounts(-1);
            fail("Should have thrown an OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception thrown as expected
        }

        try {
            result = counter.getCounts(24);
            fail("Should have thrown an OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        // Typical use case
        int result = counter.getCount(1, 3, 1);
        assertEquals(23, result);

        // Edge case
        result = counter.getCount(0, 0, 0);
        assertEquals(0, result);

        // Error scenario
        try {
            result = counter.getCount(-1, 2, 1);
            fail("Should have thrown an OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception thrown as expected
        }

        try {
            result = counter.getCount(2, 5, 1);
            fail("Should have thrown an OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception thrown as expected
        }

        try {
            result = counter.getCount(1, 3, -1);
            fail("Should have thrown an OutOfRangeException");
        } catch (OutOfRangeException e) {
            // Exception thrown as expected
        }
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        int result = counter.getSize();
        assertEquals(24, result);
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        int[] result = counter.getSizes();
        assertEquals(2, result[0]);
        assertEquals(4, result[1]);
        assertEquals(3, result[2]);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        // Typical use case
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertEquals(0, iterator.getCount());
        int[] counts = iterator.getCounts();
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());
        assertEquals(1, iterator.getCount());
        counts = iterator.getCounts();
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(1, counts[2]);

        // Edge case
        for (int i = 0; i < 21; i++) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }
        assertEquals(22, iterator.next().intValue());

        assertFalse(iterator.hasNext());
        counts = iterator.getCounts();
        assertEquals(1, counts[0]);
        assertEquals(3, counts[1]);
        assertEquals(2, counts[2]);
    }
}
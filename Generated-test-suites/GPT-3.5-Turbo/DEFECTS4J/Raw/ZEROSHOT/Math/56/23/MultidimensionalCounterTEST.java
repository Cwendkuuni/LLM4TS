import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {
    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        // Set up a counter with dimensions: 2, 4, 3
        counter = new MultidimensionalCounter(2, 4, 3);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        int[] counts = counter.getCounts(15);
        assertEquals(1, counts[0]);
        assertEquals(3, counts[1]);
        assertEquals(1, counts[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        counter.getCounts(25);
    }

    @Test
    public void testGetCount() {
        int count = counter.getCount(1, 3, 1);
        assertEquals(15, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        counter.getCount(1, 3);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        counter.getCount(2, 5, 1);
    }

    @Test
    public void testIterator() {
        int[] expectedCounts = {0, 0, 0};
        int expectedCount = 0;
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(expectedCount, iterator.getCount());
        assertArrayEquals(expectedCounts, iterator.getCounts());

        for (int i = 0; i < 5; i++) {
            iterator.next();
            expectedCount++;
            expectedCounts[2]++;
        }

        assertTrue(iterator.hasNext());
        assertEquals(expectedCount, iterator.getCount());
        assertArrayEquals(expectedCounts, iterator.getCounts());

        for (int i = 0; i < 4; i++) {
            iterator.next();
            expectedCount++;
            expectedCounts[2] = 0;
            expectedCounts[1]++;
        }

        assertTrue(iterator.hasNext());
        assertEquals(expectedCount, iterator.getCount());
        assertArrayEquals(expectedCounts, iterator.getCounts());

        for (int i = 0; i < 2; i++) {
            iterator.next();
            expectedCount++;
            expectedCounts[1] = 0;
            expectedCounts[0]++;
        }

        assertEquals(23, iterator.next().intValue());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testToString() {
        assertEquals("[0][0][0]", counter.toString());
    }
}
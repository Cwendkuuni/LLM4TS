import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MultidimensionalCounter;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter multidimensionalCounter;

    @Before
    public void setUp() throws NotStrictlyPositiveException {
        int[] size = {3, 4, 2};
        multidimensionalCounter = new MultidimensionalCounter(size);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, multidimensionalCounter.getDimension());
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        assertArrayEquals(new int[]{0, 0, 0}, multidimensionalCounter.getCounts(0));
        assertArrayEquals(new int[]{1, 0, 0}, multidimensionalCounter.getCounts(4));
        assertArrayEquals(new int[]{2, 3, 1}, multidimensionalCounter.getCounts(21));
        assertThrows(OutOfRangeException.class, () -> {
            multidimensionalCounter.getCounts(-1);
        });
        assertThrows(OutOfRangeException.class, () -> {
            multidimensionalCounter.getCounts(22);
        });
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        assertEquals(0, multidimensionalCounter.getCount(0, 0, 0));
        assertEquals(1, multidimensionalCounter.getCount(0, 0, 1));
        assertEquals(10, multidimensionalCounter.getCount(2, 2, 0));
        assertEquals(21, multidimensionalCounter.getCount(2, 3, 1));
        assertThrows(DimensionMismatchException.class, () -> {
            multidimensionalCounter.getCount(0, 0);
        });
        assertThrows(DimensionMismatchException.class, () -> {
            multidimensionalCounter.getCount(0, 0, 0, 0);
        });
        assertThrows(OutOfRangeException.class, () -> {
            multidimensionalCounter.getCount(1, 2, 3);
        });
        assertThrows(OutOfRangeException.class, () -> {
            multidimensionalCounter.getCount(0, 0, -1);
        });
        assertThrows(OutOfRangeException.class, () -> {
            multidimensionalCounter.getCount(0, 0, 2);
        });
    }

    @Test
    public void testGetSize() {
        assertEquals(24, multidimensionalCounter.getSize());
    }

    @Test
    public void testGetSizes() {
        assertArrayEquals(new int[]{3, 4, 2}, multidimensionalCounter.getSizes());
    }

    @Test
    public void testToString() {
        assertEquals("[0][0][0]", multidimensionalCounter.toString());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = multidimensionalCounter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertEquals(0, iterator.getCount());
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        assertEquals(0, iterator.getCount(0));
        assertEquals(0, iterator.getCount(1));
        assertEquals(0, iterator.getCount(2));
        for (int i = 1; i < 24; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next().intValue());
        }
        assertFalse(iterator.hasNext());
        assertThrows(UnsupportedOperationException.class, iterator::remove);
    }
}
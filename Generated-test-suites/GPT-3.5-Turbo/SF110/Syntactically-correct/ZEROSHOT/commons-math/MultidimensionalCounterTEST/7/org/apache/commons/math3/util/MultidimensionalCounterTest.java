import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

import org.apache.commons.math3.exception.*;
import java.util.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter mc;

    @Before
    public void setUp() throws Exception {
        mc = new MultidimensionalCounter(3, 4);
    }

    @Test
    public void testGetDimension() {
        assertEquals(2, mc.getDimension());
    }

    @Test
    public void testGetCounts() {
        try {
            int[] counts = mc.getCounts(7);
            assertEquals(2, counts.length);
            assertEquals(2, counts[0]);
            assertEquals(3, counts[1]);
        } catch (OutOfRangeException e) {
            fail("OutOfRangeException should not be thrown");
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRangeException() throws OutOfRangeException {
        mc.getCounts(10);
    }

    @Test
    public void testGetCount() {
        try {
            int count = mc.getCount(2, 3);
            assertEquals(11, count);
        } catch (OutOfRangeException | DimensionMismatchException e) {
            fail("OutOfRangeException or DimensionMismatchException should not be thrown");
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatchException() throws DimensionMismatchException, OutOfRangeException {
        mc.getCount(2, 3, 4);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeException() throws DimensionMismatchException, OutOfRangeException {
        mc.getCount(2, 5);
    }

    @Test
    public void testGetSize() {
        assertEquals(12, mc.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] sizes = mc.getSizes();
        assertEquals(2, sizes.length);
        assertEquals(3, sizes[0]);
        assertEquals(4, sizes[1]);
    }

    @Test
    public void testToString() {
        String expected = "[0][1][2][3][4][5][6][7][8][9][10][11]";
        assertEquals(expected, mc.toString());
    }

    @Test
    public void testIterator_hasNext() {
        MultidimensionalCounter.Iterator iterator = mc.iterator();
        assertTrue(iterator.hasNext());
    }

    @Test
    public void testIterator_next() {
        MultidimensionalCounter.Iterator iterator = mc.iterator();
        assertEquals(0, (int) iterator.next());
    }

    @Test
    public void testIterator_getCount() {
        MultidimensionalCounter.Iterator iterator = mc.iterator();
        assertEquals(-1, iterator.getCount());
    }

    @Test
    public void testIterator_getCounts() {
        MultidimensionalCounter.Iterator iterator = mc.iterator();
        int[] counts = iterator.getCounts();
        assertEquals(2, counts.length);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
    }

    @Test
    public void testIterator_getCountDim() {
        MultidimensionalCounter.Iterator iterator = mc.iterator();
        assertEquals(-1, iterator.getCount(0));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_remove() {
        MultidimensionalCounter.Iterator iterator = mc.iterator();
        iterator.remove();
    }
    
}
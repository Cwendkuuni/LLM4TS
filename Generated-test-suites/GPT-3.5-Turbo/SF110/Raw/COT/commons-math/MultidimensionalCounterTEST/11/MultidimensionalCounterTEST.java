
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.junit.Before;
import org.junit.Test;

import java.util.Iterator;

import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setUp() {
        int[] sizes = {2, 3, 4};
        try {
            counter = new MultidimensionalCounter(sizes);
        } catch (NotStrictlyPositiveException e) {
            fail("Failed to create MultidimensionalCounter object");
        }
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetCounts() {
        try {
            int[] counts = counter.getCounts(0);
            assertArrayEquals(new int[] {0, 0, 0}, counts);
        } catch (OutOfRangeException e) {
            fail("Failed to get counts");
        }
    }

    @Test
    public void testGetCountsOutOfRange() {
        try {
            counter.getCounts(-1);
            fail("Expected OutOfRangeException was not thrown");
        } catch (OutOfRangeException e) {
            // Exception was expected
        }
    }

    @Test
    public void testGetCount() {
        try {
            int count = counter.getCount(0, 0, 0);
            assertEquals(0, count);
        } catch (OutOfRangeException | DimensionMismatchException e) {
            fail("Failed to get count");
        }
    }

    @Test
    public void testGetCountOutOfRange() {
        try {
            counter.getCount(-1, 0, 0);
            fail("Expected OutOfRangeException was not thrown");
        } catch (OutOfRangeException e) {
            // Exception was expected
        } catch (DimensionMismatchException e) {
            fail("Unexpected DimensionMismatchException was thrown");
        }
    }

    @Test
    public void testGetCountDimensionMismatch() {
        try {
            counter.getCount(0, 0);
            fail("Expected DimensionMismatchException was not thrown");
        } catch (DimensionMismatchException e) {
            // Exception was expected
        } catch (OutOfRangeException e) {
            fail("Unexpected OutOfRangeException was thrown");
        }
    }

    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] sizes = counter.getSizes();
        assertArrayEquals(new int[] {2, 3, 4}, sizes);
    }

    @Test
    public void testToString() {
        String result = counter.toString();
        assertEquals("[0][0][0]", result); 
    }

    @Test
    public void testIterator() {
        Iterator<Integer> iterator = counter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(0), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(1), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(2), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(3), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(4), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(5), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(6), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(7), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(8), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(9), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(10), iterator.next());
        assertTrue(iterator.hasNext());
        assertEquals(Integer.valueOf(11), iterator.next());
        assertFalse(iterator.hasNext());
    }
}

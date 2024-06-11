import org.apache.commons.math.MathUtils;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        assertEquals(3, multidimensionalCounter.getDimension());
        assertArrayEquals(size, multidimensionalCounter.getSizes());
        assertEquals(24 * 4 * 3, multidimensionalCounter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidSizes() {
        int[] size = {-1, 2, 3};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testIterator() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = multidimensionalCounter.iterator();

        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next());

        for (int i = 1; i < size[0] * size[1] * size[2]; i++) {
            assertTrue(iterator.hasNext());
            assertEquals(i, iterator.next());
        }

        assertFalse(iterator.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        Iterator<Integer> iterator = multidimensionalCounter.iterator();
        iterator.next();
        iterator.remove();
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);

        for (int i = 0; i < multidimensionalCounter.getSize(); i++) {
            int[] counts = multidimensionalCounter.getCounts(i);
            int count = multidimensionalCounter.getCount(counts);
            assertEquals(i, count);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountInvalidIndex() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        multidimensionalCounter.getCount(-1);
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);

        for (int i = 0; i < multidimensionalCounter.getSize(); i++) {
            int[] counts = multidimensionalCounter.getCounts(i);
            int[] expectedCounts = MathUtils.range(size[last], 0, size[0]);
            assertArrayEquals(expectedCounts, counts);
            int count = multidimensionalCounter.getCount(counts);
            assertEquals(i, count);
        }

        int[] invalidCounts = new int[dimension + 1];
        try {
            multidimensionalCounter.getCounts(multidimensionalCounter.getSize() + 1);
            fail();
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsInvalidCounts() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter multidimensionalCounter = new MultidimensionalCounter(size);
        int[] invalidCounts = new int[dimension - 1];
        multidimensionalCounter.getCounts(invalidCounts);
    }
}
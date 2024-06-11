import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstruction() {
        int[] sizes = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(sizes, counter.getSizes());
        assertEquals(32, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructionNegativeSizes() {
        int[] sizes = new int[]{-1, 3, 4};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterConstructionZeroSizes() {
        int[] sizes = new int[]{0, 3, 4};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetCounts() {
        int[] sizes = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        int[] counts;

        counts = counter.getCounts(0);
        assertArrayEquals(new int[]{0, 0, 0}, counts);
        assertEquals(0, counter.getCount(counts));

        counts = counter.getCounts(counter.getSize() - 1);
        assertArrayEquals(new int[]{2, 3, 4}, counts);
        assertEquals(counter.getSize() - 1, counter.getCount(counts));

        counts = counter.getCounts(counter.getSize() + 1);
        Assert.assertNull(counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        int[] sizes = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        int[] counts = counter.getCounts(counter.getSize() + 1);
    }

    @Test
    public void testGetCount() {
        int[] sizes = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        for (int i = 0; i < counter.getSize(); i++) {
            int[] counts = counter.getCounts(i);
            int count = counter.getCount(counts);
            assertEquals(i, count);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        int count = counter.getCount(new int[]{3, 4, 5});
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] sizes = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        int count = counter.getCount(new int[]{1, 3});
    }

    @Test
    public void testIterator() {
        int[] sizes = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        Iterator iterator = counter.iterator();
        Integer next;

        next = iterator.next();
        assertEquals(0, next.intValue());

        next = iterator.next();
        assertEquals(1, next.intValue());

        next = iterator.next();
        assertEquals(2, next.intValue());

        next = iterator.next();
        assertEquals(3, next.intValue());

        next = iterator.next();
        assertEquals(4, next.intValue());

        next = iterator.next();
        assertEquals(5, next.intValue());

        next = iterator.next();
        assertEquals(6, next.intValue());

        next = iterator.next();
        assertEquals(7, next.intValue());

        next = iterator.next();
        assertEquals(8, next.intValue());

        next = iterator.next();
        assertEquals(9, next.intValue());

        next = iterator.next();
        assertEquals(10, next.intValue());

        next = iterator.next();
        assertEquals(11, next.intValue());

        next = iterator.next();
        assertEquals(12, next.intValue());

        next = iterator.next();
        assertEquals(13, next.intValue());

        next = iterator.next();
        assertEquals(14, next.intValue());

        next = iterator.next();
        assertEquals(15, next.intValue());

        next = iterator.next();
        assertEquals(16, next.intValue());

        next = iterator.next();
        assertEquals(17, next.intValue());

        next = iterator.next();
        assertEquals(18, next.intValue());

        next = iterator.next();
        assertEquals(19, next.intValue());

        next = iterator.next();
        assertEquals(20, next.intValue());

        next = iterator.next();
        assertEquals(21, next.intValue());

        next = iterator.next();
        assertEquals(22, next.intValue());

        next = iterator.next();
        assertEquals(23, next.intValue());

        next = iterator.next();
        assertEquals(24, next.intValue());

        next = iterator.next();
        assertEquals(25, next.intValue());

        next = iterator.next();
        assertEquals(26, next.intValue());

        next = iterator.next();
        assertEquals(27, next.intValue());

        next = iterator.next();
        assertEquals(28, next.intValue());

        next = iterator.next();
        assertEquals(29, next.intValue());

        next = iterator.next();
        assertEquals(30, next.intValue());

        next = iterator.next();
        assertEquals(31, next.intValue());

        next = iterator.next();
        assertEquals(31, next.intValue()); // Last one should return the same value as the previous one

        Assert.assertFalse(iterator.hasNext());

        next = iterator.next();
        Assert.assertNull(next);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] sizes = new int[]{2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        iterator.remove();
    }
}
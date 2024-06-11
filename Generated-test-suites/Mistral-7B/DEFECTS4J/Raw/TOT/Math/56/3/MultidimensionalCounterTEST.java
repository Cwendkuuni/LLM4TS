import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for MultidimensionalCounter.
 */
public class MultidimensionalCounterTest {

    /**
     * Test constructor with valid input.
     */
    @Test
    public void testConstructorValidInput() {
        final int[] size = {3, 4, 2};
        final MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Assert.assertEquals(3, mdc.getDimension());
        Assert.assertArrayEquals(size, mdc.getSizes());
        Assert.assertEquals(52, mdc.getSize());
    }

    /**
     * Test constructor with invalid input.
     */
    @Test(expected = org.apache.commons.math3.exception.NotStrictlyPositiveException.class)
    public void testConstructorInvalidInput() {
        final int[] size = {3, 0, 2};
        new MultidimensionalCounter(size);
    }

    /**
     * Test iterator.
     */
    @Test
    public void testIterator() {
        final int[] size = {3, 4, 2};
        final MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        final MultidimensionalCounter.Iterator it = mdc.iterator();

        Assert.assertTrue(it.hasNext());
        final int count = it.next();
        Assert.assertEquals(0, count);
        final int[] counts = it.getCounts();
        Assert.assertArrayEquals(new int[] {0, 0, 0}, counts);

        Assert.assertTrue(it.hasNext());
        final int nextCount = it.next();
        Assert.assertEquals(1, nextCount);
        final int[] nextCounts = it.getCounts();
        Assert.assertArrayEquals(new int[] {0, 0, 1}, nextCounts);

        Assert.assertTrue(it.hasNext());
        final int nextNextCount = it.next();
        Assert.assertEquals(2, nextNextCount);
        final int[] nextNextCounts = it.getCounts();
        Assert.assertArrayEquals(new int[] {0, 0, 2}, nextNextCounts);

        // Test getting count and counts for different dimensions
        for (int i = 0; i < size.length; i++) {
            final int dimension = i;
            Assert.assertEquals(0, it.getCount(i));
            Assert.assertArrayEquals(new int[] {0, 0, i}, it.getCounts(i));

            Assert.assertTrue(it.hasNext());
            final int nextCount = it.next().getCount(i);
            Assert.assertEquals(1, nextCount);
            Assert.assertArrayEquals(new int[] {0, 0, i + 1}, it.getCounts(i));
        }

        // Test getting count and counts for last dimension
        for (int i = size.length - 1; i >= 0; i--) {
            final int dimension = i;
            Assert.assertTrue(it.hasNext());
            final int nextCount = it.next().getCount(dimension);
            Assert.assertEquals(size[dimension] - 1, nextCount);
            Assert.assertArrayEquals(new int[] {i + 1, 0, 0}, it.getCounts(dimension));
        }

        Assert.assertFalse(it.hasNext());
    }

    /**
     * Test getting count and counts for out of range index.
     */
    @Test(expected = org.apache.commons.math3.exception.OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        final int[] size = {3, 4, 2};
        final MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(-1);
    }

    /**
     * Test getting count and counts for invalid dimension index.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetCountInvalidDimensionIndex() {
        final int[] size = {3, 4, 2};
        final MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCounts(size.length);
    }

    /**
     * Test getting size.
     */
    @Test
    public void testGetSize() {
        final int[] size = {3, 4, 2};
        final MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Assert.assertEquals(52, mdc.getSize());
    }

    /**
     * Test toString.
     */
    @Test
    public void testToString() {
        final int[] size = {3, 4, 2};
        final MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        final MultidimensionalCounter mdc2 = new MultidimensionalCounter(size);
        mdc.iterator().next();
        mdc2.iterator().next();
        mdc2.iterator().next();

        final String mdctest1 = mdc.toString();
        final String mdctest2 = mdc2.toString();

        Assert.assertEquals("[0]", mdctest1);
        Assert.assertEquals("[1]", mdctest2);
    }
}
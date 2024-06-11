import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    // Test MultidimensionalCounter constructor with positive sizes
    @Test
    public void testConstructor_PositiveSizes() {
        int[] sizes = {2, 4, 3};

        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        Assert.assertEquals(3, counter.getDimension());
        Assert.assertArrayEquals(new int[]{2, 4, 3}, counter.getSizes());
        Assert.assertEquals(2*4*3, counter.getSize());
    }

    // Test MultidimensionalCounter constructor with negative size
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NegativeSize() {
        int[] sizes = {2, 4, -3};

        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
    }

    // Test MultidimensionalCounter constructor with zero size
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_ZeroSize() {
        int[] sizes = {2, 4, 0};

        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
    }

    // Test iterator behavior
    @Test
    public void testIterator() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        MultidimensionalCounter.Iterator iterator = counter.iterator();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(new Integer(0), iterator.next());
        Assert.assertEquals(0, iterator.getCount());
        Assert.assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        Assert.assertEquals(0, iterator.getCount(0));
        Assert.assertEquals(0, iterator.getCount(1));
        Assert.assertEquals(0, iterator.getCount(2));

        for (int i = 1; i < 2*4*3; i++) {
            Assert.assertTrue(iterator.hasNext());
            iterator.next();
        }

        Assert.assertFalse(iterator.hasNext());
    }

    // Test getCounts with valid index inside the range
    @Test
    public void testGetCounts_ValidIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        int[] counts = counter.getCounts(23);

        Assert.assertArrayEquals(new int[]{1, 3, 2}, counts);
    }

    // Test getCounts with invalid index below the range
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_InvalidIndexBelowRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        counter.getCounts(-1);
    }

    // Test getCounts with invalid index above the range
    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_InvalidIndexAboveRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        counter.getCounts(24);
    }

    // Test getCount with valid indices
    @Test
    public void testGetCount_ValidIndices() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        int count = counter.getCount(1, 3, 2);

        Assert.assertEquals(23, count);
    }

    // Test getCount with invalid index due to incorrect dimension
    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_InvalidIndex_DimensionMismatch() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        counter.getCount(1, 3);
    }

    // Test getCount with invalid index below the range
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_InvalidIndexBelowRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        counter.getCount(1, 3, -1);
    }

    // Test getCount with invalid index above the range
    @Test(expected = OutOfRangeException.class)
    public void testGetCount_InvalidIndexAboveRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        counter.getCount(1, 3, 3);
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        String expected = "[0][0][0]";
        Assert.assertEquals(expected, counter.toString());
    }

}
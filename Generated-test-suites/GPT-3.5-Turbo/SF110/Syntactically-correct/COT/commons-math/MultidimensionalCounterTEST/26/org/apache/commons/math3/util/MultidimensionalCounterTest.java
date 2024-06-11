import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int dimension = counter.getDimension();
        Assert.assertEquals(2, dimension);
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] sizes = counter.getSizes();
        Assert.assertArrayEquals(new int[]{2, 3}, sizes);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int size = counter.getSize();
        Assert.assertEquals(6, size);
    }

    @Test
    public void testGetCounts() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int[] counts = counter.getCounts(3);
        Assert.assertArrayEquals(new int[]{1, 0}, counts);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsNegative() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() throws OutOfRangeException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCounts(7);
    }

    @Test
    public void testGetCount() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        int count = counter.getCount(1, 2);
        Assert.assertEquals(5, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(1, 2, 3);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() throws OutOfRangeException, DimensionMismatchException {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        counter.getCount(1, 5);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        Assert.assertEquals(6, count);
    }

    // Potential edge cases and exception handling scenarios:

    @Test(expected = NotStrictlyPositiveException.class)
    public void testInvalidSize() throws NotStrictlyPositiveException {
        new MultidimensionalCounter(-1, 2);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testRemoveUnsupportedOperation() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3);
        String str = counter.toString();
        Assert.assertEquals("[0][0]", str);
    }
}
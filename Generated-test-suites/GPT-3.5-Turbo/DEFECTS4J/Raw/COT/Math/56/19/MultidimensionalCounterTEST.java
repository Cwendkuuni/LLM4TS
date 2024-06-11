import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounter() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertEquals(2, counter.getDimension());
        Assert.assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
        Assert.assertEquals(24, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterWithInvalidSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(-2, 3, 4);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, (int) iterator.next());
        Assert.assertEquals(1, iterator.getCount(0));
        Assert.assertEquals(0, iterator.getCount(1));
        Assert.assertEquals(1, iterator.getCount());

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, (int) iterator.next());
        Assert.assertEquals(1, iterator.getCount(1));
        Assert.assertEquals(1, iterator.getCount());

        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        int[] counts0 = counter.getCounts(0);
        Assert.assertArrayEquals(new int[]{0, 0, 0}, counts0);

        int[] counts23 = counter.getCounts(23);
        Assert.assertArrayEquals(new int[]{1, 3, 2}, counts23);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsInvalidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = counter.getCounts(24);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertEquals(12, counter.getCount(1, 0, 0));
        Assert.assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWithInvalidDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(1, 3);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountWithInvalidIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(2, 0, 0);
    }
}
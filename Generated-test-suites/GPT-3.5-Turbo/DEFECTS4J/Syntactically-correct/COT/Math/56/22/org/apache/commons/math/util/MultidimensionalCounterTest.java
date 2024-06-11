import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        int count = 0;
        while (iterator.hasNext()) {
            Assert.assertEquals(count, iterator.next().intValue());
            count++;
        }
        Assert.assertEquals(24, count);
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        Assert.assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        Assert.assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        Assert.assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        Assert.assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
        Assert.assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(12));
        Assert.assertArrayEquals(new int[]{1, 3, 2}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCounts(24);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);

        Assert.assertEquals(0, counter.getCount(0, 0, 0));
        Assert.assertEquals(1, counter.getCount(0, 0, 1));
        Assert.assertEquals(2, counter.getCount(0, 0, 2));
        Assert.assertEquals(3, counter.getCount(0, 1, 0));
        Assert.assertEquals(12, counter.getCount(1, 0, 0));
        Assert.assertEquals(23, counter.getCount(1, 3, 2));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(0, 1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        counter.getCount(2, 0, 0);
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertArrayEquals(new int[]{2, 4, 3}, counter.getSizes());
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4, 3);
        Assert.assertEquals("[0][0][0]", counter.toString());
    }
}
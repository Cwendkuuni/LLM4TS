import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setup() {
        counter = new MultidimensionalCounter(2, 4, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_NotStrictlyPositiveException() {
        counter = new MultidimensionalCounter(-1, 1, 2);
    }

    @Test
    public void testGetDimension() {
        Assert.assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        Assert.assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] expectedSizes = {2, 4, 3};
        Assert.assertArrayEquals(expectedSizes, counter.getSizes());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.getCount());
        Assert.assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        Assert.assertEquals(0, iterator.getCount(0));
        Assert.assertEquals(0, iterator.getCount(1));
        Assert.assertEquals(0, iterator.getCount(2));
        Assert.assertEquals(0, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.getCount());
        Assert.assertArrayEquals(new int[]{0, 0, 1}, iterator.getCounts());
        Assert.assertEquals(0, iterator.getCount(0));
        Assert.assertEquals(0, iterator.getCount(1));
        Assert.assertEquals(1, iterator.getCount(2));
        // ...
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIterator_Remove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testGetCounts() {
        int[] indices1 = counter.getCounts(0);
        int[] expectedIndices1 = {0, 0, 0};
        Assert.assertArrayEquals(expectedIndices1, indices1);

        int[] indices2 = counter.getCounts(23);
        int[] expectedIndices2 = {1, 3, 2};
        Assert.assertArrayEquals(expectedIndices2, indices2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCounts_OutOfRangeException() {
        counter.getCounts(100);
    }

    @Test
    public void testGetCount() {
        int count1 = counter.getCount(0, 0, 0);
        Assert.assertEquals(0, count1);

        int count2 = counter.getCount(1, 3, 2);
        Assert.assertEquals(23, count2);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCount_DimensionMismatchException() {
        counter.getCount(0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCount_OutOfRangeException() {
        counter.getCount(2, 0, 0);
    }

}
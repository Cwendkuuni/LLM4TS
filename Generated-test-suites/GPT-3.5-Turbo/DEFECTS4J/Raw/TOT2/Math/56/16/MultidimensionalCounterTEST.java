import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.util.MultidimensionalCounter;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        Assert.assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 4);
        Assert.assertEquals(8, counter.getSize());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int[] counts = counter.getCounts(8);
        Assert.assertArrayEquals(new int[]{1, 0, 2}, counts);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        int count = counter.getCount(1, 1, 3);
        Assert.assertEquals(10, count);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(1, 1);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        int count = 0;
        while(iterator.hasNext()) {
            Assert.assertEquals(count, iterator.getCount());
            count++;
            iterator.next();
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsOutOfRangeException() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 2);
        counter.getCounts(5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorIllegalArgumentException() {
        new MultidimensionalCounter(-1, 2, 3);
    }
}
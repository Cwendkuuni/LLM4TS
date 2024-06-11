import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.List;
import java.util.Arrays;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Assert.assertEquals(sizes.length, counter.getDimension());
        Assert.assertArrayEquals(sizes, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizes() {
        int[] sizes = {-1, 2, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSizes() {
        int[] sizes = {0, 2, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] index = {0, 0, 0};
        int[] expected = {0, 0, 0};
        Assert.assertArrayEquals(expected, counter.getCounts(0));

        index = new int[]{0, 1, 0};
        expected = new int[]{0, 1, 0};
        Assert.assertArrayEquals(expected, counter.getCounts(12));

        index = new int[]{1, 0, 0};
        expected = new int[]{1, 0, 0};
        Assert.assertArrayEquals(expected, counter.getCounts(1));

        index = new int[]{1, 3, 2};
        expected = new int[]{1, 3, 2};
        Assert.assertArrayEquals(expected, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = -1;
        counter.getCounts(index);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange2() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int index = 25;
        counter.getCounts(index);
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();

        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.next());

        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            Assert.assertTrue(iterator.hasNext());
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();

        for (int i = 0; i < sizes[0] * sizes[1] * sizes[2]; i++) {
            int next = iterator.next();
            int[] counts = counter.getCounts(next);
            List<Integer> expected = Arrays.asList(counts);
            List<Integer> actual = Arrays.asList(iterator.getCounts());
            Assert.assertEquals(expected, actual);
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        String expected = "[0][0][0]";
        String actual = counter.toString();
        Assert.assertEquals(expected, actual);
    }
}
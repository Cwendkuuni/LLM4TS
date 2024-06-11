import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Assert.assertEquals(2 * 4 * 3, counter.getSize());
        Assert.assertArrayEquals(size, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        int[] size = {-1, 2, 3};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testIterator() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator it = counter.iterator();
        List<Integer> expectedCounts = Arrays.asList(0, 0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3);
        for (int i = 0; i < 12; i++) {
            Assert.assertEquals(expectedCounts.get(i), it.next());
            Assert.assertEquals(i + 1, it.getCount());
            Assert.assertArrayEquals(Arrays.asList(i / (size[0] * size[1]), i % (size[0] * size[1]) / size[2], i % (size[0] * size[1]) % size[2]).toArray(), it.getCounts());
        }
        Assert.assertFalse(it.hasNext());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetCountNegativeIndex() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetCountOutOfRangeIndex() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(size[0] * size[1] * size[2]);
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] expectedCounts = {0, 0, 1, 1, 1, 2, 2, 2, 3, 3, 3};
        for (int i = 0; i < 12; i++) {
            int[] actualCounts = counter.getCounts(i);
            Assert.assertArrayEquals(expectedCounts[i], actualCounts[last]);
            Arrays.sort(actualCounts);
            Arrays.sort(expectedCounts);
            Assert.assertArrayEquals(expectedCounts, actualCounts);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] size = {2, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(3, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRangeDimension() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCount(2, 5);
    }

    @Test
    public void testToString() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        for (int i = 0; i < 12; i++) {
            String expected = "[";
            for (int j = 0; j < dimension; j++) {
                expected += Precision.formatInt(getCount(i, size), 10);
                if (j < dimension - 1) {
                    expected += ", ";
                }
            }
            expected += "]";
            Assert.assertEquals(expected, counter.toString());
            it.next();
        }
        Assert.assertFalse(it.hasNext());
    }

    private int getCount(int index, int[] size) {
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        return counter.getCount(index);
    }
}
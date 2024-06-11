import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Assert.assertEquals(2 * 4 * 3, mdc.getSize());
        Assert.assertArrayEquals(size, mdc.getSizes());
    }

    @Test
    public void testIteratorBasicFunctionality() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator<Integer> it = mdc.iterator();

        Assert.assertTrue(it.hasNext());
        Assert.assertEquals(0, it.next().intValue());

        for (int i = 0; i < 2 * 4 * 3; i++) {
            Assert.assertTrue(it.hasNext());
            int next = it.next().intValue();
            Assert.assertEquals(i + 1, next + 1);
        }

        Assert.assertFalse(it.hasNext());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator<Integer> it = mdc.iterator();
        it.next();
        it.remove();
    }

    @Test
    public void testGetCounts() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator<Integer> it = mdc.iterator();

        int[] expected = new int[]{0, 0, 0};
        int count = it.next().intValue();
        int[] actual = mdc.getCounts(count);
        Assert.assertArrayEquals(expected, actual);

        for (int i = 1; i < 2 * 4 * 3; i++) {
            count = it.next().intValue();
            expected = new int[]{i / (2 * 3), i / 3 % 2, i % 3};
            actual = mdc.getCounts(count);
            Assert.assertArrayEquals(expected, actual);
        }

        Assert.assertArrayEquals(new int[]{1, 1, 1}, mdc.getCounts(2 * 4 * 3 - 1));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWrongDimension() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCounts(mdc.getSize() + 1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfBounds() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountLastIndex() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        mdc.getCount(mdc.getSize() - 1);
    }

    @Test
    public void testToString() {
        int[] size = new int[]{2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator<Integer> it = mdc.iterator();

        String expected = "[0]";
        String actual = mdc.toString();
        Assert.assertEquals(expected, actual);

        for (int i = 1; i < 2 * 4 * 3; i++) {
            int count = it.next().intValue();
            expected = String.format("[%d]", count);
            actual = mdc.toString();
            Assert.assertEquals(expected, actual);
            mdc.next();
        }

        expected = "[25]";
        actual = mdc.toString();
        Assert.assertEquals(expected, actual);
    }
}
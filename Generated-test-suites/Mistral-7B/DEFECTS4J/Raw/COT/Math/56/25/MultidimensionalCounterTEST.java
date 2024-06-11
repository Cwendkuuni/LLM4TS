import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Assert.assertEquals(6 * 4 * 3, counter.getSize());
        Assert.assertArrayEquals(size, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSize() {
        int[] size = {-2, 4, 3};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSize() {
        int[] size = {0, 4, 3};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testIterator() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
        int count = iterator.next();
        int[] counts = iterator.getCounts();
        Assert.assertEquals(0, counts[0]);
        Assert.assertEquals(0, counts[1]);
        Assert.assertEquals(0, counts[2]);
        Assert.assertEquals(0, count);
        Assert.assertTrue(iterator.hasNext());
        counts = iterator.getCounts();
        Assert.assertEquals(0, counts[0]);
        Assert.assertEquals(0, counts[1]);
        Assert.assertEquals(1, counts[2]);
        Assert.assertEquals(1, count);
        Assert.assertTrue(iterator.hasNext());
        // ... continue this process to test all possible states
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetCountOutOfBounds() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int index = size[0] * size[1] * size[2];
        counter.getCount(new int[3]);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountDimensionMismatch() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] c = {2, 4, 5};
        counter.getCount(c);
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] c = {1, 1, 1};
        int count = counter.getCount(c);
        int[] counts = counter.getCounts(count);
        Assert.assertArrayEquals(c, counts);
    }

    @Test
    public void testToString() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        String toString = counter.toString();
        String expected = "[0][0][0]";
        for (int i = 0; i < counter.getSize(); i++) {
            int[] counts = counter.getCounts(i);
            String countString = "[";
            for (int j = 0; j < multidimensionalCounter.getDimension(); j++) {
                countString += counts[j] + "]";
                if (j < multidimensionalCounter.getDimension() - 1) {
                    countString += ",";
                }
            }
            toString = toString.replaceAll("[0-9]+" + i + "[}]", countString + "]");
        }
        Assert.assertEquals(toString, expected);
    }
}
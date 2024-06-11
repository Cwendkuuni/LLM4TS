import org.apache.commons.math3.util.MultidimensionalCounter;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructorEmpty() {
        MultidimensionalCounter counter = new MultidimensionalCounter();
        Assert.assertEquals(0, counter.getDimension());
        Assert.assertEquals(0, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] sizes = {-1, 2, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
    }

    @Test
    public void testConstructorOneDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{3});
        Assert.assertEquals(1, counter.getDimension());
        Assert.assertEquals(3, counter.getSize());
    }

    @Test
    public void testConstructorTwoDimensions() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3});
        Assert.assertEquals(2, counter.getDimension());
        Assert.assertEquals(6, counter.getSize());
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3});
        Iterator iterator = counter.iterator();
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(0, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(1, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(2, iterator.next().intValue());
        Assert.assertTrue(iterator.hasNext());
        Assert.assertEquals(3, iterator.next().intValue());
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3});
        int[] counts = counter.getCounts(0);
        Assert.assertEquals(0, counts[0]);
        Assert.assertEquals(0, counts[1]);

        counts = counter.getCounts(3);
        Assert.assertEquals(2, counts[0]);
        Assert.assertEquals(2, counts[1]);
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3});
        Assert.assertEquals(0, counter.getCount(0, 0));
        Assert.assertEquals(1, counter.getCount(0, 1));
        Assert.assertEquals(2, counter.getCount(0, 2));
        Assert.assertEquals(3, counter.getCount(1, 0));
        Assert.assertEquals(4, counter.getCount(1, 1));
        Assert.assertEquals(5, counter.getCount(1, 2));
        Assert.assertEquals(6, counter.getCount(2, 0));
        Assert.assertEquals(7, counter.getCount(2, 1));
        Assert.assertEquals(3, counter.getCount(2, 2));
    }

    @Test
    public void testSize() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3});
        Assert.assertEquals(6, counter.getSize());
    }

    @Test
    public void testSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3});
        int[] sizes = counter.getSizes();
        Assert.assertArrayEquals(new int[]{2, 3}, sizes);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWrongDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3});
        counter.getCount(new int[]{3, 3});
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3});
        counter.getCount(new int[]{3, 3});
    }

    @Test
    public void testToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(new int[]{2, 3});
        String expected = "[0][0][0]";
        String actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[0][0][1]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[0][0][2]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[0][1][0]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[0][1][1]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[0][1][2]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[1][0][0]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[1][0][1]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[1][0][2]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[1][1][0]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[1][1][1]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[1][1][2]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[2][0][0]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[2][0][1]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[2][0][2]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[2][1][0]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[2][1][1]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);

        counter.next();
        expected = "[2][1][2]";
        actual = counter.toString();
        Assert.assertEquals(expected, actual);
    }
}
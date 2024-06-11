import org.junit.Test;
import static org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounter() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(new int[]{2, 3, 4}, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterWithNegativeSizes() {
        new MultidimensionalCounter(-2, 3, 4);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterWithZeroSizes() {
        new MultidimensionalCounter(2, 0, 4);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        MultidimensionalCounter.Iterator iterator = counter.iterator();

        int count = 0;
        while (iterator.hasNext()) {
            int unidimensionalCount = iterator.next();
            int[] multidimensionalCounts = iterator.getCounts();

            assertEquals(count, unidimensionalCount);
            assertArrayEquals(getExpectedMultidimensionalCounts(count), multidimensionalCounts);

            count++;
        }
    }

    @Test
    public void testConverterToUnidimensionalCounter() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 1, 0));
        assertEquals(4, counter.getCount(0, 1, 1));
        assertEquals(5, counter.getCount(0, 1, 2));
        assertEquals(6, counter.getCount(0, 2, 0));
        assertEquals(7, counter.getCount(0, 2, 1));
        assertEquals(8, counter.getCount(0, 2, 2));
        assertEquals(9, counter.getCount(1, 0, 0));
        assertEquals(10, counter.getCount(1, 0, 1));
        assertEquals(11, counter.getCount(1, 0, 2));
        assertEquals(12, counter.getCount(1, 1, 0));
        assertEquals(13, counter.getCount(1, 1, 1));
        assertEquals(14, counter.getCount(1, 1, 2));
        assertEquals(15, counter.getCount(1, 2, 0));
        assertEquals(16, counter.getCount(1, 2, 1));
        assertEquals(17, counter.getCount(1, 2, 2));
        assertEquals(18, counter.getCount(2, 0, 0));
        assertEquals(19, counter.getCount(2, 0, 1));
        assertEquals(20, counter.getCount(2, 0, 2));
        assertEquals(21, counter.getCount(2, 1, 0));
        assertEquals(22, counter.getCount(2, 1, 1));
        assertEquals(23, counter.getCount(2, 1, 2));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testConverterToUnidimensionalCounterWithInvalidDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(0, 0);
    }

    @Test(expected = OutOfRangeException.class)
    public void testConverterToUnidimensionalCounterWithOutOfRangeIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCount(0, 0, 5);
    }

    @Test
    public void testConverterToMultidimensionalCounter() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);

        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(3));
        assertArrayEquals(new int[]{0, 1, 1}, counter.getCounts(4));
        assertArrayEquals(new int[]{0, 1, 2}, counter.getCounts(5));
        assertArrayEquals(new int[]{0, 2, 0}, counter.getCounts(6));
        assertArrayEquals(new int[]{0, 2, 1}, counter.getCounts(7));
        assertArrayEquals(new int[]{0, 2, 2}, counter.getCounts(8));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(9));
        assertArrayEquals(new int[]{1, 0, 1}, counter.getCounts(10));
        assertArrayEquals(new int[]{1, 0, 2}, counter.getCounts(11));
        assertArrayEquals(new int[]{1, 1, 0}, counter.getCounts(12));
        assertArrayEquals(new int[]{1, 1, 1}, counter.getCounts(13));
        assertArrayEquals(new int[]{1, 1, 2}, counter.getCounts(14));
        assertArrayEquals(new int[]{1, 2, 0}, counter.getCounts(15));
        assertArrayEquals(new int[]{1, 2, 1}, counter.getCounts(16));
        assertArrayEquals(new int[]{1, 2, 2}, counter.getCounts(17));
        assertArrayEquals(new int[]{2, 0, 0}, counter.getCounts(18));
        assertArrayEquals(new int[]{2, 0, 1}, counter.getCounts(19));
        assertArrayEquals(new int[]{2, 0, 2}, counter.getCounts(20));
        assertArrayEquals(new int[]{2, 1, 0}, counter.getCounts(21));
        assertArrayEquals(new int[]{2, 1, 1}, counter.getCounts(22));
        assertArrayEquals(new int[]{2, 1, 2}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testConverterToMultidimensionalCounterWithOutOfRangeIndex() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        counter.getCounts(24);
    }

    private int[] getExpectedMultidimensionalCounts(int index) {
        int[] counts = new int[3];
        counts[0] = index / 12;
        index %= 12;
        counts[1] = index / 4;
        index %= 4;
        counts[2] = index;
        return counts;
    }

}
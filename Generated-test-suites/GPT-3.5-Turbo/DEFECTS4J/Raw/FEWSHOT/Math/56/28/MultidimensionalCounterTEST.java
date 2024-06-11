import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {
    private MultidimensionalCounter counter;
    private final int[] size = {2, 3, 4};

    @Before
    public void setUp() {
        counter = new MultidimensionalCounter(size);
    }

    @Test
    public void testConstructor() {
        assertEquals(size.length, counter.getDimension());
        assertArrayEquals(size, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeSize() {
        new MultidimensionalCounter(-1, 2, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithZeroSize() {
        new MultidimensionalCounter(0, 2, 3);
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(-1, iterator.getCount());

        int count = 0;
        while (iterator.hasNext()) {
            count++;
            iterator.next();
        }

        assertEquals(counter.getSize(), count);
    }

    @Test
    public void testGetCounts() {
        assertArrayEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertArrayEquals(new int[]{0, 0, 1}, counter.getCounts(1));
        assertArrayEquals(new int[]{0, 0, 2}, counter.getCounts(2));
        assertArrayEquals(new int[]{0, 0, 3}, counter.getCounts(3));
        assertArrayEquals(new int[]{0, 1, 0}, counter.getCounts(4));
        assertArrayEquals(new int[]{0, 1, 1}, counter.getCounts(5));
        assertArrayEquals(new int[]{0, 1, 2}, counter.getCounts(6));
        assertArrayEquals(new int[]{0, 1, 3}, counter.getCounts(7));
        assertArrayEquals(new int[]{0, 2, 0}, counter.getCounts(8));
        assertArrayEquals(new int[]{0, 2, 1}, counter.getCounts(9));
        assertArrayEquals(new int[]{0, 2, 2}, counter.getCounts(10));
        assertArrayEquals(new int[]{0, 2, 3}, counter.getCounts(11));
        assertArrayEquals(new int[]{1, 0, 0}, counter.getCounts(12));
        assertArrayEquals(new int[]{1, 0, 1}, counter.getCounts(13));
        assertArrayEquals(new int[]{1, 0, 2}, counter.getCounts(14));
        assertArrayEquals(new int[]{1, 0, 3}, counter.getCounts(15));
        assertArrayEquals(new int[]{1, 1, 0}, counter.getCounts(16));
        assertArrayEquals(new int[]{1, 1, 1}, counter.getCounts(17));
        assertArrayEquals(new int[]{1, 1, 2}, counter.getCounts(18));
        assertArrayEquals(new int[]{1, 1, 3}, counter.getCounts(19));
        assertArrayEquals(new int[]{1, 2, 0}, counter.getCounts(20));
        assertArrayEquals(new int[]{1, 2, 1}, counter.getCounts(21));
        assertArrayEquals(new int[]{1, 2, 2}, counter.getCounts(22));
        assertArrayEquals(new int[]{1, 2, 3}, counter.getCounts(23));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsIndexLowerBound() {
        counter.getCounts(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountsIndexHigherBound() {
        counter.getCounts(counter.getSize());
    }

    @Test
    public void testGetCount() {
        assertEquals(0, counter.getCount(0, 0, 0));
        assertEquals(1, counter.getCount(0, 0, 1));
        assertEquals(2, counter.getCount(0, 0, 2));
        assertEquals(3, counter.getCount(0, 0, 3));
        assertEquals(4, counter.getCount(0, 1, 0));
        assertEquals(5, counter.getCount(0, 1, 1));
        assertEquals(6, counter.getCount(0, 1, 2));
        assertEquals(7, counter.getCount(0, 1, 3));
        assertEquals(8, counter.getCount(0, 2, 0));
        assertEquals(9, counter.getCount(0, 2, 1));
        assertEquals(10, counter.getCount(0, 2, 2));
        assertEquals(11, counter.getCount(0, 2, 3));
        assertEquals(12, counter.getCount(1, 0, 0));
        assertEquals(13, counter.getCount(1, 0, 1));
        assertEquals(14, counter.getCount(1, 0, 2));
        assertEquals(15, counter.getCount(1, 0, 3));
        assertEquals(16, counter.getCount(1, 1, 0));
        assertEquals(17, counter.getCount(1, 1, 1));
        assertEquals(18, counter.getCount(1, 1, 2));
        assertEquals(19, counter.getCount(1, 1, 3));
        assertEquals(20, counter.getCount(1, 2, 0));
        assertEquals(21, counter.getCount(1, 2, 1));
        assertEquals(22, counter.getCount(1, 2, 2));
        assertEquals(23, counter.getCount(1, 2, 3));
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountInvalidDimensionSize() {
        counter.getCount(1, 2);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountInvalidIndex() {
        counter.getCount(0, 0, -1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountInvalidIndexOutOfRange() {
        counter.getCount(0, 0, 4);
    }

    @Test
    public void testToString() {
        assertEquals("[0][0][0]", counter.toString());
    }
}
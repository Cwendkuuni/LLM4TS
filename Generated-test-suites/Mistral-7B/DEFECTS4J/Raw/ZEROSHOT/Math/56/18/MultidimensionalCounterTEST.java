import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.Assert.assertEquals;
import org.junit.Assert.assertArrayEquals;
import org.junit.Assert.assertFalse;
import org.junit.Assert.assertNull;
import org.junit.Assert.assertTrue;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertArrayEquals(sizes, counter.getSizes());
        assertEquals(24, counter.getSize());
        assertEquals(new int[]{0, 0, 0}, counter.getCounts(0));
        assertEquals(new int[]{1, 0, 0}, counter.getCounts(12));
        assertEquals(new int[]{1, 3, 2}, counter.getCounts(23));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSize() {
        int[] sizes = {-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSize() {
        int[] sizes = {0, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIterator() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        assertEquals(0, iterator.next().intValue());
        assertArrayEquals(new int[]{0, 0, 0}, iterator.getCounts());
        assertTrue(iterator.hasNext());
        assertEquals(1, iterator.next().intValue());
        assertArrayEquals(new int[]{0, 0, 1}, iterator.getCounts());
        assertTrue(iterator.hasNext());
        // ...
        assertTrue(iterator.hasNext());
        assertEquals(23, iterator.next().intValue());
        assertArrayEquals(new int[]{1, 3, 2}, iterator.getCounts());
        assertFalse(iterator.hasNext());
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(25);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(new int[]{4, 3, 2});
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals("[0]", counter.toString());
        counter.next();
        assertEquals("[0, 0]", counter.toString());
        counter.next();
        assertEquals("[0, 0, 1]", counter.toString());
        counter.next();
        assertEquals("[0, 1, 0]", counter.toString());
        // ...
        counter.next();
        assertEquals("[1, 3, 2]", counter.toString());
    }
}
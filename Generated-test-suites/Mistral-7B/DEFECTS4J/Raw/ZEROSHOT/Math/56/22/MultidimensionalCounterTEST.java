import org.apache.commons.math3.util.MathUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounter() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertEquals(2 * 4 * 3, counter.getSize());
        assertEquals(size, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        int[] size = {-1, 4, 3};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testGetCounts() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] expected = {0, 0, 0};
        int[] actual = counter.getCounts(0);
        assertArrayEquals(expected, actual);
        expected = new int[]{1, 0, 0};
        actual = counter.getCounts(12);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeException() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        counter.getCounts(-1);
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int expected = 0;
        int actual = counter.getCount(0, 0, 0);
        assertEquals(expected, actual);
        expected = 12;
        actual = counter.getCount(1, 0, 0);
        assertEquals(expected, actual);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] c = new int[1];
        counter.getCount(c);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeException1() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] c = {-1, 1, 1};
        counter.getCount(c);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeException2() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] c = {2, 5, 1};
        counter.getCount(c);
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        for (int i = 0; i < 2 * 4 * 3; i++) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetNext() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int expected = 0;
        int actual = iterator.next();
        assertEquals(expected, actual);
        int[] counts = iterator.getCounts();
        assertArrayEquals(new int[]{0, 0, 0}, counts);
        expected = 1;
        actual = iterator.next();
        assertEquals(expected, actual);
        counts = iterator.getCounts();
        assertArrayEquals(new int[]{1, 0, 0}, counts);
        // ...
        expected = 12;
        actual = iterator.next();
        assertEquals(expected, actual);
        counts = iterator.getCounts();
        assertArrayEquals(new int[]{1, 3, 2}, counts);
        assertNull(iterator.next());
    }

    @Test
    public void testIteratorGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int expected = 0;
        int actual = iterator.getCount();
        assertEquals(expected, actual);
        expected = 1;
        iterator.next();
        actual = iterator.getCount();
        assertEquals(expected, actual);
        // ...
        expected = 12;
        iterator.next();
        // ...
        assertNull(iterator.getCount());
    }

    @Test
    public void testToString() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        int expected = "[0]";
        String actual = counter.toString();
        assertEquals(expected, actual);
        iterator.next();
        expected = "[1, 0, 0]";
        actual = counter.toString();
        assertEquals(expected, actual);
        // ...
        iterator.next();
        expected = "[1, 3, 2]";
        actual = counter.toString();
        assertEquals(expected, actual);
    }
}
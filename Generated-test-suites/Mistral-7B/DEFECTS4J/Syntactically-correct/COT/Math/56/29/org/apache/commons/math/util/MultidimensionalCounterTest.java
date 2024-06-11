import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.math3.util.MathUtils.copyOf;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(2 * 4 * 3, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] sizes = {-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();

        for (int i = 0; i < counter.getSize(); i++) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();

        for (int i = 0; i < counter.getSize(); i++) {
            assertEquals(i, iterator.getCount());
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator iterator = counter.iterator();

        for (int i = 0; i < counter.getSize(); i++) {
            int[] counts = iterator.getCounts();
            int[] expected = copyOf(new int[]{i / (sizes[0] * sizes[1]), i / sizes[0] % sizes[1], i % sizes[0]});
            assertArrayEquals(expected, counts);
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        for (int i = 0; i < counter.getSize(); i++) {
            int[] counts = counter.getCounts(i);
            int[] expected = copyOf(new int[]{i / (sizes[0] * sizes[1]), i / sizes[0] % sizes[1], i % sizes[0]});
            assertArrayEquals(expected, counts);
        }
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountNegativeIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfBoundsIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(counter.getSize());
    }

    @Test
    public void testGetCountValidIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        for (int i = 0; i < counter.getSize(); i++) {
            int count = counter.getCount(i);
            assertEquals(i, count);
        }
    }

    @Test
    public void testSize() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(2 * 4 * 3, counter.getSize());
    }

    @Test
    public void testSizes() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertArrayEquals(sizes, counter.getSizes());
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        StringBuilder expected = new StringBuilder("[");
        for (int i = 0; i < counter.getDimension(); i++) {
            expected.append("[").append(counter.getCount(i)).append("]");
        }
        expected.append("]");

        assertEquals(expected.toString(), counter.toString());
    }
}
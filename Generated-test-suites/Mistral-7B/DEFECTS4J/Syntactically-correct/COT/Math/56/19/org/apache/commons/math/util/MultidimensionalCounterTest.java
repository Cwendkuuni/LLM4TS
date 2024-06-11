import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(3, counter.getDimension());
        assertEquals(2 * 4 * 3, counter.getSize());
        assertEquals(size, counter.getSizes());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizesInConstructor() {
        int[] size = {-2, 4, 3};
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testZeroSizesInConstructor() {
        int[] size = {0, 4, 3};
        new MultidimensionalCounter(size);
    }

    @Test
    public void testIteratorHasNext() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            assertTrue(iterator.hasNext());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        int i = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            assertEquals(i, next.intValue());
            i++;
        }
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            int[] expected = new int[size.length];
            int index = counter.getCount();
            counter.getCounts(index, expected);
            assertArrayEquals(expected, size);
        }

        assertFalse(counter.iterator().hasNext());
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int index = size[0] * size[1] * size[2];
        counter.getCount(index);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsDimensionMismatch() {
        int[] size = {3, 2};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        int index = 0;
        int[] expected = new int[3];
        counter.getCounts(index, expected);
        assertArrayEquals(new int[]{1, 1, 1}, expected);

        index = size[0] * size[1];
        counter.getCounts(index, expected);
        assertArrayEquals(new int[]{2, 2}, expected);
    }

    @Test
    public void testToString() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);

        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            String actual = counter.iterator().next().toString();
            String expected = "[";
            for (int j = 0; j < size.length; j++) {
                expected += "[" + counter.getCount(j) + "]";
                if (j < size.length - 1) {
                    expected += ",";
                }
            }
            expected += "]";
            assertEquals(expected, actual);
        }

        assertFalse(counter.iterator().hasNext());
    }
}
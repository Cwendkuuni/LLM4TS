import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(2 * 4 * 3, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSizesInConstructor() {
        int[] sizes = new int[]{-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();

        for (int i = 0; i < Math.pow(2, sizes.length); i++) {
            assertTrue(iterator.hasNext());
        }
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        iterator.remove();
    }

    @Test
    public void testIteratorNext() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();

        int index = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            assertEquals(index++, next.intValue());
        }
    }

    @Test
    public void testGetCounts() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        for (int i = 0; i < Math.pow(2, sizes.length); i++) {
            int[] counts = counter.getCounts(i);
            int[] expected = new int[sizes.length];
            for (int j = 0; j < sizes.length; j++) {
                int index = i / Math.pow(size[j + 1], sizes.length - j - 1);
                expected[j] = index % (size[j + 1]);
            }
            assertArrayEquals(expected, counts);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountsWrongDimension() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCounts(Math.pow(2, sizes.length + 1));
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(-1);
    }

    @Test
    public void testGetCount() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        for (int i = 0; i < Math.pow(2, sizes.length); i++) {
            int index = counter.getCount(i);
            assertEquals(i, index);
        }
    }

    @Test(expected = DimensionMismatchException.class)
    public void testGetCountWrongDimension() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(Math.pow(2, sizes.length + 1), new int[sizes.length + 1]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange2() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(-1, new int[sizes.length]);
    }

    @Test
    public void testSize() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(Math.pow(2, sizes.length), counter.getSize());
    }

    @Test
    public void testToString() {
        int[] sizes = new int[]{2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);

        for (int i = 0; i < Math.pow(2, sizes.length); i++) {
            String result = counter.toString();
            String[] expected = new String[sizes.length];
            for (int j = 0; j < sizes.length; j++) {
                int index = i / Math.pow(size[j + 1], sizes.length - j - 1);
                expected[j] = String.valueOf(index % (size[j + 1]));
            }
            String expectedResult = "[";
            for (int j = 0; j < sizes.length; j++) {
                expectedResult += "[" + expected[j] + "]";
                if (j < sizes.length - 1) {
                    expectedResult += ", ";
                }
            }
            expectedResult += "]";
            assertEquals(expectedResult, result);
        }
    }
}
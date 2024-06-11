import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Iterator;
import java.util.List;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        assertEquals(3, counter.getDimension());
        assertEquals(sizes, counter.getSizes());
        assertEquals(24, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] sizes = {-2, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testGetCountNegativeIndex() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(-1);
    }

    @Test(expected = OutOfRangeException.class)
    public void testGetCountOutOfRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        counter.getCount(25);
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        for (int i = 0; i < counter.getSize(); i++) {
            assertTrue(iterator.hasNext());
            iterator.next();
        }
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorNext() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        List<Integer> list = List.of();
        while (iterator.hasNext()) {
            list.add(iterator.next());
        }
        assertEquals(List.of(0, 3, 2, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22), list);
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] expected = {1, 1, 2};
        int[] actual = counter.getCounts(0);
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetCountsOutOfRange() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        try {
            counter.getCounts(counter.getSize());
            fail();
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        int[] expected = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        for (int i = 0; i < counter.getSize(); i++) {
            int[] counts = counter.getCounts(i);
            int actual = counter.getCount(counts);
            assertArrayEquals(counts, counter.getCounts(actual));
            assertEquals(i, actual);
        }
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        Iterator<Integer> iterator = counter.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            String expected = "[i]";
            String actual = counter.toString();
            if (i == 0) {
                expected = "[0]";
            } else {
                String[] parts = actual.split("\\]");
                for (int j = 0; j < dimension; j++) {
                    int index = Integer.parseInt(parts[j].substring(1));
                    assertEquals(get(j), index);
                }
            }
            i++;
            iterator.next();
        }
    }

    private int[] get(int index) {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(sizes);
        return counter.getCounts(counter.getCount(new int[]{index}));
    }
}
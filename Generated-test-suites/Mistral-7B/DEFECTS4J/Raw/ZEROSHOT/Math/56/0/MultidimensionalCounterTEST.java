import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Iterator;
import java.util.Arrays;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        assertEquals(3, mdc.getDimension());
        assertEquals(sizes, mdc.getSizes());
        assertEquals(24 * 4 * 3, mdc.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeSizes() {
        int[] sizes = {-1, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorZeroSizes() {
        int[] sizes = {0, 4, 3};
        new MultidimensionalCounter(sizes);
    }

    @Test
    public void testGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int[] counts = mdc.getCounts(0);
        assertArrayEquals(new int[]{0, 0, 0}, counts);

        counts = mdc.getCounts(1);
        assertArrayEquals(new int[]{0, 0, 1}, counts);

        counts = mdc.getCounts(23);
        assertArrayEquals(new int[]{1, 3, 2}, counts);

        counts = mdc.getCounts(24 * 4 * 3);
        assertArrayEquals(new int[]{-1, -1, -1}, counts);

        int[] invalidCounts = new int[]{4, 3, 2};
        try {
            mdc.getCounts(invalidCounts);
            fail();
        } catch (DimensionMismatchException e) {
            //expected
        }
    }

    @Test
    public void testGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        int count = mdc.getCount(0, 0, 0);
        assertEquals(0, count);

        count = mdc.getCount(0, 0, 1);
        assertEquals(1, count);

        count = mdc.getCount(1, 0, 0);
        assertEquals(12, count);

        count = mdc.getCount(1, 3, 2);
        assertEquals(23, count);

        int[] invalidCounts = new int[]{4, 3, 2, 3};
        try {
            mdc.getCount(invalidCounts);
            fail();
        } catch (DimensionMismatchException e) {
            //expected
        }

        int invalidIndex = 25;
        try {
            mdc.getCount(invalidIndex);
            fail();
        } catch (OutOfRangeException e) {
            //expected
        }
    }

    @Test
    public void testIteratorHasNext() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = mdc.iterator();

        assertFalse(it.hasNext());

        it.next();
        assertTrue(it.hasNext());

        int counter = 0;
        while (it.hasNext()) {
            it.next();
            counter++;
        }
        assertEquals(mdc.getSize(), counter);
    }

    @Test
    public void testIteratorNext() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = mdc.iterator();

        int[] counts = it.next().getCounts();
        assertArrayEquals(new int[]{0, 0, 0}, counts);

        int counter = 0;
        while (it.hasNext()) {
            int next = it.next();
            counts = next.getCounts();
            assertEquals(sizes.length, counts.length);
            for (int i = 0; i < sizes.length; i++) {
                assertEquals(sizes[i], counts[i] + 1);
            }
            counter++;
        }
        assertEquals(mdc.getSize(), counter);
    }

    @Test
    public void testIteratorGetCount() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = mdc.iterator();

        int next = it.next();
        int count = next.getCount();
        assertEquals(0, count);

        it.next();
        next = it.next();
        count = next.getCount();
        assertEquals(1, count);

        it.next();
        next = it.next();
        count = next.getCount();
        assertEquals(12, count);

        it.next();
        next = it.next();
        count = next.getCount();
        assertEquals(23, count);

        it.next(); // last iteration
        next = it.next();
        count = next.getCount();
        assertEquals(-1, count);
    }

    @Test
    public void testIteratorGetCounts() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = mdc.iterator();

        int[] counts = it.next().getCounts();
        assertArrayEquals(new int[]{0, 0, 0}, counts);

        it.next();
        counts = it.next().getCounts();
        assertArrayEquals(new int[]{0, 0, 1}, counts);

        it.next();
        counts = it.next().getCounts();
        assertArrayEquals(new int[]{1, 0, 0}, counts);

        it.next();
        counts = it.next().getCounts();
        assertArrayEquals(new int[]{1, 3, 2}, counts);

        it.next(); // last iteration
        counts = it.next().getCounts();
        assertArrayEquals(new int[]{-1, -1, -1}, counts);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testIteratorRemove() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);
        Iterator<Integer> it = mdc.iterator();
        it.remove();
    }

    @Test
    public void testToString() {
        int[] sizes = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(sizes);

        assertEquals("[0]", mdc.toString());

        Iterator<Integer> it = mdc.iterator();
        int counter = 0;
        while (it.hasNext()) {
            int next = it.next();
            assertEquals("[counter]", next.toString());
            counter++;
        }
    }
}
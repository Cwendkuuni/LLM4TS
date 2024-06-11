package org.apache.commons.math.util;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Iterator;
import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(3, mdc.getDimension());
        assertEquals(size, mdc.getSizes());
        assertEquals(2 * 4 * 3, mdc.getSize());
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        for (int i = 0; i < mdc.getSize(); i++) {
            int[] counts = mdc.getCounts(i);
            int index = mdc.getCount(counts);
            assertArrayEquals(counts, mdc.getCounts(index));
        }
    }

    @Test
    public void testIterator() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        Iterator<Integer> it = mdc.iterator();
        for (int i = 0; i < mdc.getSize(); i++) {
            int next = it.next();
            int[] counts = mdc.getCounts(next);
            int index = mdc.getCount(counts);
            assertArrayEquals(counts, mdc.getCounts(index));
            assertEquals(next, index);
            it.remove(); // UnsupportedOperationException is expected
        }
        assertThrows(UnsupportedOperationException.class, it::remove);
    }

    @Test
    public void testOutOfRangeException() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        assertThrows(OutOfRangeException.class, () -> mdc.getCount(-1, 0));
        assertThrows(OutOfRangeException.class, () -> mdc.getCount(0, -1));
        assertThrows(OutOfRangeException.class, () -> mdc.getCount(-1, -1));
        assertThrows(OutOfRangeException.class, () -> mdc.getCount(mc -> mc.length != size.length));

        for (int i = 0; i < size.length; i++) {
            for (int j = 0; j < size[i]; j++) {
                int[] multidimensionalCount = new int[size.length];
                multidimensionalCount[i] = j;
                assertThrows(OutOfRangeException.class, () -> mdc.getCount(multidimensionalCount));
            }
        }
    }

    @Test
    public void testDimensionMismatchException() {
        int[] size1 = {2, 4, 3};
        MultidimensionalCounter mdc1 = new MultidimensionalCounter(size1);

        int[] size2 = {2, 3};
        assertThrows(DimensionMismatchException.class, () -> new MultidimensionalCounter(size2));

        int[] size3 = {2, 4, 3, 1};
        assertThrows(DimensionMismatchException.class, () -> new MultidimensionalCounter(size3));
    }

    @Test
    public void testNotStrictlyPositiveException() {
        int[] size = {-1, 4, 3};
        assertThrows(NotStrictlyPositiveException.class, () -> new MultidimensionalCounter(size));
    }

    @Test
    public void testToString() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        for (int i = 0; i < mdc.getSize(); i++) {
            int[] counts = mdc.getCounts(i);
            String expectedString = "[";
            for (int j = 0; j < counts.length; j++) {
                if (j > 0) {
                    expectedString += ", ";
                }
                expectedString += counts[j];
            }
            expectedString += "]";
            assertEquals(expectedString, mdc.toString());
            mdc.next();
        }

        assertNull(mdc.next());
    }
}
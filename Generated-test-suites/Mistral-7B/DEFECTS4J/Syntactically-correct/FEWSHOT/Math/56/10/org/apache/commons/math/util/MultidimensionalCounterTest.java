package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.util.Iterator;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.apache.commons.math.util.MathUtils;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = new int[] { 2, 4, 3 };
        MultidimensionalCounter mc = new MultidimensionalCounter(size);
        assertEquals(3, mc.getDimension());
        assertEquals(size, mc.getSizes());
        assertEquals(216, mc.getSize());
    }

    @Test
    public void testIterator() {
        int[] size = new int[] { 1, 2, 3 };
        MultidimensionalCounter mc = new MultidimensionalCounter(size);
        Iterator<Integer> it = mc.iterator();
        assertEquals(0, it.next());
        assertEquals(new int[] { 0, 0, 0 }, it.getCounts());
        assertEquals(0, it.getCounts()[0]);
        assertEquals(0, it.getCounts()[1]);
        assertEquals(0, it.getCounts()[2]);

        for (int i = 1; i < MathUtils.mult(MathUtils.sum(size), 1.0) - 1; i++) {
            assertEquals(i, it.next());
            int[] c = it.getCounts();
            for (int j = 0; j < size.length; j++) {
                assertEquals(MathUtils.floorDiv(i, size[j]), c[j]);
            }
        }

        assertNull(it.next());
    }

    @Test
    public void testGetCount() {
        int[] size = new int[] { 1, 2, 3 };
        MultidimensionalCounter mc = new MultidimensionalCounter(size);
        for (int i = 0; i < MathUtils.mult(MathUtils.sum(size), 1.0) - 1; i++) {
            int c[] = mc.getCounts(i);
            int idx = mc.getCount(c);
            assertArrayEquals(c, mc.getCounts(idx));
        }
    }

    @Test
    public void testOutOfRangeException() {
        int[] size = new int[] { 1, 2, 3 };
        MultidimensionalCounter mc = new MultidimensionalCounter(size);
        assertThrows(OutOfRangeException.class, () -> mc.getCount(-1, 0, 2));
        assertThrows(OutOfRangeException.class, () -> mc.getCount(1, -1, 2));
        assertThrows(OutOfRangeException.class, () -> mc.getCount(1, 3, 2));
        assertThrows(OutOfRangeException.class, () -> mc.getCount(1, 3, MathUtils.negate(size[2])));
        assertThrows(OutOfRangeException.class, () -> mc.getCount(MathUtils.sum(size) + 1));
    }

    @Test
    public void testDimensionMismatchException() {
        int[] size1 = new int[] { 1, 2, 3 };
        int[] size2 = new int[] { 1, 2, 3, 4 };
        MultidimensionalCounter mc1 = new MultidimensionalCounter(size1);
        MultidimensionalCounter mc2 = new MultidimensionalCounter(size2);
        assertThrows(DimensionMismatchException.class, () -> mc1.getCounts(mc2.iterator().next()));
        assertThrows(DimensionMismatchException.class, () -> mc2.getCount(mc1.iterator().next()));
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeSize() {
        int[] size = new int[] { -1, 2, 3 };
        new MultidimensionalCounter(size);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNegativeTotalSize() {
        int[] size = new int[] { 1, 2, 3 };
        new MultidimensionalCounter(MathUtils.negate(MathUtils.mult(MathUtils.sum(size), 1.0)));
    }
}
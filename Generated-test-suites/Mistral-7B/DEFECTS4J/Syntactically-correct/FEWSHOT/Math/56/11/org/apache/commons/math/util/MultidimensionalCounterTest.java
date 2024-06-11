package org.apache.commons.math.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounter() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        assertEquals(2 * 4 * 3, mdc.getSize());

        Iterator iterator = mdc.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            assertEquals(count, next);
            count++;
        }
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        int[] size = {-1, 4, 3};
        try {
            new MultidimensionalCounter(size);
        } catch (NotStrictlyPositiveException e) {
            throw new AssertionError("Should not have thrown NotStrictlyPositiveException for invalid size", e);
        }
    }

    @Test
    public void testGetCount() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        int[] count = mdc.getCounts(0);
        assertEquals(0, count[0]);
        assertEquals(0, count[1]);
        assertEquals(0, count[2]);

        count = mdc.getCounts(1);
        assertEquals(0, count[0]);
        assertEquals(0, count[1]);
        assertEquals(1, count[2]);

        count = mdc.getCounts(2);
        assertEquals(0, count[0]);
        assertEquals(0, count[1]);
        assertEquals(2, count[2]);

        count = mdc.getCounts(3);
        assertEquals(0, count[0]);
        assertEquals(0, count[1]);
        assertEquals(2, count[2]);
        assertEquals(1, count[1]);

        count = mdc.getCounts(4);
        assertEquals(0, count[0]);
        assertEquals(1, count[1]);
        assertEquals(2, count[2]);

        count = mdc.getCounts(5);
        assertEquals(0, count[0]);
        assertEquals(1, count[1]);
        assertEquals(2, count[2]);
        assertEquals(2, count[1]);

        count = mdc.getCounts(6);
        assertEquals(0, count[0]);
        assertEquals(1, count[1]);
        assertEquals(2, count[2]);
        assertEquals(2, count[1]);
        assertEquals(1, count[2]);
    }

    @Test(expected = OutOfRangeException.class)
    public void testOutOfRangeException() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        mdc.getCounts(-1);
    }

    @Test(expected = DimensionMismatchException.class)
    public void testDimensionMismatchException() {
        int[] size = {2, 4};
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{2, 4, 3});

        mdc.getCount(new int[]{2, 4});
    }

    @Test
    public void testToString() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        Iterator iterator = mdc.iterator();
        int count = 0;
        while (iterator.hasNext()) {
            Integer next = iterator.next();
            String toString = mdc.toString();
            String expected = "[" + count + "] [" + (count % 4) + "] [" + (count % 3) + "]";
            assertEquals(expected, toString);
            count++;
        }
    }
}
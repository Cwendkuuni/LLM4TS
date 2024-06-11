package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.util.MathUtils;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterCreation() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        assertEquals(3, counter.getDimension());
        assertEquals(6, counter.getSize());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterCreationNegativeSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(-1, 2, 3);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testMultidimensionalCounterCreationZeroSizes() {
        MultidimensionalCounter counter = new MultidimensionalCounter(0, 2, 3);
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        Iterator iterator = counter.iterator();

        // Test hasNext
        for (int i = 0; i < 6; i++) {
            assertTrue(iterator.hasNext());
        }
        assertFalse(iterator.hasNext());

        // Test next
        int index = 0;
        for (Integer nextIndex : counter) {
            assertEquals(index++, nextIndex);
        }

        // Test getCount
        for (Integer nextIndex : counter) {
            assertEquals(nextIndex, iterator.next());
            assertEquals(nextIndex, iterator.getCount());
        }

        // Test getCounts
        assertEquals(new int[]{0, 0, 0}, counter.iterator().next().getCounts());
        assertEquals(new int[]{1, 0, 0}, counter.iterator().next().getCounts());
        assertEquals(new int[]{1, 1, 0}, counter.iterator().next().getCounts());
        assertEquals(new int[]{1, 1, 1}, counter.iterator().next().getCounts());
        assertEquals(new int[]{0, 1, 1}, counter.iterator().next().getCounts());
        assertEquals(new int[]{0, 1, 2}, counter.iterator().next().getCounts());
        assertNull(counter.iterator().next().getCounts());
    }

    @Test
    public void testMultidimensionalCounterGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);

        int[] counts = counter.getCounts(0);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        counts = counter.getCounts(1);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        counts = counter.getCounts(2);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        counts = counter.getCounts(3);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);

        counts = counter.getCounts(4);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(0, counts[2]);

        counts = counter.getCounts(5);
        assertEquals(0, counts[0]);
        assertEquals(1, counts[1]);
        assertEquals(1, counts[2]);

        int count = counter.getCount(0, 0, 0);
        assertEquals(0, count);

        count = counter.getCount(1, 0, 0);
        assertEquals(1, count);

        count = counter.getCount(1, 0, 1);
        assertEquals(1, count);

        count = counter.getCount(1, 0, 2);
        assertEquals(2, count);

        count = counter.getCount(1, 1, 0);
        assertEquals(3, count);

        count = counter.getCount(1, 1, 1);
        assertEquals(4, count);

        count = counter.getCount(1, 1, 2);
        assertEquals(6, count);

        count = counter.getCount(1, 2, 0);
        assertEquals(7, count);

        count = counter.getCount(1, 2, 1);
        assertEquals(8, count);

        count = counter.getCount(1, 2, 2);
        assertEquals(9, count);

        count = counter.getCount(2, 0, 0);
        assertEquals(1, count);

        count = counter.getCount(2, 0, 1);
        assertEquals(2, count);

        count = counter.getCount(2, 0, 2);
        assertEquals(3, count);

        count = counter.getCount(2, 1, 0);
        assertEquals(4, count);

        count = counter.getCount(2, 1, 1);
        assertEquals(6, count);

        count = counter.getCount(2, 1, 2);
        assertEquals(9, count);

        count = counter.getCount(2, 2, 0);
        assertEquals(7, count);

        count = counter.getCount(2, 2, 1);
        assertEquals(8, count);

        count = counter.getCount(2, 2, 2);
        assertEquals(9, count);
    }

    @Test
    public void testMultidimensionalCounterToString() {
        MultidimensionalCounter counter = new MultidimensionalCounter(1, 2, 3);
        assertEquals("[0] [0] [0]", counter.toString());

        Iterator iterator = counter.iterator();
        for (int i = 0; i < 6; i++) {
            iterator.next();
        }
        assertEquals("[5] [5] [5]", counter.toString());
    }
}
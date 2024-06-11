package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounter() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        int[] expected = {0, 0, 0};
        int unidimensionalIndex = counter.getCount(expected);
        assertEquals(unidimensionalIndex, 0);

        counter.next();
        expected[0]++;
        unidimensionalIndex = counter.getCount(expected);
        assertEquals(unidimensionalIndex, 1);

        counter.next();
        expected[1]++;
        unidimensionalIndex = counter.getCount(expected);
        assertEquals(unidimensionalIndex, 3);

        counter.next();
        expected[0]++;
        expected[1]++;
        unidimensionalIndex = counter.getCount(expected);
        assertEquals(unidimensionalIndex, 13);

        counter.next();
        expected[2]++;
        unidimensionalIndex = counter.getCount(expected);
        assertEquals(unidimensionalIndex, 23);

        int[] invalidSize = {2, -1, 3};
        try {
            new MultidimensionalCounter(invalidSize);
            fail();
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();

        int[] expected = {0, 0, 0};
        int unidimensionalIndex = counter.getCount(expected);
        Integer next = iterator.next();
        assertEquals(next, 0);

        assertArrayEquals(expected, iterator.getCounts());
        expected[0]++;

        next = iterator.next();
        assertEquals(next, 1);
        assertArrayEquals(new int[]{1, 0, 0}, iterator.getCounts());

        next = iterator.next();
        assertEquals(next, 3);
        assertArrayEquals(new int[]{1, 0, 1}, iterator.getCounts());

        next = iterator.next();
        assertEquals(next, 13);
        assertArrayEquals(new int[]{1, 1, 0}, iterator.getCounts());

        next = iterator.next();
        assertEquals(next, 23);
        assertArrayEquals(new int[]{1, 1, 2}, iterator.getCounts());

        next = iterator.next();
        assertNull(next);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testMultidimensionalCounterIteratorRemove() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        Iterator iterator = counter.iterator();
        iterator.remove();
    }
}
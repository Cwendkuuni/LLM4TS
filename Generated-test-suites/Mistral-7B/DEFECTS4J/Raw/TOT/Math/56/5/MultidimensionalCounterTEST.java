package org.apache.commons.math.util;

import org.junit.Test;
import org.junit.Assert.*;

import java.util.Iterator;

public class MultidimensionalCounterTest {

    @Test
    public void testSimpleMultidimensionalCounter() {
        int[] size = {2, 4, 3};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);

        // Test iteration
        Iterator<Integer> it = mdc.iterator();
        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            assertTrue(it.hasNext());
            int result = it.next();
            int[] counts = it.getCounts();
            assertEquals(counts.length, size.length);
            for (int j = 0; j < counts.length; j++) {
                assertEquals(counts[j], i % size[j]);
            }
        }
        assertFalse(it.hasNext());

        // Test getCount and getCounts
        for (int i = 0; i < size[0] * size[1] * size[2]; i++) {
            int index = mdc.getCount(i);
            int[] counts = mdc.getCounts(index);
            assertEquals(counts.length, size.length);
            for (int j = 0; j < counts.length; j++) {
                assertEquals(counts[j], i % size[j]);
            }
            assertEquals(index, mdc.getCount(counts));
        }

        // Test out of bounds
        try {
            mdc.getCount(-1);
            fail("Negative index should cause an exception");
        } catch (OutOfRangeException e) {
            // expected
        }

        try {
            mdc.getCount(size[0] * size[1] * size[2]);
            fail("Index out of bounds should cause an exception");
        } catch (OutOfRangeException e) {
            // expected
        }
    }

    @Test
    public void testEmptyMultidimensionalCounter() {
        int[] size = {};
        MultidimensionalCounter mdc = new MultidimensionalCounter(size);
        Iterator<Integer> it = mdc.iterator();
        assertFalse(it.hasNext());
    }

    @Test
    public void testOneDimensionalCounter() {
        int size = 5;
        MultidimensionalCounter mdc = new MultidimensionalCounter(new int[]{size});

        // Test iteration
        Iterator<Integer> it = mdc.iterator();
        for (int i = 0; i < size; i++) {
            assertTrue(it.hasNext());
            int result = it.next();
            int[] counts = it.getCounts();
            assertEquals(counts.length, 1);
            assertEquals(counts[0], i);
        }
        assertFalse(it.hasNext());

        // Test getCount and getCounts
        for (int i = 0; i < size; i++) {
            int index = mdc.getCount(i);
            int[] counts = mdc.getCounts(index);
            assertEquals(counts.length, 1);
            assertEquals(counts[0], i);
            assertEquals(index, mdc.getCount(counts));
        }
    }
}
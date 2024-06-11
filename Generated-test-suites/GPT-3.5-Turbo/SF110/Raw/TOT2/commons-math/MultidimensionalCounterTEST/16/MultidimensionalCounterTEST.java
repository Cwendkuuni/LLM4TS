
package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testMultidimensionalCounterConstructor() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(sizes);
        } catch (Exception e) {
            fail("Constructor failed");
        }
        assertEquals(sizes.length, counter.getDimension());
    }

    @Test
    public void testCountsMethod() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(sizes);
        } catch (Exception e) {
            fail("Constructor failed");
        }
        int index = 3;
        int[] expectedCounts = {0, 1, 1};
        assertArrayEquals(expectedCounts, counter.getCounts(index));
    }

    @Test
    public void testGetCountMethod() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(sizes);
        } catch (Exception e) {
            fail("Constructor failed");
        }
        int[] counts = {1, 2, 3};
        int expectedCount = 29;
        try {
            assertEquals(expectedCount, counter.getCount(counts));
        } catch (Exception e) {
            fail("GetCount method failed");
        }
    }

    @Test
    public void testMultidimensionalCounterIterator() {
        int[] sizes = {2, 3, 4};
        MultidimensionalCounter counter = null;
        try {
            counter = new MultidimensionalCounter(sizes);
        } catch (Exception e) {
            fail("Constructor failed");
        }
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertTrue(iterator.hasNext());
        int count = 0;
        while (iterator.hasNext()) {
            assertEquals(count, (int) iterator.next());
            count++;
        }
        assertEquals(counter.getSize(), count);
    }

}


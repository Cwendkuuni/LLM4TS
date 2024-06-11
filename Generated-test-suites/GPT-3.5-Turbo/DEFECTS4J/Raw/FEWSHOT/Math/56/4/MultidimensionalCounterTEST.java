
package org.apache.commons.math.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math.exception.DimensionMismatchException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.exception.OutOfRangeException;
import org.junit.Before;
import org.junit.Test;

public class MultidimensionalCounterTest {

    private MultidimensionalCounter counter;

    @Before
    public void setup() {
        counter = new MultidimensionalCounter(2, 3, 4);
    }

    @Test
    public void testConstructor() {
        assertNotNull(counter);
    }

    @Test
    public void testGetDimension() {
        assertEquals(3, counter.getDimension());
    }

    @Test
    public void testGetSize() {
        assertEquals(24, counter.getSize());
    }

    @Test
    public void testGetSizes() {
        int[] sizes = counter.getSizes();
        assertNotNull(sizes);
        assertEquals(3, sizes.length);
        assertEquals(2, sizes[0]);
        assertEquals(3, sizes[1]);
        assertEquals(4, sizes[2]);
    }

    @Test
    public void testGetCounts() {
        int[] counts = counter.getCounts(0);
        assertNotNull(counts);
        assertEquals(3, counts.length);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(0, counts[2]);
    }

    @Test
    public void testGetCount() {
        int count = counter.getCount(0, 0, 0);
        assertEquals(0, count);
    }

    @Test
    public void testGetCountWithInvalidDimensions() {
        assertThrows(DimensionMismatchException.class, () -> counter.getCount(0, 0));
    }

    @Test
    public void testGetCountWithInvalidRange() {
        assertThrows(OutOfRangeException.class, () -> counter.getCount(0, 0, 5));
    }

    @Test
    public void testIterator() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertNotNull(iterator);
        assertEquals(-1, iterator.getCount());
        assertEquals(0, iterator.next().intValue());
        int[] counts = iterator.getCounts();
        assertNotNull(counts);
        assertEquals(3, counts.length);
        assertEquals(0, counts[0]);
        assertEquals(0, counts[1]);
        assertEquals(1, counts[2]);
    }

    @Test
    public void testIteratorHasNext() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(true, iterator.hasNext());
        for (int i = 0; i < 23; i++) {
            iterator.next();
        }
        assertEquals(false, iterator.hasNext());
    }

    @Test
    public void testIteratorRemove() {
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
    }

    @Test
    public void testIteratorWithEmptyCounter() {
        counter = new MultidimensionalCounter();
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(false, iterator.hasNext());
    }

    @Test
    public void testIteratorWithSingleDimension() {
        counter = new MultidimensionalCounter(5);
        MultidimensionalCounter.Iterator iterator = counter.iterator();
        assertEquals(true, iterator.hasNext());
        for (int i = 0; i < 4; i++) {
            iterator.next();
        }
        assertEquals(false, iterator.hasNext());
    }

    @Test
    public void testIteratorWithInvalidRange() {
        assertThrows(OutOfRangeException.class, () -> {
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            while (iterator.hasNext()) {
                iterator.getCounts()[2] = 5;
                iterator.next();
            }
        });
    }

    @Test
    public void testIteratorWithNegativeRange() {
        assertThrows(OutOfRangeException.class, () -> {
            MultidimensionalCounter.Iterator iterator = counter.iterator();
            while (iterator.hasNext()) {
                iterator.getCounts()[1] = -1;
                iterator.next();
            }
        });
    }
}

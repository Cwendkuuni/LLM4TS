
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, -5);
            fail("NotStrictlyPositiveException should have been thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetCounts() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        assertArrayEquals(new int[]{2, 3, 4}, counter.getCounts(59));
    }

    @Test
    public void testGetCount() {
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);
        assertEquals(59, counter.getCount(2, 3, 4));
    }

    // Add more edge cases and exception handling test cases here...

}

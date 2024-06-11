package org.apache.commons.math.util;
import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.util.MultidimensionalCounter;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        int[] size = {2, 3, 4};
        MultidimensionalCounter counter = new MultidimensionalCounter(size);
        assertEquals(size.length, counter.getDimension());
        assertEquals(6 * 3 * 4, counter.getSize());
    }
}
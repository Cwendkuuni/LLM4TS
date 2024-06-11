package org.apache.commons.math3.util;

import org.junit.Test;
import org.junit.Assert.*;

public class MultidimensionalCounterTest {

    @Test
    public void testConstructor() {
        try {
            MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
            Assert.assertNotNull(counter);
        } catch (NotStrictlyPositiveException e) {
            fail("NotStrictlyPositiveException should not be thrown");
        }
    }
    
    @Test
    public void testGetDimension() {
        MultidimensionalCounter counter = new MultidimensionalCounter(2, 3, 4);
        assertEquals(3, counter.getDimension());
    }

    // Add more test cases for other methods
    
}
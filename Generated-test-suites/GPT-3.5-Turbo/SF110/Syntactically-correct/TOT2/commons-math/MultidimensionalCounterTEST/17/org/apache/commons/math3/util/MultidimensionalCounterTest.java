package org.apache.commons.math3.util;

import static org.junit.Assert.*;
import org.junit.Test;

public class MultidimensionalCounterTest {

    @Test
    public void testGetDimension() {
        // Creating an instance of MultidimensionalCounter
        MultidimensionalCounter counter = new MultidimensionalCounter(3, 4, 5);

        // Testing the getDimension method
        assertEquals(3, counter.getDimension());
    }

    // Add more test cases for other methods as needed

}


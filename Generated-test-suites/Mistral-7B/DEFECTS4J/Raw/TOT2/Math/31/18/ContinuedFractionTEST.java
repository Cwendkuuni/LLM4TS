package org.apache.commons.math3.util;

import org.junit.Test;
import static org.junit.Assert.*;

public class ContinuedFractionTest {

    @Test
    public void testContinuedFraction() {
        ContinuedFraction continuedFraction = new MyContinuedFraction();
        testValidInput();
        testInvalidInputX();
        // Call other test methods from individual test files here
    }
}
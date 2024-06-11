package org.apache.commons.math.special;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

/**
 * JUnit test cases for the Gamma class methods.
 */
public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with valid input
        double result = Gamma.logGamma(1.5);
        assertEquals(result, 0.405826586754536, 0.00001);

        // Test with negative input
        result = Gamma.logGamma(-1.0);
        assertNaN("logGamma(-1.0) should be NaN", result);

        // Test with zero input
        result = Gamma.logGamma(0.0);
        assertNaN("logGamma(0.0) should be NaN", result);

        // Test with non-numeric input
        result = Gamma.logGamma("string");
        assertNaN("logGamma(non-numeric) should be NaN", result);
    }

    @Test
    public void testRegularizedGammaP() {
        // Test with valid input
        double result = Gamma.regularizedGammaP(2.5, 1.5);
        assertEquals(result, 0.63665592660656, 0.00001);

        // Test with negative input for a
        try {
            result = Gamma.regularizedGammaP(-1.0, 2.5);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with negative input for x
        try {
            result = Gamma.regularizedGammaP(2.5, -1.0);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with zero input for x (should return 1.0)
        result = Gamma.regularizedGammaP(2.5, 0.0);
        assertEquals(result, 1.0, 0.00001);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test with valid input
        double result = Gamma.regularizedGammaQ(2.5, 1.5);
        assertEquals(result, 0.36334407339344, 0.00001);

        // Test with negative input for a
        try {
            result = Gamma.regularizedGammaQ(-1.0, 2.5);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with negative input for x
        try {
            result = Gamma.regularizedGammaQ(2.5, -1.0);
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test with x > a
        result = Gamma.regularizedGammaQ(2.5, 3.0);
        assertEquals(result, 0.0, 0.00001);

        // Test with a > 1 and x < a
        result = Gamma.regularizedGammaQ(3.0, 2.5);
        assertEquals(result, 1.0, 0.00001);
    }
}
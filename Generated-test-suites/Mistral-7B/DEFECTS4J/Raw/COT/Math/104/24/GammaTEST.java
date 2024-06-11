package org.apache.commons.math.special; // make sure the test class is in the same package as the Gamma class

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

/**
 * JUnit test class for the Gamma class.
 */
public class GammaTest {

    /**
     * Test method for Gamma.logGamma(double).
     */
    @Test
    public void testLogGamma() {
        double result, expected;

        // Test with positive values
        result = Gamma.logGamma(1.0);
        expected = 0.0;
        assertEquals(expected, result, 1e-9);

        result = Gamma.logGamma(2.0);
        expected = Math.log(Math.PI) + Math.log(2.0) - 0.5 * Math.log(2.0);
        assertEquals(expected, result, 1e-9);

        result = Gamma.logGamma(3.0);
        expected = Math.log(Math.SQRT12) + Math.log(2.0) + Math.log(3.0) - 0.5 * Math.log(2.0);
        assertEquals(expected, result, 1e-9);

        // Test with negative values and NaN
        result = Gamma.logGamma(-1.0);
        assertTrue(Double.isNaN(result));

        result = Gamma.logGamma(0.0);
        assertTrue(Double.isNaN(result));
    }

    /**
     * Test method for Gamma.regularizedGammaP(double, double).
     */
    @Test
    public void testRegularizedGammaP() {
        double result, expected;

        // Test with positive values
        result = Gamma.regularizedGammaP(1.0, 1.0);
        expected = 0.5;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaP(1.5, 1.0);
        expected = Math.sqrt(Math.PI / 2) / Math.sqrt(2);
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaP(2.0, 1.0);
        expected = 1.0;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaP(3.0, 1.0);
        expected = Math.sqrt(Math.PI) / 3;
        assertEquals(expected, result, 1e-9);

        // Test with negative values
        result = Gamma.regularizedGammaP(1.0, 2.0);
        expected = Math.sqrt(Math.PI) / (2 * Math.sqrt(2));
        assertEquals(expected, result, 1e-9);

        // Test with x < a
        result = Gamma.regularizedGammaP(2.0, 1.0); // a = 2, x = 1
        expected = 1 - Math.sqrt(Math.PI) / 2;
        assertEquals(expected, result, 1e-9);

        // Test with x = a
        result = Gamma.regularizedGammaP(2.0, 2.0);
        expected = 1 - Math.exp(-2 + 2 * Math.log(2) - Gamma.logGamma(2));
        assertEquals(expected, result, 1e-9);

        // Test with x > a
        result = Gamma.regularizedGammaP(2.0, 4.0);
        expected = 1 - Math.exp(-4 + 2 * Math.log(4) - Gamma.logGamma(2)) * (1 / (2 * 3) + 1 / (3 * 4)) / (1 + 1 / (4 * 5));
        assertEquals(expected, result, 1e-9);
    }

    /**
     * Test method for Gamma.regularizedGammaQ(double, double).
     */
    @Test
    public void testRegularizedGammaQ() {
        double result, expected;

        // Test with positive values
        result = Gamma.regularizedGammaQ(1.0, 1.0);
        expected = 0.5;
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaQ(1.5, 1.0);
        expected = 1 - Math.sqrt(Math.PI / 2) / Math.sqrt(2);
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaQ(2.0, 1.0);
        expected = 1.0 - 1.0; // 1 - 1 = 0
        assertEquals(expected, result, 1e-9);

        result = Gamma.regularizedGammaQ(3.0, 1.0);
        expected = 1 - Math.sqrt(Math.PI) / 3;
        assertEquals(expected, result, 1e-9);

        // Test with negative values
        result = Gamma.regularizedGammaQ(1.0, 2.0);
        expected = 1 - Math.sqrt(Math.PI) / (2 * Math.sqrt(2));
        assertEquals(expected, result, 1e-9);

        // Test with x < a
        result = Gamma.regularizedGammaQ(2.0, 1.0); // a = 2, x = 1
        expected = 1 - (1 - Math.sqrt(Math.PI) / 2);
        assertEquals(expected, result, 1e-9);

        // Test with x = a
        result = Gamma.regularizedGammaQ(2.0, 2.0);
        expected = 0;
        assertEquals(expected, result, 1e-9);

        // Test with x > a
        result = Gamma.regularizedGammaQ(2.0, 4.0);
        expected = 1 - Math.exp(-4 + 2 * Math.log(4) - Gamma.logGamma(2)) * (1 / (2 * 3) + 1 / (3 * 4)) / (1 + 1 / (4 * 5));
        assertEquals(expected, result, 1e-9);
        
        // Test exception handling
        try {
            result = Gamma.regularizedGammaQ(-1.0, 1.0);
        } catch (AssertionError e) {
            expected = e.getMessage();
            assertTrue(expected.startsWith("expected:<"));
            assertTrue(expected.endsWith(" but was:<"));
        }

        try {
            result = Gamma.regularizedGammaQ(1.0, -1.0);
        } catch (AssertionError e) {
            expected = e.getMessage();
            assertTrue(expected.startsWith("expected:<"));
            assertTrue(expected.endsWith(" but was:<"));
        }

        try {
            result = Gamma.regularizedGammaQ(1.0, 0.0);
        } catch (AssertionError e) {
            expected = e.getMessage();
            assertTrue(expected.startsWith("expected:<"));
            assertTrue(expected.endsWith(" but was:<"));
        }

        try {
            result = Gamma.regularizedGammaQ(1.0, Double.NaN);
        } catch (AssertionError e) {
            expected = e.getMessage();
            assertTrue(expected.startsWith("expected:<"));
            assertTrue(expected.endsWith(" but was:<"));
        }

        try {
            result = Gamma.regularizedGammaQ(Double.NaN, 1.0);
        } catch (AssertionError e) {
            expected = e.getMessage();
            assertTrue(expected.startsWith("expected:<"));
            assertTrue(expected.endsWith(" but was:<"));
        }
    }
}
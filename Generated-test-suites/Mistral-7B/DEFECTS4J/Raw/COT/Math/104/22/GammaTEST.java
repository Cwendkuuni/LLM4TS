package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;

import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with valid inputs
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-9);
        assertEquals(Math.log(1.0), Gamma.logGamma(1.0), 1e-9);
        assertEquals(Math.log(2.0), Gamma.logGamma(2.0), 1e-9);
        assertEquals(Math.log(3.0), Gamma.logGamma(3.0), 1e-9);

        // Test with edge cases and exceptions
        assertNaN(Gamma.logGamma(Double.NEGATIVE_INFINITY));
        assertNaN(Gamma.logGamma(Double.POSITIVE_INFINITY));
        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(0.0));
        assertNaN(Gamma.logGamma(1.0, new MathException()));
    }

    @Test
    public void testRegularizedGammaP() {
        // Test with valid inputs
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 0.5), 1e-9);
        assertEquals(0.62831853, Gamma.regularizedGammaP(1.5, 2.0), 1e-9);
        assertEquals(Math.exp(-2.0) * (0.862805397), Gamma.regularizedGammaP(2.5, 1.0), 1e-9);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-9);

        // Test with edge cases and exceptions
        assertNaN(Gamma.regularizedGammaP(-0.5, 0.5));
        assertNaN(Gamma.regularizedGammaP(0.5, -0.5));
        assertNaN(Gamma.regularizedGammaP(0.5, 0.0));
        assertNaN(Gamma.regularizedGammaP(0.5, Double.NaN));
        assertNaN(Gamma.regularizedGammaP(0.5, 1.0, new MathException()));
        assertNaN(Gamma.regularizedGammaP(0.5, 1.0, 0.0));
        assertNaN(Gamma.regularizedGammaP(0.5, 1.0, Integer.MIN_VALUE));
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test with valid inputs
        assertEquals(0.5, Gamma.regularizedGammaQ(0.5, 0.5), 1e-9);
        assertEquals(1.0 - Math.exp(-2.0) * (0.62831853), Gamma.regularizedGammaQ(1.5, 2.0), 1e-9);
        assertEquals(1.0 - Math.exp(-2.0) * (0.862805397), Gamma.regularizedGammaQ(2.5, 1.0), 1e-9);
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-9);

        // Test with edge cases and exceptions
        assertNaN(Gamma.regularizedGammaQ(-0.5, 0.5));
        assertNaN(Gamma.regularizedGammaQ(0.5, -0.5));
        assertNaN(Gamma.regularizedGammaQ(0.5, 0.0));
        assertNaN(Gamma.regularizedGammaQ(0.5, Double.NaN));
        assertNaN(Gamma.regularizedGammaQ(0.5, 1.0, new MathException()));
        assertNaN(Gamma.regularizedGammaQ(0.5, 1.0, 0.0));
        assertNaN(Gamma.regularizedGammaQ(0.5, 1.0, Integer.MIN_VALUE));
        assertNaN(Gamma.regularizedGammaQ(0.5, 1.0, 1.0));
    }
}
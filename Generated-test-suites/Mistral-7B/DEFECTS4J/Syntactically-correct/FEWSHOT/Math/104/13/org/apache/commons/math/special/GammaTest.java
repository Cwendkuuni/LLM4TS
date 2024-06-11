package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test valid inputs
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-8);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-8);
        assertEquals(Math.log(SMath.SQRT2_PI), Gamma.logGamma(0.5), 1e-8);
        assertEquals(Math.log(Math.E), Gamma.logGamma(1.0), 1e-8);
        assertEquals(Math.log(2.0 * Math.PI), Gamma.logGamma(1.0), 1e-8);
        assertEquals(Math.log(Math.E * Math.SQRT2_PI), Gamma.logGamma(0.5), 1e-8);

        // Test negative inputs
        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(0.0));

        // Test large inputs
        assertEquals(Gamma.logGamma(1000.0), Math.log(1000.0) + Math.log(Math.E) - Math.log(Gamma.Lanczos_gamma[999]), 1e-8);
    }

    @Test(expected = MathException.class)
    public void testLogGammaThrowsException() {
        Gamma.logGamma(1.5);
    }

    @Test
    public void testRegularizedGammaP() {
        // Test valid inputs
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 0.0), 1e-8);
        assertEquals(1.0, Gamma.regularizedGammaP(0.5, 0.5), 1e-8);
        assertEquals(Math.E / (Math.SQRT2_PI * Math.pow(Math.PI, 0.5)), Gamma.regularizedGammaP(0.5, 1.0), 1e-8);
        assertEquals(Math.E / (Math.SQRT2_PI * Math.pow(Math.PI, 1.5)), Gamma.regularizedGammaP(1.5, 2.0), 1e-8);

        // Test negative inputs
        assertNaN(Gamma.regularizedGammaP(-1.0, 0.0));
        assertNaN(Gamma.regularizedGammaP(0.0, 0.0));

        // Test large inputs
        assertEquals(1.0 - Math.pow(Math.E, -1000.0), Gamma.regularizedGammaP(1000.0, 1000.0), 1e-8);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPThrowsException() {
        Gamma.regularizedGammaP(1.5, 1.5);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test valid inputs
        assertEquals(1.0, Gamma.regularizedGammaQ(0.5, 0.5), 1e-8);
        assertEquals(0.0, Gamma.regularizedGammaQ(0.5, 1.0), 1e-8);
        assertEquals(1.0 - Math.E / (Math.SQRT2_PI * Math.pow(Math.PI, 1.5)), Gamma.regularizedGammaQ(1.5, 2.0), 1e-8);

        // Test negative inputs
        assertNaN(Gamma.regularizedGammaQ(-1.0, 0.0));
        assertNaN(Gamma.regularizedGammaQ(0.0, 0.0));

        // Test large inputs
        assertEquals(1.0 - Math.pow(Math.E, -1000.0), Gamma.regularizedGammaQ(1000.0, 1000.0), 1e-8);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQThrowsException() {
        Gamma.regularizedGammaQ(1.5, 1.5);
    }

    private void assertNaN(Double result) {
        assertEquals(Double.NaN, result, 0.0);
    }
}
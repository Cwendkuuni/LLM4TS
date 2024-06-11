package org.apache.commons.cli;

import static org.junit.Assert.*;

import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with positive values
        assertEquals(Math.log(0.5), Gamma.logGamma(0.5), 1e-6);
        assertEquals(Math.log(1.0), Gamma.logGamma(1.0), 1e-12);
        assertEquals(Math.log(2.0), Gamma.logGamma(2.0), 1e-12);
        assertEquals(Math.log(3.0), Gamma.logGamma(3.0), 1e-12);
        assertEquals(Math.log(4.0), Gamma.logGamma(4.0), 1e-12);

        // Test with negative values
        assertEquals(Math.log(0.5), Gamma.logGamma(-0.5), -1e-6);

        // Test with very large values
        assertEquals(Math.log(100000.0), Gamma.logGamma(100000.0), 1e-12);

        // Test with non-numeric values
        assertNaN(Gamma.logGamma(Double.NaN));
        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(0.0));
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        // Test with positive values
        assertEquals(1.0, Gamma.regularizedGammaP(0.5, 0.5), 1e-6);
        assertEquals(1.0 / Math.sqrt(Math.PI), Gamma.regularizedGammaP(0.5, 1.0), 1e-6);
        assertEquals(1.0 / Math.sqrt(2.0 * Math.PI), Gamma.regularizedGammaP(0.5, 2.0), 1e-6);
        assertEquals(Math.exp(-2.5) / (Math.sqrt(2.0 * Math.PI) * 2.5), Gamma.regularizedGammaP(0.5, 2.5), 1e-6);

        // Test with non-numeric values
        assertNaN(Gamma.regularizedGammaP(Double.NaN, 1.0));
        assertNaN(Gamma.regularizedGammaP(-1.0, 1.0));
        assertNaN(Gamma.regularizedGammaP(0.0, 1.0));

        // Test with negative x
        assertNaN(Gamma.regularizedGammaP(1.0, -1.0));

        // Test with x = a
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-12);

        // Test with very large values
        assertEquals(Math.exp(-100000.0 + Math.log(100000.0) - Gamma.logGamma(100000.0)) / Math.pow(100000.0, 100001.0), Gamma.regularizedGammaP(100000.0, 100001.0), 1e-12);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        // Test with positive values
        assertEquals(1.0 - Math.exp(-0.5 + Math.log(0.5) - Gamma.logGamma(0.5)), Gamma.regularizedGammaQ(0.5, 0.5), 1e-6);
        assertEquals(1.0 - 1.0 / Math.sqrt(Math.PI), Gamma.regularizedGammaQ(0.5, 1.0), 1e-6);
        assertEquals(1.0 - 1.0 / (2.0 * Math.sqrt(2.0 * Math.PI)), Gamma.regularizedGammaQ(0.5, 2.0), 1e-6);
        assertEquals(Math.exp(-2.5 + Math.log(2.5) - Gamma.logGamma(2.5)) / (2.5 * Math.sqrt(2.0 * Math.PI)), Gamma.regularizedGammaQ(2.5, 2.5), 1e-6);

        // Test with non-numeric values
        assertNaN(Gamma.regularizedGammaQ(Double.NaN, 1.0));
        assertNaN(Gamma.regularizedGammaQ(-1.0, 1.0));
        assertNaN(Gamma.regularizedGammaQ(0.0, 1.0));

        // Test with negative x
        assertNaN(Gamma.regularizedGammaQ(1.0, -1.0));

        // Test with x < a
        assertNaN(Gamma.regularizedGammaQ(1.0, 0.5));

        // Test with x = a
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-12);

        // Test with very large values
        assertEquals(1.0 - Math.exp(-100000.0 + Math.log(100000.0) - Gamma.logGamma(100000.0)) / Math.pow(100000.0, 100001.0), Gamma.regularizedGammaQ(100000.0, 100001.0), 1e-12);
    }
}
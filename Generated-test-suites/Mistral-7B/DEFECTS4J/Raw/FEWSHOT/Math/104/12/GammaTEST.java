package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test for positive values
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-10);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-10);
        assertEquals(Math.log(1.0), Gamma.logGamma(2.0), 1e-10);
        assertEquals(Math.log(Math.PI), Gamma.logGamma(1.0 / 2.0), 1e-10);
        assertEquals(Math.log(Math.sqrt(Math.PI)), Gamma.logGamma(3.0 / 2.0), 1e-10);
        assertEquals(Math.log(Math.sqrt(Math.PI) / Math.SQRT12), Gamma.logGamma(5.0 / 2.0), 1e-10);
        assertEquals(Math.log(Math.sqrt(Math.PI) / Math.SQRT12 * 2.6794919243112171e-8), Gamma.logGamma(7.0 / 2.0), 1e-10);

        // Test for negative values and complex numbers
        assertEquals(Double.NaN, Gamma.logGamma(-1.0), 1e-10);
        assertEquals(Double.NaN, Gamma.logGamma(-2.0), 1e-10);
        assertEquals(Double.NaN, Gamma.logGamma(-3.0), 1e-10);
        assertEquals(Math.log(Math.PI) + Math.log(Math.SQRT12) * 1.57079632679489661923, Gamma.logGamma(0.5), 1e-10);
        assertEquals(Math.log(Math.PI) + Math.log(Math.SQRT12) * 3.14159265358979323846, Gamma.logGamma(1.0), 1e-10);
    }

    @Test(expected = MathException.class)
    public void testLogGammaInvalidArgument() {
        Gamma.logGamma(-1.0);
    }

    @Test
    public void testRegularizedGammaP() {
        // Test for regularized gamma function P(a, x) with positive values
        assertEquals(0.5, Gamma.regularizedGammaP(1.0, 1.0), 1e-10);
        assertEquals(0.5 * Math.exp(-1.0), Gamma.regularizedGammaP(1.0, 0.5), 1e-10);
        assertEquals(Math.sqrt(Math.PI) / 2.0, Gamma.regularizedGammaP(3.0 / 2.0, 1.0), 1e-10);
        assertEquals(Math.sqrt(Math.PI) / Math.SQRT12, Gamma.regularizedGammaP(5.0 / 2.0, 1.0), 1e-10);
        assertEquals(Math.sqrt(Math.PI) / Math.SQRT12 * Math.E, Gamma.regularizedGammaP(5.0 / 2.0, 2.0), 1e-10);

        // Test for regularized gamma function P(a, x) with negative values and complex numbers
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 2.0), 1e-10);
        assertEquals(1.0 - Math.exp(-Math.PI + Math.log(Math.SQRT12) * 1.57079632679489661923), Gamma.regularizedGammaP(0.5, 1.0), 1e-10);
        assertEquals(1.0 - Math.exp(-Math.PI + Math.log(Math.SQRT12) * 3.14159265358979323846), Gamma.regularizedGammaP(1.0, 1.0), 1e-10);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPInvalidArgument() {
        Gamma.regularizedGammaP(-1.0, 0.0);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test for regularized gamma function Q(a, x) with positive values
        assertEquals(1.0 - 0.5, Gamma.regularizedGammaQ(1.0, 1.0), 1e-10);
        assertEquals(1.0 - Math.exp(-1.0), Gamma.regularizedGammaQ(1.0, 0.5), 1e-10);
        assertEquals(1.0 - Math.sqrt(Math.PI) / Math.SQRT12, Gamma.regularizedGammaQ(3.0 / 2.0, 1.0), 1e-10);
        assertEquals(1.0 - Math.sqrt(Math.PI)


/ Math.SQRT12 * Math.E, Gamma.regularizedGammaQ(5.0 / 2.0, 2.0), 1e-10);

// Test for regularized gamma function Q(a, x) with negative values and complex numbers
assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 2.0), 1e-10);
assertEquals(1.0 - (1.0 - Math.exp(-Math.PI + Math.log(Math.SQRT12) * 1.57079632679489661923)), Gamma.regularizedGammaQ(0.5, 1.0), 1e-10);
assertEquals(1.0 - (1.0 - Math.exp(-Math.PI + Math.log(Math.SQRT12) * 3.14159265358979323846)), Gamma.regularizedGammaQ(1.0, 1.0), 1e-10);

    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQInvalidArgument() {
        Gamma.regularizedGammaQ(-1.0, 0.0);
    }
}
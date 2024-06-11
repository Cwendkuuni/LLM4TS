package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-15);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-15);
        assertEquals(-0.4054651083656798, Gamma.logGamma(2.0), 1e-15);
        assertEquals(Math.log(Math.PI) + Math.log(2.0), Gamma.logGamma(3.0), 1e-15);
        assertEquals(Math.log(Math.PI) + Math.log(3.0 / 2.0) + Math.log(Math.SQRT12), Gamma.logGamma(3.5), 1e-15);
        assertEquals(-Math.log(2.0) + Math.log(Math.PI), Gamma.logGamma(0.5), 1e-15);
        assertEquals(-Math.log(Math.PI) + Math.log(1.0 / 2.0) + Math.log(Math.SQRT12), Gamma.logGamma(0.5), 1e-15);

        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(1.5));
        assertNaN(Gamma.logGamma(5.5));
        assertNaN(Gamma.logGamma(Math.NAN));
    }

    @Test(expected = MathException.class)
    public void testLogGammaMaxIterationsExceeded() {
        Gamma.logGamma(1000000.0, 1e-20, 1);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-15);
        assertEquals(Math.E / Math.SQRT12, Gamma.regularizedGammaP(0.5, 1.0), 1e-15);
        assertEquals(Math.E * Math.PI / Math.SQRT(2.0 * Math.Math.Math.Math.Math.PI), Gamma.regularizedGammaP(0.5, 2.0), 1e-15);
        assertEquals(Math.E * Math.PI / Math.SQRT(2.0 * Math.Math.Math.Math.Math.PI * Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.PI), Gamma.regularizedGammaP(1.0 / 3.0, 1.0), 1e-15);
        assertEquals(Math.E * Math.PI / (Math.SQRT(2.0 * Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.PI) * Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.SQRT2), Gamma.regularizedGammaP(1.0 / 2.0, 1.0), 1e-15);

        assertNaN(Gamma.regularizedGammaP(-1.0, 1.0));
        assertNaN(Gamma.regularizedGammaP(0.0, 1.0));
        assertNaN(Gamma.regularizedGammaP(1.0, 0.0));
        assertFalse(Double.isFinite(Gamma.regularizedGammaP(1.0, 1.0 + Double.POSITIVE_INFINITY)));
        assertFalse(Double.isFinite(Gamma.regularizedGammaP(1.0, 1.0 - Double.POSITIVE_INFINITY)));
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPMaxIterationsExceeded() {
        Gamma.regularizedGammaP(1.0, 1.0, 1e-20, 1);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-15);
        assertEquals(1.0 - Math.E / Math.SQRT12, Gamma.regularizedGammaQ(0.5, 1.0), 1e-15);
        assertEquals(1.0 - Math.E * Math.PI / (Math.SQRT(2.0 * Math.Math.Math.Math.Math.PI) * Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.SQRT2), Gamma.regularizedGammaQ(1.0 / 2.0, 1.0), 1e-15);
        assertEquals(1.0 - Math.E * Math.PI / (Math.SQRT(2.0 * Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.PI) * Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.Math.SQRT2), Gamma.regularizedGammaQ(1.0 / 3.0, 1.0), 1e-15);

        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, Double.POSITIVE_INFINITY), 1e-15);
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, Double.NEGATIVE_INFINITY), 1e-15);

        assertNaN(Gamma.regularizedGammaQ(-1.0, 1.0));
        assertNaN(Gamma.regularizedGammaQ(0.0, 1.0));
        assertNaN(Gamma.regularizedGammaQ(1.0, 0.0));
    }

    @Test
    public void testRegularizedGammaQMaxIterationsExceeded() {
        Gamma.regularizedGammaQ(1.0, 1.0, 1e-20, 1);
    }
}
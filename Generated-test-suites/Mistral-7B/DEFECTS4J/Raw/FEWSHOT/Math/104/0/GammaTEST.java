package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-10);
        assertEquals(Math.log(Math.PI), Gamma.logGamma(1.0), 1e-10);
        assertEquals(Math.log(120.0) + Math.log(Math.PI), Gamma.logGamma(6.0), 1e-10);
        assertEquals(Math.log(1.337203684932239e-16) + Math.log(Math.PI), Gamma.logGamma(0.5), 1e-10);
        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(1.0 / 0.0));
        assertNaN(Gamma.logGamma(Double.NaN));
        assertThrows(IllegalArgumentException.class, () -> Gamma.logGamma(Double.POSITIVE_INFINITY));
        assertThrows(IllegalArgumentException.class, () -> Gamma.logGamma(Double.NEGATIVE_INFINITY));
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaP_ExceedMaxIterations() {
        Gamma.regularizedGammaP(1.0, 0.5, 1e-15, 10);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaP_Diverge() {
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(0.5, 0.0));
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.5, Gamma.regularizedGammaP(0.5, 1.0), 1e-10);
        assertEquals(0.25, Gamma.regularizedGammaP(0.5, 0.5), 1e-10);
        assertEquals(Math.exp(-Math.PI * Math.PI / 2.0), Gamma.regularizedGammaP(0.5, 0.5 + Math.sqrt(2.0) / 2.0), 1e-8);
        assertEquals(Math.sqrt(Math.PI) / 2.0, Gamma.regularizedGammaP(0.5, 1.0), 1e-8);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(0.75, Gamma.regularizedGammaQ(0.5, 1.0), 1e-10);
        assertEquals(0.25, Gamma.regularizedGammaQ(0.5, 0.5), 1e-10);
        assertEquals(1.0 - Math.exp(-Math.PI * Math.PI / 2.0), Gamma.regularizedGammaQ(0.5, 0.5 + Math.sqrt(2.0) / 2.0), 1e-8);
        assertEquals(1.0 - Math.sqrt(Math.PI) / 2.0, Gamma.regularizedGammaQ(0.5, 1.0), 1e-8);
    }
}
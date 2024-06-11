package org.apache.commons.cli;

import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(-Math.log(2.0), Gamma.logGamma(0.0), 1e-10);
        assertEquals(Math.log(0.5), Gamma.logGamma(0.5), 1e-10);
        assertEquals(Math.E * Math.log(Math.PI) - Math.log(120.0),
                Gamma.logGamma(1.0), 1e-10);
        assertEquals(Math.E * Math.log(Math.PI) * Math.log(3.0) - 1.015595530689591,
                Gamma.logGamma(2.0), 1e-10);
        assertEquals(Math.E * Math.log(Math.PI) * Math.log(1.5) + Math.log(Math.SQRT12),
                Gamma.logGamma(3.0), 1e-10);

        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(0.0));
        assertNaN(Gamma.logGamma(1.0, Double.NaN));
        assertNaN(Gamma.logGamma(1.0, 0.0));
        assertNaN(Gamma.logGamma(1.0, Double.POSITIVE_INFINITY));
        assertNaN(Gamma.logGamma(1.0, Double.NEGATIVE_INFINITY));
        assertNaN(Gamma.logGamma(Double.NaN, Double.NaN));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLogGammaInvalidArg() {
        Gamma.logGamma(-1.0);
    }

    @Test(expected = MathException.class)
    public void testLogGammaMaxIterationsExceeded() throws MathException {
        Gamma.logGamma(2.5, 10);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 0.0), 1e-10);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-10);
        assertEquals(0.75, Gamma.regularizedGammaP(1.5, 2.0), 1e-10);
        assertEquals(0.188292794332421, Gamma.regularizedGammaP(2.5, 1.0), 1e-10);
        assertEquals(0.319285714289139, Gamma.regularizedGammaP(3.5, 1.0), 1e-10);
        assertEquals(0.110433019372248, Gamma.regularizedGammaP(4.5, 1.0), 1e-10);

        assertNaN(Gamma.regularizedGammaP(-1.0, 0.0));
        assertNaN(Gamma.regularizedGammaP(0.0, 0.0));
        assertNaN(Gamma.regularizedGammaP(0.0, 1.0));
        assertNaN(Gamma.regularizedGammaP(1.0, 0.0));
        assertNaN(Gamma.regularizedGammaP(1.0, 1.0, 0.0));
        assertNaN(Gamma.regularizedGammaP(1.0, 1.0, 10));

        assertTrue(Double.isNaN(Gamma.regularizedGammaP(1.5, 0.0)));
        assertTrue(Double.isNaN(Gamma.regularizedGammaP(1.5, 1.0, 0.0)));
        assertTrue(Double.isNaN(Gamma.regularizedGammaP(1.5, 1.0, 1)));

        try {
            Gamma.regularizedGammaP(1.5, -1.0, 100);
        } catch (MathException e) {
            assertEquals(e.getMessage(), "Regularized gamma function P(1.5, -1.0) not defined.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegularizedGammaPIllegalArg() {
        Gamma.regularizedGammaP(-1.0, 0.0);
        Gamma.regularizedGammaP(1.5, -1.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPMaxIterationsExceeded() throws MathException {
        Gamma.regularizedGammaP(2.5, 1.0, 1);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 0.0), 1e-10);
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-10);
        assertEquals(1.25, Gamma.regularizedGammaQ(1.5, 2.0), 1e-10);
        assertEquals(0.880714285714286, Gamma.regularizedGammaQ(2.5, 1.0), 1e-10);
        assertEquals(0.688569330692728, Gamma.regularizedGammaQ(3.5, 1.0), 1e-10);
        assertEquals(0.571176533648386, Gamma.regularizedGammaQ(4.5, 1.0), 1e-10);

        assertNaN(Gamma.regularizedGammaQ(-1.0, 0.0));
        assertNaN(Gamma.regularizedGammaQ(0.0, 0.0));
        assertNaN(Gamma.regularizedGammaQ(0.0, 1.0));
        assertNaN(Gamma.regularizedGammaQ(1.0, 0.0));
        assertNaN(Gamma.regularizedGammaQ(1.0, 1.0, 0.0));
        assertNaN(Gamma.regularizedGammaQ(1.0, 1.0, 10));

        assertTrue(Double.isNaN(Gamma.regularizedGammaQ(1.5, 0.0)));
        assertTrue(Double.isNaN(Gamma.regularizedGammaQ(1.5, 1.0, 0.0)));
        assertTrue(Double.isNaN(Gamma.regularizedGammaQ(1.5, 1.0, 1)));

        try {
            Gamma.regularizedGammaQ(1.5, -1.0, 100);
        } catch (MathException e) {
            assertEquals(e.getMessage(), "Regularized gamma function Q(1.5, -1.0) not defined.");
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRegularizedGammaQIllegalArg() {
        Gamma.regularizedGammaQ(-1.0, 0.0);
        Gamma.regularizedGammaQ(1.5, -1.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQMaxIterationsExceeded() throws MathException {
        Gamma.regularizedGammaQ(2.5, 1.0, 1);
    }
}
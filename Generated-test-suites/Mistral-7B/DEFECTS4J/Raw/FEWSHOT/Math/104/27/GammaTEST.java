package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.special.MaxIterationsExceededException;
import org.apache.commons.math3.special.MathException;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-14);
        assertEquals(0.0, Gamma.logGamma(1.0), 1e-14);
        for (double x = 1.5; x <= 3.0; x += 0.5) {
            assertEquals(Math.log(factorial(x)), Gamma.logGamma(x), 1e-11);
        }
        assertEquals(Math.log(Double.POSITIVE_INFINITY), Gamma.logGamma(Double.POSITIVE_INFINITY), 1e-14);
        assertEquals(Math.log(Double.NEGATIVE_INFINITY), Gamma.logGamma(Double.NEGATIVE_INFINITY), 1e-14);
        assertNaN(Gamma.logGamma(-1.0));
        assertNaN(Gamma.logGamma(Double.NaN));
        assertNaN(Gamma.logGamma(Double.POSITIVE_INFINITY, Double.NaN));
        assertNaN(Gamma.logGamma(Double.NEGATIVE_INFINITY, Double.NaN));
        assertNaN(Gamma.logGamma(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY));
        assertNaN(Gamma.logGamma(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY));
        assertNaN(Gamma.logGamma(Double.POSITIVE_INFINITY, Double.NAN));
        assertNaN(Gamma.logGamma(Double.NEGATIVE_INFINITY, Double.NAN));
        assertNaN(Gamma.logGamma(-0.5));
        assertNaN(Gamma.logGamma(Double.POSITIVE_0));
        assertThrows(MathException.class, () -> Gamma.logGamma(-1.5));
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(1.0, Gamma.regularizedGammaP(0.0, 0.0), 1e-14);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 1e-14);
        assertEquals(Math.E, Gamma.regularizedGammaP(0.5, 1.0), 1e-11);
        assertEquals(Math.E / Math.sqrt(Math.PI), Gamma.regularizedGammaP(0.5, 0.5), 1e-7);
        assertEquals(1.0 / Math.sqrt(Math.PI), Gamma.regularizedGammaP(0.5, 2.0), 1e-7);
        assertEquals(0.6666666666666666, Gamma.regularizedGammaP(1.5, 2.0), 1e-6);
        assertEquals(0.3333333333333333, Gamma.regularizedGammaP(2.5, 2.0), 1e-6);
        assertEquals(Math.exp(-1.0) / sqrt(Math.PI), Gamma.regularizedGammaP(1.0, 1.0), 1e-7);
        assertEquals(Math.exp(-2.0) * (2.0 / Math.sqrt(Math.PI)), Gamma.regularizedGammaP(1.0, 2.0), 1e-7);
        assertEquals(Math.exp(-3.0) * (Math.SQRT12 / Math.sqrt(Math.PI)), Gamma.regularizedGammaP(1.0, 3.0), 1e-7);
        assertEquals(Math.exp(-4.0) * (7.0 / (12 * Math.sqrt(Math.PI))), Gamma.regularizedGammaP(1.0, 4.0), 1e-7);
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(-1.0, 1.0));
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(Double.NaN, Double.NaN));
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(0.0, -1.0));
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(1.0, -1.0));
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(0.0, Gamma.regularizedGammaQ(0.0, 0.0), 1e-14);
        assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 1.0), 1e-14);
        assertEquals(1.0 - Math.E / Math.sqrt(Math.PI), Gamma.regularizedGammaQ(0.5, 0.5), 1e-7);
        assertEquals(Math.sqrt(Math.PI) / Math.E - 1.0, Gamma.regularizedGammaQ(0.5, 2.0), 1e-7);
        assertEquals(1.0 - Math.sqrt(Math.PI / 2.0) / Math.E, Gamma.regularizedGammaQ(0.5, 1.0), 1e-7);
        assertEquals(0.6666666666666666, 1.0 - Gamma.regularizedGammaP(1.5, 2.0), 1e-6);
        assertEquals(0.3333333333333333, 1.0 - Gamma.regularizedGammaP(2.5, 2.0), 1e-6);
        assertEquals(1.0 - Math.exp(-1.0) / Math.sqrt(Math.PI), Gamma.regularizedGammaQ(1.0, 1.0), 1e-7);
        assertEquals(1.0 - Math.exp(-2.0) * (2.0 / Math.sqrt(Math.PI)), Gamma.regularizedGammaQ(1.0, 2.0), 1e-7);
        assertEquals(1.0 - Math.exp(-3.0) * (Math.SQRT12 / Math.sqrt(Math.PI)), Gamma.regularizedGammaQ(1.0, 3.0), 1e-7);
        assertEquals(1.0 - Math.exp(-4.0) * (7.0 / (12 * Math.sqrt(Math.PI))), Gamma.regularizedGammaQ(1.0, 4.0), 1e-7);
        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(-1.0, 1.0));
        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(Double.NaN, Double.NaN));
        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(0.0, -1.0));
        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(1.0, -1.0));
    }

    private static long factorial(double n) {
        long result = 1;
        for (long i = 1; i <= (long) n; i++) {
            result *= i;
        }
        return result;
    }

    private static double sqrt(double value) {
        return Math.sqrt(value);
    }
}
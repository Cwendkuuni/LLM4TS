package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;

import junit.framework.Assert;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1.0e-15);
        assertEquals(0.0, Gamma.logGamma(1.0), 1.0e-15);
        assertEquals(0.5 * Math.log(2.0 * Math.PI), Gamma.logGamma(0.5), 1.0e-15);
        assertEquals(Math.log(Math.PI), Gamma.logGamma(1.0 / 2.0), 1.0e-15);
        assertEquals(Math.EULER_CONSTANT, Gamma.logGamma(1.0), 1.0e-15);
        assertEquals(Math.log(Math.PI), Gamma.logGamma(1.0 / 2.0), 1.0e-15);
        assertEquals(Math.PI * Math.log(2.0), Gamma.logGamma(1.0 / 2.0) + Math.log(Gamma.E), 1.0e-15);
        assertFalse(Double.isFinite(Gamma.logGamma(-1.0)));
        assertFalse(Double.isFinite(Gamma.logGamma(0.0)));
        assertFalse(Double.isFinite(Gamma.logGamma(1.0 + 1.0e-15)));
        assertNotEquals(0.0, Gamma.logGamma(2.0), 1.0e-15);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(1.0, Gamma.regularizedGammaP(0.0, 1.0), 1.0e-15);
        assertEquals(Math.E, Gamma.regularizedGammaP(1.0, 1.0), 1.0e-15);
        assertEquals(Math.PI / Math.SQRT_2, Gamma.regularizedGammaP(0.5, 1.0), 1.0e-15);
        assertEquals(Math.PI / (Math.SQRT_2 * Math.SQRT(Math.PI)), Gamma.regularizedGammaP(0.5, 2.0), 1.0e-15);
        assertEquals(Math.exp(-1.0) / Math.sqrt(2.0 * Math.PI), Gamma.regularizedGammaP(0.5, 0.5), 1.0e-15);
        assertEquals(Math.exp(-1.0) / (Math.sqrt(2.0 * Math.PI) * Math.sqrt(Math.PI)), Gamma.regularizedGammaP(0.5, 1.0), 1.0e-15);
        assertEquals(Math.exp(-1.0) / Math.sqrt(2.0 * Math.PI), Gamma.regularizedGammaP(0.5, 2.0), 1.0e-15);
        Assert.assertThrows(MathException.class, () -> Gamma.regularizedGammaP(0.5, -1.0));
        Assert.assertThrows(MathException.class, () -> Gamma.regularizedGammaP(1.0, 0.0));
        Assert.assertThrows(MathException.class, () -> Gamma.regularizedGammaP(1.0, 1.0 + 1.0e-15));
        Assert.assertThrows(MathException.class, () -> Gamma.regularizedGammaP(1.5, 2.0));
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(1.0 - Math.E, Gamma.regularizedGammaQ(0.0, 1.0), 1.0e-15);
        assertEquals(1.0 - Math.E, Gamma.regularizedGammaQ(1.0, 1.0), 1.0e-15);
        assertEquals(1.0 - Math.PI / (2.0 * Math.SQRT_2), Gamma.regularizedGammaQ(0.5, 1.0), 1.0e-15);
        assertEquals(1.0 - Math.PI / (2.0 * Math.SQRT_2), Gamma.regularizedGammaQ(0.5, 2.0), 1.0e-15);
        assertEquals(1.0 - Math.exp(-1.0) / (Math.sqrt(2.0 * Math.PI) * Math.sqrt(Math.PI)), Gamma.regularizedGammaQ(0.5, 0.5), 1.0e-15);
        assertEquals(1.0 - Math.exp(-1.0) / (Math.sqrt(2.0 * Math.PI) * Math.sqrt(Math.PI)), Gamma.regularizedGammaQ(0.5, 1.0), 1.0e-15);
        assertEquals(1.0 - Math.exp(-1.0) / Math.sqrt(2.0 * Math.PI), Gamma.regularizedGammaQ(0.5, 2.0), 1.0e-15);
        Assert.assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(0.5, -1.0));
        Assert.assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(1.0, 0.0));
        Assert.assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(1.0, 1.0 + 1.0e-15));
        Assert.assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(1.5, 2.0));
    }
}
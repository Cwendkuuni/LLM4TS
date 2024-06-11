package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNaN;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.special.MaxIterationsExceededException;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(0.0, Gamma.logGamma(0.0), 1e-10);
        assertEquals(-Math.log(2.0), Gamma.logGamma(1.0), 1e-10);
        assertEquals(Math.log(Math.PI), Gamma.logGamma(0.5), 1e-10);
        assertEquals(Math.log(Math.E), Gamma.logGamma(1.0), 1e-10);
        assertNaN(Gamma.logGamma(1.5), "logGamma should return NaN for complex inputs");
        assertNaN(Gamma.logGamma(-1.0), "logGamma should return NaN for negative real inputs");
        assertNaN(Gamma.logGamma(0.5, new Complex(1.0, 2.0)), "logGamma should return NaN for complex inputs");

        // Test overflow
        assertNaN(Gamma.logGamma(1.79e308), "logGamma should return NaN for very large inputs");

        // Test underflow
        assertEquals(-1157.051195118e19, Gamma.logGamma(1e-308), 1e-308);

        // Test error bounds
        assertEquals(-1.382e-23, Gamma.logGamma(0.5 + 1e-23), 1e-23);
        assertEquals(-1.382e-23, Gamma.logGamma(0.5 - 1e-23), 1e-23);
        assertEquals(-1.382e-23, Gamma.logGamma(-0.5 + 1e-23), 1e-23);
        assertEquals(-1.382e-23, Gamma.logGamma(-0.5 - 1e-23), 1e-23);

        // Test large inputs
        assertEquals(1.072163627546645e23, Gamma.logGamma(1000.0), 1e-5);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(0.5, Gamma.regularizedGammaP(0.5), 1e-10);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0), 1e-10);
        assertEquals(Math.sqrt(Math.PI) / 2.0, Gamma.regularizedGammaP(0.5, 1.0), 1e-10);
        assertEquals(1.0 - Math.sqrt(Math.PI) / 2.0, Gamma.regularizedGammaP(0.5, 2.0), 1e-10);
        assertEquals(Math.E * Math.sqrt(Math.PI) / Math.SQRT12, Gamma.regularizedGammaP(0.5, 0.5), 1e-10);
        assertEquals(1.0 - Math.E * Math.sqrt(Math.PI) / Math.SQRT12, Gamma.regularizedGammaP(0.5, 1.5), 1e-10);
        assertNaN(Gamma.regularizedGammaP(1.5, -1.0), "regularizedGammaP should return NaN for negative x");

        // Test large inputs
        assertEquals(1.082322695248221e23, Gamma.regularizedGammaP(1000.0, 1000.0), 1e-5);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(1.0 - 0.5, Gamma.regularizedGammaQ(0.5), 1e-10);
        assertEquals(0.5, Gamma.regularizedGammaQ(1.0), 1e-10);
        assertEquals(1.0 - Math.sqrt(Math.PI) / 2.0, Gamma.regularizedGammaQ(0.5, 1.0), 1e-10);
        assertEquals(Math.sqrt(Math.PI) / 2.0, Gamma.regularizedGammaQ(0.5, 2.0), 1e-10);
        assertEquals(1.0 - Math.E * Math.sqrt(Math.PI) / Math.SQRT12, Gamma.regularizedGammaQ(0.5, 0.5), 1e-10);
        assertEquals(Math.E * Math.sqrt(Math.PI) / Math.SQRT12, Gamma.regularizedGammaQ(0.5, 1.5), 1e-10);
        assertNaN(Gamma.regularizedGammaQ(1.5, -1.0), "regularizedGammaQ should return NaN for negative x");

        // Test large inputs
        assertEquals(1.082322695248221e23, Gamma.regularizedGammaQ(1000.0, 1000.0), 1e-5);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPMaxIterationsExceeded() throws MathException {
        Gamma.regularizedGammaP(1.0, 1.0, 1e-15, 100);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQMaxIterationsExceeded() throws MathException {
        Gamma.regularizedGammaQ(1.0, 1.0, 1e-15, 100);
    }
}
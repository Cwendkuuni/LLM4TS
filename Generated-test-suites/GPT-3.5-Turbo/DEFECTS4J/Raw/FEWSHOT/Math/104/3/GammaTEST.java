package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.MathException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Test;

public class GammaTest {
    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 1e-9);
        assertEquals(Double.NaN, Gamma.logGamma(-1.0), 1e-9);
        assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(0.0), 1e-9);
        assertEquals(Double.NEGATIVE_INFINITY, Gamma.logGamma(-10.0), 1e-9);
        assertEquals(4.526317842264078, Gamma.logGamma(10.0), 1e-9);
        assertEquals(9.255783001176952, Gamma.logGamma(100.0), 1e-9);
        assertEquals(13.992406791291833, Gamma.logGamma(1000.0), 1e-9);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 10.0), 1e-9);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(10.0, Double.NaN), 1e-9);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, Double.NaN), 1e-9);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, 10.0), 1e-9);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(10.0, -1.0), 1e-9);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1.0, -1.0), 1e-9);
        assertEquals(0.999999998460285, Gamma.regularizedGammaP(10.0, 0.0), 1e-9);
        assertEquals(0.999999999999993, Gamma.regularizedGammaP(10.0, 10000.0), 1e-9);
    }
    
    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 10.0), 1e-9);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(10.0, Double.NaN), 1e-9);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, Double.NaN), 1e-9);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.0, 10.0), 1e-9);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(10.0, -1.0), 1e-9);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1.0, -1.0), 1e-9);
        assertEquals(1.5397153056250146e-9, Gamma.regularizedGammaQ(10.0, 0.0), 1e-9);
        assertEquals(0.0, Gamma.regularizedGammaQ(10.0, 10000.0), 1e-9);
    }
}
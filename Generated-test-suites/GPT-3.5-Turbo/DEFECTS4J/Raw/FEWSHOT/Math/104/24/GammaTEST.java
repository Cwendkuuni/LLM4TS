package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GammaTest {
    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.001);
        assertEquals(Double.NaN, Gamma.logGamma(-1), 0.001);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.001);
        assertEquals(0.0, Gamma.logGamma(1), 0.001);
        assertEquals(0.693, Gamma.logGamma(2), 0.001);
        assertEquals(1.791, Gamma.logGamma(3), 0.001);
        assertEquals(4.787, Gamma.logGamma(6), 0.001);
    }
    
    @Test
    public void testRegularizedGammaP() {
        assertTrue(Double.isNaN(Gamma.regularizedGammaP(Double.NaN, 1)));
        assertTrue(Double.isNaN(Gamma.regularizedGammaP(1, Double.NaN)));
        assertTrue(Double.isNaN(Gamma.regularizedGammaP(-1, 1)));
        assertTrue(Double.isNaN(Gamma.regularizedGammaP(1, -1)));
        assertEquals(0.0, Gamma.regularizedGammaP(1, 0), 0.001);
        assertEquals(0.632, Gamma.regularizedGammaP(1, 1), 0.001);
        assertEquals(0.951, Gamma.regularizedGammaP(2, 1), 0.001);
        assertEquals(0.995, Gamma.regularizedGammaP(3, 1), 0.001);
        assertEquals(1.0, Gamma.regularizedGammaP(1, Double.POSITIVE_INFINITY), 0.001);
    }
    
    @Test
    public void testRegularizedGammaQ() {
        assertTrue(Double.isNaN(Gamma.regularizedGammaQ(Double.NaN, 1)));
        assertTrue(Double.isNaN(Gamma.regularizedGammaQ(1, Double.NaN)));
        assertTrue(Double.isNaN(Gamma.regularizedGammaQ(-1, 1)));
        assertTrue(Double.isNaN(Gamma.regularizedGammaQ(1, -1)));
        assertEquals(1.0, Gamma.regularizedGammaQ(1, 0), 0.001);
        assertEquals(0.368, Gamma.regularizedGammaQ(1, 1), 0.001);
        assertEquals(0.049, Gamma.regularizedGammaQ(2, 1), 0.001);
        assertEquals(0.004, Gamma.regularizedGammaQ(3, 1), 0.001);
        assertEquals(0.0, Gamma.regularizedGammaQ(1, Double.POSITIVE_INFINITY), 0.001);
    }
}
package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GammaTest {
    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0001);
        assertEquals(-0.5, Gamma.logGamma(1.0), 0.0001);
        assertEquals(0.0, Gamma.logGamma(2.0), 0.0001);
        assertEquals(0.6931, Gamma.logGamma(3.0), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 1.0), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, Double.NaN), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 0.0), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 0.0001);
        assertEquals(0.6321, Gamma.regularizedGammaP(2.0, 1.0), 0.0001);
    }
    
    @Test
    public void testRegularizedGammaQ() {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 1.0), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, Double.NaN), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 0.0), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 1.0), 0.0001);
        assertEquals(0.3679, Gamma.regularizedGammaQ(2.0, 1.0), 0.0001);
    }
}
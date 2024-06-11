package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GammaTest {
    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0);
        assertEquals(Double.NaN, Gamma.logGamma(-5), 0);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0);
        
        assertEquals(0.5723649429247001, Gamma.logGamma(0.1), 1e-6);
        assertEquals(-0.03648997397857634, Gamma.logGamma(1), 1e-6);
        assertEquals(0.6931471805599453, Gamma.logGamma(2), 1e-6);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 1), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(1, Double.NaN), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1, 1), 0);
        
        assertEquals(1.0, Gamma.regularizedGammaP(0, 1), 0);
        assertEquals(0.36787944117144233, Gamma.regularizedGammaP(1, 1), 1e-6);
        assertEquals(0.26424111765711533, Gamma.regularizedGammaP(2, 1), 1e-6);
    }
    
    @Test
    public void testRegularizedGammaQ() {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 1), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(1, Double.NaN), 0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1, 1), 0);
        
        assertEquals(0.0, Gamma.regularizedGammaQ(0, 1), 0);
        assertEquals(0.6321205588285577, Gamma.regularizedGammaQ(1, 1), 1e-6);
        assertEquals(0.7357588823428847, Gamma.regularizedGammaQ(2, 1), 1e-6);
    }
}
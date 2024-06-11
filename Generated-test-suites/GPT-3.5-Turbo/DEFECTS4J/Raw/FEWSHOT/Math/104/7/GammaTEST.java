package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Test;

public class GammaTest {
    //Test logGamma method
    @Test
    public void testLogGamma() {
        assertEquals(13.676416414446214, Gamma.logGamma(10.5), 0.00001);
        assertEquals(Double.NaN, Gamma.logGamma(-10.5), 0.00001);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.00001);
    }

    //Test regularizedGammaP method
    @Test
    public void testRegularizedGammaP() throws MaxIterationsExceededException {
        assertEquals(0.15677476722567353, Gamma.regularizedGammaP(1, 2), 0.00001);
        assertEquals(0.00019864030313712684, Gamma.regularizedGammaP(1, 10), 0.00001);
        assertEquals(0.6321205588285577, Gamma.regularizedGammaP(0.5, 1), 0.00001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1, 2), 0.00001);
    }

    //Test regularizedGammaQ method
    @Test
    public void testRegularizedGammaQ() throws MaxIterationsExceededException {
        assertEquals(0.8432252322926929, Gamma.regularizedGammaQ(1, 2), 0.00001);
        assertEquals(0.9998013596968628, Gamma.regularizedGammaQ(1, 10), 0.00001);
        assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(0.5, 1), 0.00001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1, 2), 0.00001);
    }
}
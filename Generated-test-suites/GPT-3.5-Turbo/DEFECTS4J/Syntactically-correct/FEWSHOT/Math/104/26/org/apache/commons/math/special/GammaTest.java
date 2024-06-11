package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math.MaxIterationsExceededException;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(-1.0), 0.0);
        assertEquals(0.0, Gamma.logGamma(1.0), 0.0);
        assertEquals(0.6931471805599453, Gamma.logGamma(2.0), 0.0);
        assertEquals(1.791759469228055, Gamma.logGamma(3.0), 0.0);
    }

    @Test
    public void testRegularizedGammaP() {
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 1.0), 0.0);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, Double.NaN), 0.0);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, Double.NaN), 0.0);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, 1.0), 0.0);
            assertEquals(Double.NaN, Gamma.regularizedGammaP(1.0, -1.0), 0.0);
            assertEquals(0.0, Gamma.regularizedGammaP(1.0, 0.0), 0.0);
            assertEquals(1.0, Gamma.regularizedGammaP(1.0, Double.POSITIVE_INFINITY), 0.0);
            assertEquals(0.6321205588285577, Gamma.regularizedGammaP(1.0, 1.0), 0.0);
            assertEquals(0.36787944117144233, Gamma.regularizedGammaP(1.0, 2.0), 0.0);
            assertEquals(0.1353352832366127, Gamma.regularizedGammaP(2.0, 1.0), 0.0);
        } catch (MaxIterationsExceededException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        try {
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 1.0), 0.0);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, Double.NaN), 0.0);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, Double.NaN), 0.0);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, 1.0), 0.0);
            assertEquals(Double.NaN, Gamma.regularizedGammaQ(1.0, -1.0), 0.0);
            assertEquals(1.0, Gamma.regularizedGammaQ(1.0, 0.0), 0.0);
            assertEquals(0.0, Gamma.regularizedGammaQ(1.0, Double.POSITIVE_INFINITY), 0.0);
            assertEquals(0.36787944117144233, Gamma.regularizedGammaQ(1.0, 1.0), 0.0);
            assertEquals(0.6321205588285577, Gamma.regularizedGammaQ(1.0, 2.0), 0.0);
            assertEquals(0.8646647167633873, Gamma.regularizedGammaQ(2.0, 1.0), 0.0);
        } catch (MaxIterationsExceededException e) {
            e.printStackTrace();
        }
    }
}
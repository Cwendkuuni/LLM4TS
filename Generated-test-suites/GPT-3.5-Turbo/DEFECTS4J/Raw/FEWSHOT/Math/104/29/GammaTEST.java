package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GammaTest {
    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0);
        assertEquals(0.5723649429247001, Gamma.logGamma(0.5), 0.0);
        assertEquals(0.0, Gamma.logGamma(1.0), 0.0);
        assertEquals(0.6931471805599453, Gamma.logGamma(2.0), 0.0);
        assertEquals(1.791759469228055, Gamma.logGamma(4.0), 0.0);
    }

    @Test
    public void testRegularizedGammaP() throws Exception {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 2.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(2.0, Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.POSITIVE_INFINITY, 2.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(2.0, Double.POSITIVE_INFINITY), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NEGATIVE_INFINITY, 2.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(2.0, Double.NEGATIVE_INFINITY), 0.0);
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 2.0), 0.0);
        assertEquals(0.0, Gamma.regularizedGammaP(2.0, 0.0), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaP(2.0, Double.POSITIVE_INFINITY), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaP(Double.POSITIVE_INFINITY, 2.0), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaP(2.0, Double.NEGATIVE_INFINITY), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaP(Double.NEGATIVE_INFINITY, 2.0), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaP(2.0, 2.0), 0.0);
        assertEquals(0.0018100240789072209, Gamma.regularizedGammaP(2.0, 3.0), 0.0);
        assertEquals(0.7133498878774648, Gamma.regularizedGammaP(2.0, 0.5), 0.0);
    }

    @Test
    public void testRegularizedGammaQ() throws Exception {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 2.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(2.0, Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.POSITIVE_INFINITY, 2.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(2.0, Double.POSITIVE_INFINITY), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NEGATIVE_INFINITY, 2.0), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(2.0, Double.NEGATIVE_INFINITY), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 2.0), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaQ(2.0, 0.0), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaQ(2.0, Double.POSITIVE_INFINITY), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaQ(Double.POSITIVE_INFINITY, 2.0), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaQ(2.0, Double.NEGATIVE_INFINITY), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaQ(Double.NEGATIVE_INFINITY, 2.0), 0.0);
        assertEquals(0.0, Gamma.regularizedGammaQ(2.0, 2.0), 0.0);
        assertEquals(0.9981899759210928, Gamma.regularizedGammaQ(2.0, 3.0), 0.0);
        assertEquals(0.2866501121225352, Gamma.regularizedGammaQ(2.0, 0.5), 0.0);
    }
}
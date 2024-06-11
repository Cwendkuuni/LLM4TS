package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GammaTest {
    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.logGamma(0.0), 0.0);
        assertEquals(0.5723649429247001, Gamma.logGamma(0.1), 0.000001);
        assertEquals(-0.8414655602085464, Gamma.logGamma(0.5), 0.000001);
        assertEquals(0.0, Gamma.logGamma(1.0), 0.0);
        assertEquals(0.0, Gamma.logGamma(2.0), 0.0);
        assertEquals(0.6931471805599453, Gamma.logGamma(3.0), 0.000001);
        assertEquals(1.791759469228055, Gamma.logGamma(4.0), 0.000001);
        assertEquals(3.1780538303479458, Gamma.logGamma(5.0), 0.000001);
        assertEquals(607.4858107046554, Gamma.logGamma(100.0), 0.000001);
    }

    @Test
    public void testRegularizedGammaP() {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(0.0, 0.0), 0.0);
        assertEquals(1.0, Gamma.regularizedGammaP(0.0, 1.0), 0.0);
        assertEquals(0.36787944117144233, Gamma.regularizedGammaP(0.1, 1.0), 0.000001);
        assertEquals(0.6065306597126334, Gamma.regularizedGammaP(0.5, 1.0), 0.000001);
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 1.0), 0.0);
        assertEquals(0.6281741443739172, Gamma.regularizedGammaP(1.1, 1.0), 0.000001);
        assertEquals(0.6931471805599453, Gamma.regularizedGammaP(1.0, 2.0), 0.0);
        assertEquals(0.7364028618842733, Gamma.regularizedGammaP(1.0, 3.0), 0.000001);
        assertEquals(0.9932620530009146, Gamma.regularizedGammaP(10.0, 1.0), 0.000001);
        assertEquals(0.9999970878089338, Gamma.regularizedGammaP(100.0, 1.0), 0.000001);
    }

    @Test
    public void testRegularizedGammaQ() {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, Double.NaN), 0.0);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(0.0, 0.0), 0.0);
        assertEquals(0.0, Gamma.regularizedGammaQ(0.0, 1.0), 0.0);
        assertEquals(0.6321205588285577, Gamma.regularizedGammaQ(0.1, 1.0), 0.000001);
        assertEquals(0.3934693402873666, Gamma.regularizedGammaQ(0.5, 1.0), 0.000001);
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 1.0), 0.0);
        assertEquals(0.37182585562608287, Gamma.regularizedGammaQ(1.1, 1.0), 0.000001);
        assertEquals(0.3068528194400547, Gamma.regularizedGammaQ(1.0, 2.0), 0.000001);
        assertEquals(0.2635971381157267, Gamma.regularizedGammaQ(1.0, 3.0), 0.000001);
        assertEquals(0.006737946999085467, Gamma.regularizedGammaQ(10.0, 1.0), 0.000001);
        assertEquals(2.9121910661695596E-6, Gamma.regularizedGammaQ(100.0, 1.0), 0.000001);
    }
}
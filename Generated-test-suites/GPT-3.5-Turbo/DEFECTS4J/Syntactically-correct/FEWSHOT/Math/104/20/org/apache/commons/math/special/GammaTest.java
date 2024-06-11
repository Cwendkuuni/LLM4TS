package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math.MathException;
import org.junit.Test;

public class GammaTest {
    @Test
    public void testLogGamma() {
        assertEquals(Double.NaN, Gamma.logGamma(Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.logGamma(0), 0.0001);
        assertEquals(0.5723649429246974, Gamma.logGamma(0.5), 0.0001);
        assertEquals(0.0, Gamma.logGamma(1), 0.0001);
        assertEquals(0.6931471805599453, Gamma.logGamma(2), 0.0001);
        assertEquals(1.791759469228055, Gamma.logGamma(4), 0.0001);
        assertEquals(2.9304180225179802, Gamma.logGamma(10), 0.0001);
    }

    @Test
    public void testRegularizedGammaP() throws MathException {
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, 5), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(5, Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(Double.NaN, Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(-1, 5), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaP(5, -1), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaP(0, 5), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaP(5, 0), 0.0001);
        assertEquals(0.09020401066876394, Gamma.regularizedGammaP(0.5, 2), 0.0001);
        assertEquals(0.7174244291059003, Gamma.regularizedGammaP(1, 2), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaP(2, 2), 0.0001);
        assertEquals(0.05610088574764619, Gamma.regularizedGammaP(5, 2), 0.0001);
        assertEquals(0.9992254359860046, Gamma.regularizedGammaP(2, 5), 0.0001);
    }

    @Test
    public void testRegularizedGammaQ() throws MathException {
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, 5), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(5, Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(Double.NaN, Double.NaN), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(-1, 5), 0.0001);
        assertEquals(Double.NaN, Gamma.regularizedGammaQ(5, -1), 0.0001);
        assertEquals(1.0, Gamma.regularizedGammaQ(0, 5), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaQ(5, 0), 0.0001);
        assertEquals(0.0876945000988086, Gamma.regularizedGammaQ(0.5, 2), 0.0001);
        assertEquals(0.2825755708949007, Gamma.regularizedGammaQ(1, 2), 0.0001);
        assertEquals(0.0, Gamma.regularizedGammaQ(2, 2), 0.0001);
        assertEquals(0.9438991142523538, Gamma.regularizedGammaQ(5, 2), 0.0001);
        assertEquals(7.745642310781328E-4, Gamma.regularizedGammaQ(2, 5), 0.0001);
    }
}
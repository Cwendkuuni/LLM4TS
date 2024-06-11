package org.apache.commons.math.special.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

/**
 * Tests for the Gamma class.
 */
public class GammaTest {

    /**
     * Test logGamma method with positive argument.
     */
    @Test
    public void testLogGammaPositive() {
        double result = Gamma.logGamma(1.5);
        double expected = Math.log(Math.PI) + (Math.log(1.5) - 0.5)
                - Math.log(2.0 * Math.sin(Math.PI / 4.0) * Math.gamma(1.5));
        assertEquals(expected, result, 1e-7);
    }

    /**
     * Test logGamma method with negative argument.
     */
    @Test(expected = MathException.class)
    public void testLogGammaNegative() {
        Gamma.logGamma(-1.0);
    }

    /**
     * Test logGamma method with zero argument.
     */
    @Test(expected = MathException.class)
    public void testLogGammaZero() {
        Gamma.logGamma(0.0);
    }

    /**
     * Test logGamma method with large argument.
     */
    @Test
    public void testLogGammaLarge() {
        double result = Gamma.logGamma(100.0);
        double expected = Math.log(Math.PI) + (Math.log(100.0) - 0.5)
                - Math.log(2.0 * Math.sin(Math.PI / 4.0) * Math.gamma(100.0));
        assertEquals(expected, result, 1e-7);
    }

    /**
     * Test regularizedGammaP method with positive a and x.
     */
    @Test
    public void testRegularizedGammaPP() {
        double result = Gamma.regularizedGammaP(3.0, 1.5);
        double expected = Math.exp(-1.5 + 3.0 * Math.log(1.5)
                - Gamma.logGamma(3.0))
                * (1.5 / (3.0 * 1.5 + 1.0))
                * (1.5 / (3.0 * 1.5 + 2.0))
                * (1.5 / (3.0 * 1.5 + 3.0))
                * (1.5 / (3.0 * 1.5 + 4.0));
        assertEquals(expected, result, 1e-12);
    }

    /**
     * Test regularizedGammaP method with negative a or x.
     */
    @Test(expected = MathException.class)
    public void testRegularizedGammaPInvalid() {
        Gamma.regularizedGammaP(-2.0, 2.5);
    }

    /**
     * Test regularizedGammaP method with x = a.
     */
    @Test
    public void testRegularizedGammaPEqual() {
        double result = Gamma.regularizedGammaP(1.0, 1.0);
        double expected = 1.0;
        assertEquals(expected, result, 1e-14);
    }

    /**
     * Test regularizedGammaP method with large a and x.
     */
    @Test
    public void testRegularizedGammaPLarge() {
        double result = Gamma.regularizedGammaP(100.0, 50.0);
        double expected = Math.exp(-50.0 + 100.0 * Math.log(50.0)
                - Gamma.logGamma(100.0))
                / (Math.gamma(1.0) * Math.pow(50.0, 99.0));
        assertEquals(expected, result, 1e-12);
    }

    /**
     * Test regularizedGammaQ method with positive a and x.
     */
    @Test
    public void testRegularizedGammaQ() {
        double result = Gamma.regularizedGammaQ(3.0, 1.5);
        double expected = 1.0 - result;
        assertEquals(expected, result, 1e-12);
    }

    /**
     * Test regularizedGammaQ method with negative a or x.
     */
    @Test(expected = MathException.class)
    public void testRegularizedGammaQInvalid() {
        Gamma.regularizedGammaQ(-2.0, 2.5);
    }

    /**
     * Test regularizedGammaQ method with x = a.
     */
    @Test
    public void testRegularizedGammaQEqual() {
        double result = Gamma.regularizedGammaQ(1.0, 1.0);
        double expected = 0.0;
        assertEquals(expected, result, 1e-14);
    }

    /**
     * Test regularizedGammaQ method with large a and x.
     */
    @Test
    public void testRegularizedGammaQLarge() {
        double result = Gamma.regularizedGammaQ(100.0, 50.0);
        double expected = 1.0 - result;
        assertEquals(expected, result, 1e-12);
    }
}
package org.apache.commons.math.special.GammaTest;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        double expected = 0.5 * Math.log(2.0 * Math.PI) + Math.log(607.0 / 128.0) - 0.5 * Math.log(4.0);
        double actual = Gamma.logGamma(4.0);
        assertEquals(expected, actual, 1e-9);
    }

    @Test(expected = MathException.class)
    public void testLogGammaNaN() {
        double actual = Gamma.logGamma(-1.0);
        fail("Expected MathException, but got " + actual);
    }

    @Test(expected = MathException.class)
    public void testLogGammaZero() {
        double actual = Gamma.logGamma(0.0);
        fail("Expected MathException, but got " + actual);
    }

    @Test(expected = MathException.class)
    public void testLogGammaNonFinite() {
        double actual = Gamma.logGamma(Double.POSITIVE_INFINITY);
        fail("Expected MathException, but got " + actual);

        actual = Gamma.logGamma(Double.NEGATIVE_INFINITY);
        fail("Expected MathException, but got " + actual);
    }

    @Test
    public void testLogGammaLargeInput() {
        double expected = 0.5 * Math.log(2.0 * Math.PI) + Math.log(607.0 / 128.0) + Math.E * (1.0 + Math.log(1260.0 / Math.PI) + Math.log(1.0 + 1.0 / 126.0));
        double actual = Gamma.logGamma(126.0);
        assertEquals(expected, actual, 1e-9);
    }

    @Test
    public void testRegularizedGammaP() {
        double expected = Math.exp(-3.0 + (2.0 * Math.log(3.0)) - Gamma.logGamma(2.0)) * (1.0 / 3.0 + 1.0 / 4.0);
        double actual = Gamma.regularizedGammaP(2.0, 3.0);
        assertEquals(expected, actual, 1e-9);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPNegativeA() {
        double actual = Gamma.regularizedGammaP(-2.0, 3.0);
        fail("Expected MathException, but got " + actual);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPNegativeX() {
        double actual = Gamma.regularizedGammaP(2.0, -3.0);
        fail("Expected MathException, but got " + actual);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPZeroA() {
        double actual = Gamma.regularizedGammaP(0.0, 3.0);
        fail("Expected MathException, but got " + actual);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPZeroX() {
        double actual = Gamma.regularizedGammaP(2.0, 0.0);
        fail("Expected MathException, but got " + actual);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPNotConverged() {
        double actual = Gamma.regularizedGammaP(2.0, 5.0);
        fail("Expected MathException, but got " + actual);
    }

    @Test
    public void testRegularizedGammaPXGreaterThanA() {
        double expected = Math.exp(-5.0 + (3.0 * Math.log(5.0)) - Gamma.logGamma(3.0)) * (1.0 / 5.0 + 1.0 / 6.0 + 1.0 / 7.0);
        double actual = Gamma.regularizedGammaP(3.0, 5.0);
        assertEquals(expected, actual, 1e-9);
    }

    @Test
    public void testRegularizedGammaQ() {
        double expected = Math.exp(-3.0 + (2.0 * Math.log(3.0)) - Gamma.logGamma(2.0));
        double actual = 1.0 - Gamma.regularizedGammaP(2.0, 3.0);
        assertEquals(expected, actual, 1e-9);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQNegativeA() {
        double actual = 1.0 - Gamma.regularizedGammaP(-2.0, 3.0);
        fail("Expected MathException, but got " + actual);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQNegativeX() {
        double actual = 1.0 - Gamma.regularizedGammaP(2.0, -3.0);
        fail("Expected MathException, but got " + actual);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQZeroA() {
        double actual = 1.0 - Gamma.regularizedGammaP(0.0, 3.0);
        fail("Expected MathException, but got " + actual);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQZeroX() {
        double actual = 1.0 - Gamma.regularizedGammaP(2.0, 0.0);
        fail("Expected MathException, but got " + actual);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQNotConverged() {
        double actual = 1.0 - Gamma.regularizedGammaP(2.0, 5.0);
        fail("Expected MathException, but got " + actual);
    }

    @Test
    public void testRegularizedGammaQXLessThanA() {
        double expected = Math.exp(-2.0 + (3.0 * Math.log(2.0)) - Gamma.logGamma(3.0)) * (1.0 / 2.0 + 1.0 / 3.0 + 1.0 / 4.0);
        double actual = Gamma.regularizedGammaQ(3.0, 2.0);
        assertEquals(expected, actual, 1e-9);
    }

    @Test
    public void testRegularizedGammaQXGreaterThanA() {
        double expected = Math.exp(-5.0 + (3.0 * Math.log(5.0)) - Gamma.logGamma(3.0)) * (1.0 / 5.0 + 1.0 / 6.0 + 1.0 / 7.0);
        double actual = Gamma.regularizedGammaQ(3.0, 5.0);
        assertEquals(expected, actual, 1e-9);
    }
}
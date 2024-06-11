package org.apache.commons.math.special.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.special.Gamma;

import org.junit.Test;

/**
 * Test class for the Gamma utility class.
 */
public class GammaTest {

    /**
     * Test logGamma method with positive arguments.
     */
    @Test
    public void testLogGammaPositive() {
        for (double x = 0.5; x <= 5.0; x += 1.0) {
            double result = Gamma.logGamma(x);
            double expected = Math.log(fact(x));
            assertEquals(expected, result, 1e-12);
        }
    }

    /**
     * Test logGamma method with negative arguments.
     */
    @Test(expected = MathException.class)
    public void testLogGammaNegative() {
        Gamma.logGamma(-1.0);
    }

    /**
     * Test logGamma method with non-finite arguments.
     */
    @Test(expected = MathException.class)
    public void testLogGammaNaN() {
        Gamma.logGamma(Double.NaN);
    }

    /**
     * Test logGamma method with zero argument.
     */
    @Test(expected = ArithmeticException.class)
    public void testLogGammaZero() {
        Gamma.logGamma(0.0);
    }

    /**
     * Test regularizedGammaP method with positive a and x.
     */
    @Test
    public void testRegularizedGammaPP() {
        for (double a = 1.5; a <= 5.0; a += 1.0) {
            for (double x = 0.0; x <= a; x += 1.0) {
                double result = Gamma.regularizedGammaP(a, x);
                double expected = pFact(a, x) / fact(a);
                assertEquals(expected, result, 1e-13);
            }
        }
    }

    /**
     * Test regularizedGammaP method with non-positive a.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegularizedGammaPA() {
        Gamma.regularizedGammaP(0.0, 1.0);
    }

    /**
     * Test regularizedGammaP method with non-positive x.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegularizedGammaPX() {
        Gamma.regularizedGammaP(1.5, -1.0);
    }

    /**
     * Test regularizedGammaP method with infinite x.
     */
    @Test
    public void testRegularizedGammaPInf() {
        double result = Gamma.regularizedGammaP(1.5, Double.POSITIVE_INFINITY);
        double expected = Math.exp(-1.5 * Math.log(1.5) + Gamma.logGamma(1.5));
        assertEquals(expected, result, 1e-13);
    }

    /**
     * Test regularizedGammaQ method with positive a and x.
     */
    @Test
    public void testRegularizedGammaQP() {
        for (double a = 1.5; a <= 5.0; a += 1.0) {
            for (double x = 0.0; x <= a; x += 1.0) {
                double result = Gamma.regularizedGammaQ(a, x);
                double expected = 1.0 - pFact(a, x) / fact(a);
                assertEquals(expected, result, 1e-13);
            }
        }
    }

    /**
     * Test regularizedGammaQ method with non-positive a.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegularizedGammaQA() {
        Gamma.regularizedGammaQ(0.0, 1.0);
    }

    /**
     * Test regularizedGammaQ method with non-positive x.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRegularizedGammaQX() {
        Gamma.regularizedGammaQ(1.5, -1.0);
    }

    /**
     * Test regularizedGammaQ method with infinite x.
     */
    @Test
    public void testRegularizedGammaQInf() {
        double result = Gamma.regularizedGammaQ(1.5, Double.POSITIVE_INFINITY);
        double expected = 1.0 - Math.exp(-1.5 * Math.log(1.5) + Gamma.logGamma(1.5));
        assertEquals(expected, result, 1e-13);
    }

    /**
     * Helper method for fact calculation.
     *
     * @param n the factorial.
     * @return the factorial.
     */
    private static double fact(double n) {
        if (n == 0.0 || Double.isNaN(n)) {
            return 1.0;
        }
        double result = 1.0;
        for (double i = 1.0; i <= n; i += 1.0) {
            result *= i;
        }
        return result;
    }

    /**
     * Helper method for pFact calculation.
     *
     * @param a the a parameter.
     * @param x the value.
     * @return pFact(a, x).
     */
    private static double pFact(double a, double x) {
        double result = 1.0;
        for (double i = 1.0; i <= a; i += 1.0) {
            result *= Gamma.regularizedGammaP(a, i) / Gamma.regularizedGammaP(a, x + i);
        }
        return result;
    }

    /**
     * Helper method for complex log gamma testing.
     */
    @Test
    public void testLogGammaComplex() {
        double real = 1.5;
        double imag = 0.1;
        Complex expected = new Complex(Math.log(fact(real)), Math.PI * imag);
        Complex result = new Complex(Gamma.logGamma(new Complex(real, imag)));
        Complex diff = result.subtract(expected);
        assertTrue(diff.abs().isLessThan(new Complex(1.0, 0.0), 1e-13));
    }
}
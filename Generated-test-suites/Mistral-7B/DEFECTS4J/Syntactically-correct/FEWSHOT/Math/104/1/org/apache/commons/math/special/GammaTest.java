package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.MaxIterationsExceededException;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.special.GammaFunction;
import org.apache.commons.math3.special.PsiFunction;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        for (double x = 0.0; x <= 10.0; x += 0.1) {
            double result = Gamma.logGamma(x);
            double expected = GammaFunction.logGamma(x + 1.0);
            assertEquals("logGamma(x) incorrect for x = " + x, expected, result, 1e-10);
        }

        double xNegative = -1.0;
        double resultNegative = Gamma.logGamma(xNegative);
        double expectedNegative = GammaFunction.logGamma(1.0 - xNegative);
        assertEquals("logGamma(x) incorrect for negative x = " + xNegative, expectedNegative, resultNegative, 1e-10);
    }

    @Test(expected = MathException.class)
    public void testLogGammaNaN() {
        double xNaN = Double.NaN;
        Gamma.logGamma(xNaN);
    }

    @Test(expected = MathException.class)
    public void testLogGammaNegative() {
        double xNegative = -1.0;
        Gamma.logGamma(xNegative);
    }

    @Test(expected = MathException.class)
    public void testLogGammaZero() {
        double xZero = 0.0;
        Gamma.logGamma(xZero);
    }

    @Test
    public void testRegularizedGammaP() {
        double a = 1.5;
        double x = 2.5;
        double expected = 0.6681133814312835;
        double result = Gamma.regularizedGammaP(a, x);
        assertEquals("regularizedGammaP(a, x) incorrect for a = " + a + " and x = " + x, expected, result, 1e-10);

        for (double aValue = 0.5; aValue <= 5.0; aValue += 1.0) {
            for (double xValue = 0.0; xValue <= aValue; xValue += 0.1) {
                double resultValue = Gamma.regularizedGammaP(aValue, xValue);
                double expectedValue = PsiFunction.digamma(aValue) - PsiFunction.digamma(aValue - xValue);
                assertEquals("regularizedGammaP(a, x) incorrect for a = " + aValue + " and x = " + xValue, expectedValue, resultValue, 1e-10);
            }
        }
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPNegativeA() {
        double aNegative = -1.0;
        double x = 2.0;
        Gamma.regularizedGammaP(aNegative, x);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPZeroA() {
        double aZero = 0.0;
        double x = 2.0;
        Gamma.regularizedGammaP(aZero, x);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPZeroX() {
        double a = 1.5;
        double xZero = 0.0;
        Gamma.regularizedGammaP(a, xZero);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPMaxIterations() throws MathException {
        double a = 1.5;
        double x = 2.5;
        int maxIterations = 1;
        Gamma.regularizedGammaP(a, x, 1e-10, maxIterations);
    }

    @Test
    public void testRegularizedGammaQ() {
        double a = 1.5;
        double x = 2.5;
        double expected = 0.3318866185687165;
        double result = Gamma.regularizedGammaQ(a, x);
        assertEquals("regularizedGammaQ(a, x) incorrect for a = " + a + " and x = " + x, expected, result, 1e-10);

        for (double aValue = 0.5; aValue <= 5.0; aValue += 1.0) {
            for (double xValue = 0.0; xValue <= aValue; xValue += 0.1) {
                double resultValue = Gamma.regularizedGammaQ(aValue, xValue);
                double expectedValue = 1.0 - PsiFunction.digamma(aValue) + PsiFunction.digamma(aValue - xValue);
                assertEquals("regularizedGammaQ(a, x) incorrect for a = " + aValue + " and x = " + xValue, expectedValue, resultValue, 1e-10);
            }
        }
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQNegativeA() {
        double aNegative = -1.0;
        double x = 2.0;
        Gamma.regularizedGammaQ(aNegative, x);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQZeroA() {
        double aZero = 0.0;
        double x = 2.0;
        Gamma.regularizedGammaQ(aZero, x);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQZeroX() {
        double a = 1.5;
        double xZero = 0.0;
        Gamma.regularizedGammaQ(a, xZero);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaQMaxIterations() throws MathException {
        double a = 1.5;
        double x = 2.5;
        int maxIterations = 1;
        Gamma.regularizedGammaQ(a, x, 1e-10, maxIterations);
    }

    @Test
    public void testRegularizedGammaPAndQSameValues() {
        double a = 1.5;
        double x = 2.5;
        double p = Gamma.regularizedGammaP(a, x);
        double q = Gamma.regularizedGammaQ(a, x);
        assertEquals("regularizedGammaP and regularizedGammaQ should give the same result for a = " + a + " and x = " + x, p, q, 1e-10);
    }

    @Test
    public void testRegularizedGammaComplexP() {
        double real = 1.5;
        double imaginary = 0.5;
        Complex z = new Complex(real, imaginary);
        double expected = 0.3756044744284568 + 0.017375530568035271 * imaginary;
        GammaFunction gammaFunction = new GammaFunction();
        Complex result = gammaFunction.regularizedGamma(z);
        Complex resultReal = Gamma.regularizedGammaP(real, Math.abs(z.getImaginary()));
        Complex resultImaginary = Gamma.regularizedGammaQ(real, Math.abs(z.getImaginary()));
        Complex expectedComplex = resultReal.add(resultImaginary);
        assertArrayEquals("regularizedGammaP and regularizedGammaQ should give the same complex result for a = " + real + " and z = " + z,
                expectedComplex.getReal(), expectedComplex.getImaginary(), expected.realpart(), expected.imaginarypart(), 1e-10);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaComplexNegativeA() {
        double real = -1.0;
        double imaginary = 0.0;
        Complex z = new Complex(real, imaginary);
        Gamma.regularizedGammaP(real, Math.abs(z.getImaginary()));
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaComplexZeroA() {
        double real = 0.0;
        double imaginary = 0.0;
        Complex z = new Complex(real, imaginary);
        Gamma.regularizedGammaP(real, Math.abs(z.getImaginary()));
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaComplexZeroImaginary() {
        double real = 1.5;
        double imaginary = 0.0;
        Complex z = new Complex(real, imaginary);
        Gamma.regularizedGammaQ(real, Math.abs(z.getImaginary()));
    }
}
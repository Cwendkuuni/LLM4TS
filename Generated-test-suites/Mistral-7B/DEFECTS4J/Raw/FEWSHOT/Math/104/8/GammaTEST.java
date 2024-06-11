package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Positive integers
        for (int i = 1; i <= 10; i++) {
            double result = Gamma.logGamma(i);
            double expected = Math.log(factorial(i)) - Math.log(i) - Math.log(2.0 * Math.PI);
            assertEquals(expected, result, 1e-14);
        }

        // Floating point numbers
        for (double x = 1.5; x <= 5.5; x += 0.5) {
            double result = Gamma.logGamma(x);
            double expected = logGammaImpl(x);
            assertEquals(expected, result, 1e-10);
        }

        // Non-positive integers
        for (int i = 0; i <= -5; i++) {
            double result = Gamma.logGamma(i);
            double expected = Double.NaN;
            assertEquals(expected, result);
        }

        // NaN and infinities
        double result = Gamma.logGamma(Double.NaN);
        double expected = Double.NaN;
        assertEquals(expected, result);

        result = Gamma.logGamma(Double.POSITIVE_INFINITY);
        expected = Double.POSITIVE_INFINITY;
        assertEquals(expected, result);

        result = Gamma.logGamma(Double.NEGATIVE_INFINITY);
        expected = Double.NEGATIVE_INFINITY;
        assertEquals(expected, result);
    }

    @Test
    public void testRegularizedGammaP() {
        // Positive integers
        for (int a = 1; a <= 10; a++) {
            for (int x = a; x <= a + 5; x++) {
                double result = Gamma.regularizedGammaP(a, x);
                double expected = factorial(a) / factorial(x);
                assertEquals(expected, result, 1e-14);
            }
        }

        // Floating point numbers
        for (double a = 1.5; a <= 5.5; a += 0.5) {
            for (double x = a; x <= a + 1.5; x += 0.5) {
                double result = Gamma.regularizedGammaP(a, x);
                double expected = Gamma.gamma(a) / Gamma.gamma(a, x);
                assertEquals(expected, result, 1e-10);
            }
        }

        // Non-positive integers or values
        for (int a = 0; a <= -5; a++) {
            for (int x = 0; x <= a; x++) {
                double result = Gamma.regularizedGammaP(a, x);
                double expected = Double.NaN;
                assertEquals(expected, result);
            }
        }

        for (int a = 0; a <= -5; a++) {
            for (double x = a + 1.0; x < 0.0; x++) {
                try {
                    Gamma.regularizedGammaP(a, x);
                    fail("Expected MathException");
                } catch (MathException e) {
                    // expected
                }
            }
        }

        for (double a = -5.0; a < -5.0; a -= 0.5) {
            for (double x = a + 1.0; x < 0.0; x++) {
                try {
                    Gamma.regularizedGammaP(a, x);
                    fail("Expected MathException");
                } catch (MathException e) {
                    // expected
                }
            }
        }

        // Infinities
        double result = Gamma.regularizedGammaP(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        double expected = 1.0;
        assertEquals(expected, result, 1e-14);

        result = Gamma.regularizedGammaP(Double.POSITIVE_INFINITY, 0.0);
        expected = 0.0;
        assertEquals(expected, result, 1e-14);

        result = Gamma.regularizedGammaP(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        expected = 0.0;
        assertEquals(expected, result, 1e-14);

        result = Gamma.regularizedGammaP(Double.NEGATIVE_INFINITY, 0.0);
        expected = 1.0;
        assertEquals(expected, result, 1e-14);
    }

    @Test
    public void testRegularizedGammaQ() {
        // Positive integers
        for (int a = 1; a <= 10; a++) {
            for (int x = a + 1; x <= a + 5; x++) {
                double result = Gamma.regularizedGammaQ(a, x);
                double expected = 1.0 - Gamma.gamma(a) / factorial(x);
                assertEquals(expected, result, 1e-14);
            }
        }

        // Floating point numbers
        for (double a = 1.5; a <= 5.5; a += 0.5) {
            for (double x = a + 1.0; x <= a + 1.5; x += 0.5) {
                double result = Gamma.regularizedGammaQ(a, x);
                double expected = 1.0 - Gamma.gamma(a) / Gamma.gamma(a, x);
                assertEquals(expected, result, 1e-10);
            }
        }

        // Non-positive integers or values
        for (int a = 0; a <= -5; a++) {
            for (int x = 0; x <= a; x++) {
                double result = Gamma.regularizedGammaQ(a, x);
                double expected = Double.NaN;
                assertEquals(expected, result);
            }
        }

        for (int a = 0; a <= -5; a++) {
            for (double x = a + 1.0; x < 0.0; x++) {
                try {
                    Gamma.regularizedGammaQ(a, x);
                    fail("Expected MathException");
                } catch (MathException e) {
                    // expected
                }
            }
        }

        for (double a = -5.0; a < -5.0; a -= 0.5) {
            for (double x = a + 1.0; x < 0.0; x++) {
                try {
                    Gamma.regularizedGammaQ(a, x);
                    fail("Expected MathException");
                } catch (MathException e) {
                    // expected
                }
            }
        }

        // Infinities
        double result = Gamma.regularizedGammaQ(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
        double expected = 1.0;
        assertEquals(expected, result, 1e-14);

        result = Gamma.regularizedGammaQ(Double.POSITIVE_INFINITY, 0.0);
        expected = 0.0;
        assertEquals(expected, result, 1e-14);

        result = Gamma.regularizedGammaQ(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
        expected = 0.0;
        assertEquals(expected, result, 1e-14);

        result = Gamma.regularizedGammaQ(Double.NEGATIVE_INFINITY, 0.0);
        expected = 1.0;
        assertEquals(expected, result, 1e-14);
    }

    private double factorial(int n) {
        double result = 1.0;
        for (int i = 1; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    private double logGammaImpl(double x) {
        double g = 607.0 / 128.0;
        double sum = 0.0;
        for (int i = lanczos.length - 1; i > 0; --i) {
            sum = sum + (lanczos[i] / (x + i));
        }
        sum = sum + lanczos[0];

        double tmp = x + g + .5;
        return ((x + .5) * Math.log(tmp)) - tmp + 0.5 * Math.log(2.0 * Math.PI) + Math.log(sum / x);
    }
}
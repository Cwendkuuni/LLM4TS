package org.apache.commons.math.special;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;
import org.junit.Test;

public class GammaTest {

    @Test
    public void testLogGamma() {
        // Test with valid inputs
        for (double x = 0.1; x <= 5.0; x += 1.0) {
            double result = Gamma.logGamma(x);
            double expected = Math.log(factorial(x)); // Manually computed factorial
            assertEquals(expected, result, 1e-9);
        }
        
        // Test with negative input
        assertFalse("logGamma(-1.0) should return NaN", Double.isNaN(Gamma.logGamma(-1.0)));
        
        // Test with zero input
        assertNull("logGamma(0.0) should return NaN", Gamma.logGamma(0.0));
    }

    @Test(expected = MathException.class)
    public void testLogGammaNaNInput() {
        Gamma.logGamma(Double.NaN);
    }

    @Test(expected = MathException.class)
    public void testLogGammaNegativeInput() {
        Gamma.logGamma(-1.0);
    }

    @Test
    public void testRegularizedGammaP() {
        // Test with valid inputs
        for (double a = 1.1; a <= 5.1; a += 1.0) {
            for (double x = 0.1; x <= 5.0; x += 1.0) {
                double result = Gamma.regularizedGammaP(a, x);
                double expected = pFunction(a, x); // Manually computed p function
                assertEquals(expected, result, 1e-9);
            }
        }

        // Test with a = 0.0
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 1.0), 1e-9);
        
        // Test with a = 1.0 and x = 0.0
        assertEquals(1.0, Gamma.regularizedGammaP(1.0, 0.0), 1e-9);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPNegativeA() {
        Gamma.regularizedGammaP(-1.0, 1.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPNegativeX() {
        Gamma.regularizedGammaP(1.0, -1.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaPNonConvergence() {
        try {
            Gamma.regularizedGammaP(0.5, 1.1);
        } catch (MathException e) {
            assertEquals("Regularized gamma function did not converge",
                         e.getMessage());
            throw e;
        }
    }

    @Test
    public void testRegularizedGammaQ() {
        // Test with valid inputs
        for (double a = 1.1; a <= 5.1; a += 1.0) {
            for (double x = 0.1; x <= 5.0; x += 1.0) {
                if (x < a || a < 1.0) {
                    double result = Gamma.regularizedGammaQ(a, x);
                    double expected = qFunction(a, x); // Manually computed q function
                    assertEquals(expected, result, 1e-9);
                }
            }
        }

        // Test with a = 0.0
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 1.0), 1e-9);
        
        // Test with a = 1.0 and x = 0.0
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 0.0), 1e-9);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQNegativeA() {
        Gamma.regularizedGammaQ(-1.0, 1.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQNegativeX() {
        Gamma.regularizedGammaQ(1.0, -1.0);
    }

    @Test(expected = MathException.class)
    public void testRegularizedGammaQNonConvergence() {
        try {
            Gamma.regularizedGammaQ(0.5, 1.1);
        } catch (MathException e) {
            assertEquals("Regularized gamma function did not converge",
                         e.getMessage());
            throw e;
        }
    }

    @Test
    public void testRegularizedGammaPQSum() {
        for (double a = 1.1; a <= 5.1; a += 1.0) {
            for (double x = 0.1; x <= 5.0; x += 1.0) {
                double p = Gamma.regularizedGammaP(a, x);
                double q = Gamma.regularizedGammaQ(a, x);
                assertEquals(1.0, p + q, 1e-9);
            }
        }
    }

    private double factorial(double n) {
        double result = 1.0;
        for (int i = 1; i <= (int) n; ++i) {
            result *= i;
        }
        return result;
    }

    private double pFunction(double a, double x) {
        double result = 1.0;
        for (int i = 1; i <= (int) Math.floor(x); ++i) {
            result *= (a + i) / (a + i + x);
        }
        return result;
    }

    private double qFunction(double a, double x) {
        return 1.0 - pFunction(a, x);
    }

}
package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.special.Gamma;

public class GammaTest {

    @Test
    public void testLogGammaPositiveValues() {
        for (double i = 1.0; i <= 10.0; i++) {
            assertEquals(Gamma.logGamma(i), Math.log(fact(i)), 0.0001);
        }
    }

    @Test
    public void testLogGammaZero() {
        assertEquals(Double.NaN, Gamma.logGamma(0.0));
    }

    @Test
    public void testLogGammaNegativeValues() {
        for (double i = -1.0; i >= -6.0; i -= 0.5) {
            assertEquals(Gamma.logGamma(i), Gamma.logGamma(1.0 - i), 0.0001);
        }
    }

    @Test
    public void testRegularizedGammaPZero() {
        assertEquals(0.0, Gamma.regularizedGammaP(0.0, 0.0), 0.0);
    }

    @Test
    public void testRegularizedGammaPEpsilon() {
        double epsilon = 1e-14;
        assertEquals(0.5772156649015328606, Gamma.regularizedGammaP(0.5, 1.0), epsilon);
    }

    @Test
    public void testRegularizedGammaPMaxIterations() throws MathException {
        double a = 2.0;
        double x = 1.5;
        double epsilon = 1e-14;
        int maxIterations = 100000;
        assertEquals(0.95125834591654223086, Gamma.regularizedGammaP(a, x), epsilon);

        maxIterations = 10;
        assertThrows(MathException.class, () -> Gamma.regularizedGammaP(a, x, epsilon, maxIterations));
    }

    @Test
    public void testRegularizedGammaPNegativeX() {
        assertFalse(Double.isFinite(Gamma.regularizedGammaP(-1.0, 1.0)));
    }

    @Test
    public void testRegularizedGammaPNegativeA() {
        assertFalse(Double.isFinite(Gamma.regularizedGammaP(0.0, 1.0)));
    }

    @Test
    public void testRegularizedGammaQZero() {
        assertEquals(1.0, Gamma.regularizedGammaQ(0.0, 0.0), 0.0);
    }

    @Test
    public void testRegularizedGammaQOne() {
        assertEquals(0.0, Gamma.regularizedGammaQ(1.0, 1.0), 0.0);
    }

    @Test
    public void testRegularizedGammaQMaxIterations() throws MathException {
        double a = 2.0;
        double x = 1.5;
        double epsilon = 1e-14;
        int maxIterations = 100000;
        assertEquals(0.048788655292675342295, 1.0 - Gamma.regularizedGammaP(a, x), epsilon);

        maxIterations = 10;
        assertThrows(MathException.class, () -> Gamma.regularizedGammaQ(a, x, epsilon, maxIterations));
    }

    private static double fact(double n) {
        double prod = 1.0;
        for (int i = 1; i <= n; i++) {
            prod *= i;
        }
        return prod;
    }
}
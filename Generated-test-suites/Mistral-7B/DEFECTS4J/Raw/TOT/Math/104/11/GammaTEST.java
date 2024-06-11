package org.apache.commons.math.special.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxIterationsExceededException;

import org.junit.Test;

/**
 * Test class for the Gamma utility class.
 */
public class GammaTest {

    /**
     * Test method for {@link Gamma#logGamma(double)} with positive value.
     */
    @Test
    public void testLogGammaPositive() {
        double x = 1.5;
        double expected = 0.405820711; // calculated using Wolfram Alpha
        double actual = Gamma.logGamma(x);
        assertEquals(expected, actual, 1e-6);
    }

    /**
     * Test method for {@link Gamma#logGamma(double)} with negative value.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testLogGammaNegative() {
        double x = -0.5;
        Gamma.logGamma(x);
    }

    /**
     * Test method for {@link Gamma#logGamma(double)} with zero.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testLogGammaZero() {
        double x = 0.0;
        Gamma.logGamma(x);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaP(double, double)} with valid arguments.
     */
    @Test
    public void testRegularizedGammaPValid() {
        double a = 0.5;
        double x = 1.5;
        double expected = 0.327567951; // calculated using Wolfram Alpha
        double actual;

        try {
            actual = Gamma.regularizedGammaP(a, x);
        } catch (MathException e) {
            throw new RuntimeException("Unexpected exception: " + e);
        }

        assertEquals(expected, actual, 1e-6);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaP(double, double)} with negative a.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testRegularizedGammaPNegativeA() {
        double a = -0.5;
        double x = 1.5;
        Gamma.regularizedGammaP(a, x);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaP(double, double)} with non-positive x.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testRegularizedGammaPNonPositiveX() {
        double a = 1.5;
        double x = 0.0;
        Gamma.regularizedGammaP(a, x);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaP(double, double)} with max iterations.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaPMaxIterations() {
        double a = 0.5;
        double x = 1.5;
        double epsilon = 1.0e-12; // smaller than default epsilon
        int maxIterations = 10;
        Gamma.regularizedGammaP(a, x, epsilon, maxIterations);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaQ(double, double)} with valid arguments.
     */
    @Test
    public void testRegularizedGammaQValid() {
        double a = 0.5;
        double x = 1.5;
        double expected = 0.672622049; // calculated using Wolfram Alpha
        double actual;

        try {
            actual = Gamma.regularizedGammaQ(a, x);
        } catch (MathException e) {
            throw new RuntimeException("Unexpected exception: " + e);
        }

        assertEquals(expected, actual, 1e-6);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaQ(double, double)} with negative a.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testRegularizedGammaQNegativeA() {
        double a = -0.5;
        double x = 1.5;
        Gamma.regularizedGammaQ(a, x);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaQ(double, double)} with non-positive x.
     */
    @Test(expected = MathIllegalArgumentException.class)
    public void testRegularizedGammaQNonPositiveX() {
        double a = 1.5;
        double x = 0.0;
        Gamma.regularizedGammaQ(a, x);
    }

    /**
     * Test method for {@link Gamma#regularizedGammaQ(double, double)} with max iterations.
     */
    @Test(expected = MaxIterationsExceededException.class)
    public void testRegularizedGammaQMaxIterations() {
        double a = 0.5;
        double x = 1.5;
        double epsilon = 1.0e-12; // smaller than default epsilon
        int maxIterations = 10;
        Gamma.regularizedGammaQ(a, x, epsilon, maxIterations);
    }
}
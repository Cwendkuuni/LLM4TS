package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link ContinuedFraction} abstract class.
 */
public class ContinuedFractionTest {

    /**
     * Test for {@link ContinuedFraction#evaluate(double)} method with default epsilon and maxIterations.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testEvaluateDefault() {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(Math.PI);
        assertEquals(cf.getExpectedValue(Math.PI), result, 0.0);
    }

    /**
     * Test for {@link ContinuedFraction#evaluate(double, double)} method with small epsilon.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testEvaluateSmallEpsilon() {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(Math.PI, 1e-12);
        assertEquals(cf.getExpectedValue(Math.PI), result, 1e-12);
    }

    /**
     * Test for {@link ContinuedFraction#evaluate(double, int)} method with large maxIterations.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testEvaluateLargeMaxIterations() {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(Math.E, 100000);
        assertEquals(cf.getExpectedValue(Math.E), result, 0.0);
    }

    /**
     * Test for {@link ContinuedFraction#evaluate(double, double, int)} method with small epsilon and large maxIterations.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testEvaluateSmallEpsilonLargeMaxIterations() {
        ContinuedFraction cf = new MyContinuedFraction();
        double result = cf.evaluate(Math.PI, 1e-12, 100000);
        assertEquals(cf.getExpectedValue(Math.PI), result, 1e-12);
    }

    /**
     * Test for {@link ContinuedFraction#evaluate(double)} method with invalid input.
     * @throws Exception if an error occurs during testing.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEvaluateInvalidInput() {
        ContinuedFraction cf = new MyContinuedFraction();
        cf.evaluate('c');
    }

    /**
     * Test for {@link ContinuedFraction#evaluate(double, double)} method with convergence exception.
     * @throws Exception if an error occurs during testing.
     */
    @Test(expected = ConvergenceException.class)
    public void testEvaluateConvergenceException() {
        ContinuedFraction cf = new MyDivergentContinuedFraction();
        cf.evaluate(0.0);
    }

    /**
     * Test for {@link ContinuedFraction#evaluate(double, int)} method with max count exceeded exception.
     * @throws Exception if an error occurs during testing.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceeded() {
        ContinuedFraction cf = new MyMaxIterationsExceededContinuedFraction();
        cf.evaluate(0.0, 10);
    }

    /**
     * Test for {@link ContinuedFraction#evaluate(double, double, int)} method with max count exceeded exception.
     * @throws Exception if an error occurs during testing.
     */
    @Test(expected = MaxCountExceededException.class)
    public void testEvaluateMaxCountExceededWithMaxIterations() {
        ContinuedFraction cf = new MyMaxIterationsExceededContinuedFraction();
        cf.evaluate(0.0, 1e-12, 10);
    }

    private static class MyContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            // Implementation of a coefficients for a specific continued fraction.
            return (n % 2 == 0) ? Math.sin(x) : Math.cos(x);
        }

        @Override
        protected double getB(int n, double x) {
            return (n % 2 == 0) ? Math.cos(x) : -Math.sin(x);
        }

        private double getExpectedValue(double x) {
            // Calculate the expected value for the given input.
            if (x == Math.PI) {
                return Math.sqrt(10.0);
            } else {
                return Math.exp(x);
            }
        }
    }

    private static class MyDivergentContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return 1.0 / (x * x);
        }

        @Override
        protected double getB(int n, double x) {
            return -1.0;
        }
    }

    private static class MyMaxIterationsExceededContinuedFraction extends ContinuedFraction {
        @Override
        protected double getA(int n, double x) {
            return 1.0;
        }

        @Override
        protected double getB(int n, double x) {
            return 1.0;
        }
    }
}
package org.apache.commons.math3.util;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ContinuedFractionTest {
    private class TestContinuedFraction extends ContinuedFraction {
        private final double[] a;
        private final double[] b;

        TestContinuedFraction(double[] a, double[] b) {
            this.a = a;
            this.b = b;
        }

        @Override
        protected double getA(int n, double x) {
            return a[n];
        }

        @Override
        protected double getB(int n, double x) {
            return b[n];
        }
    }

    // Test case 1: Test evaluate method with x = 1
    @Test
    public void testEvaluate_xEquals1() {
        double x = 1;
        double epsilon = 1e-9;
        int maxIterations = Integer.MAX_VALUE;

        // Set up the continued fraction coefficients
        double[] a = {1, 2, 3, 4, 5};
        double[] b = {5, 4, 3, 2, 1};

        // Create the continued fraction object
        ContinuedFraction cf = new TestContinuedFraction(a, b);

        // Evaluate the continued fraction
        double result = cf.evaluate(x, epsilon, maxIterations);

        // Verify the result
        assertEquals(0.65, result, epsilon);
    }

    // Test case 2: Test evaluate method with x = 2
    @Test
    public void testEvaluate_xEquals2() {
        double x = 2;
        double epsilon = 1e-9;
        int maxIterations = Integer.MAX_VALUE;

        // Set up the continued fraction coefficients
        double[] a = {1, -2, 3, -4, 5};
        double[] b = {5, 4, -3, -2, 1};

        // Create the continued fraction object
        ContinuedFraction cf = new TestContinuedFraction(a, b);

        // Evaluate the continued fraction
        double result = cf.evaluate(x, epsilon, maxIterations);

        // Verify the result
        assertEquals(-1.0517241379310345, result, epsilon);
    }

    // Test case 3: Test evaluate method with x = 0
    @Test
    public void testEvaluate_xEquals0() {
        double x = 0;
        double epsilon = 1e-9;
        int maxIterations = Integer.MAX_VALUE;

        // Set up the continued fraction coefficients
        double[] a = {1, 2, 3, 4, 5};
        double[] b = {5, 4, 3, 2, 1};

        // Create the continued fraction object
        ContinuedFraction cf = new TestContinuedFraction(a, b);

        // Evaluate the continued fraction
        double result = cf.evaluate(x, epsilon, maxIterations);

        // Verify the result
        assertEquals(0.2, result, epsilon);
    }

    // Test case 4: Test evaluate method with x = 10
    @Test
    public void testEvaluate_xEquals10() {
        double x = 10;
        double epsilon = 1e-9;
        int maxIterations = Integer.MAX_VALUE;

        // Set up the continued fraction coefficients
        double[] a = {1, -2, 3, -4, 5};
        double[] b = {5, 4, -3, -2, 1};

        // Create the continued fraction object
        ContinuedFraction cf = new TestContinuedFraction(a, b);

        // Evaluate the continued fraction
        double result = cf.evaluate(x, epsilon, maxIterations);

        // Verify the result
        assertEquals(-0.8586956521739131, result, epsilon);
    }

    // Test case 5: Test evaluate method with maxIterations = 0
    @Test
    public void testEvaluate_maxIterationsEquals0() {
        double x = 1;
        double epsilon = 1e-9;
        int maxIterations = 0;

        // Set up the continued fraction coefficients
        double[] a = {1, 2, 3, 4, 5};
        double[] b = {5, 4, 3, 2, 1};

        // Create the continued fraction object
        ContinuedFraction cf = new TestContinuedFraction(a, b);

        // Evaluate the continued fraction
        try {
            double result = cf.evaluate(x, epsilon, maxIterations);
            fail("ConvergenceException not thrown");
        } catch (ConvergenceException e) {
            assertTrue(e.getMessage().contains(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION.toString()));
        }
    }

    // Test case 6: Test evaluate method with infinite divergence
    @Test
    public void testEvaluate_infiniteDivergence() {
        double x = 1;
        double epsilon = 1e-9;
        int maxIterations = Integer.MAX_VALUE;

        // Set up the continued fraction coefficients
        double[] a = {1, 2, 3, 4, 5};
        double[] b = {0, 0, 0, 0, 0};

        // Create the continued fraction object
        ContinuedFraction cf = new TestContinuedFraction(a, b);

        // Evaluate the continued fraction
        try {
            double result = cf.evaluate(x, epsilon, maxIterations);
            fail("ConvergenceException not thrown");
        } catch (ConvergenceException e) {
            assertTrue(e.getMessage().contains(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE.toString()));
        }
    }

    // Test case 7: Test evaluate method with NaN divergence
    @Test
    public void testEvaluate_nanDivergence() {
        double x = 1;
        double epsilon = 1e-9;
        int maxIterations = Integer.MAX_VALUE;

        // Set up the continued fraction coefficients
        double[] a = {1, 2, 3, 4, 5};
        double[] b = {Double.NaN, Double.NaN, Double.NaN, Double.NaN, Double.NaN};

        // Create the continued fraction object
        ContinuedFraction cf = new TestContinuedFraction(a, b);

        // Evaluate the continued fraction
        try {
            double result = cf.evaluate(x, epsilon, maxIterations);
            fail("ConvergenceException not thrown");
        } catch (ConvergenceException e) {
            assertTrue(e.getMessage().contains(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE.toString()));
        }
    }
}
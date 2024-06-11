package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.special.E;
import org.junit.Test;

/**
 * Tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest {

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with positive relative and absolute tolerance.
     */
    @Test
    public void testBrentOptimizerWithPositiveTolerances() {
        double x = 3.0;
        double func = Math.exp(x) - E.E;

        UnivariateFunction f = f -> func;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        PointValuePair result = optimizer.optimize(f, x, null);
        assertNotNull(result);
        assertEquals(E.E, result.getPoint(), 1e-7);
        assertEquals(E.E, result.getValue(), 1e-7);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)}
     * with positive relative and absolute tolerance.
     */
    @Test
    public void testBrentOptimizerWithPositiveTolerancesOnly() {
        double x = 3.0;
        double func = Math.exp(x) - E.E;

        UnivariateFunction f = f -> func;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.0);
        assertThrows(NotStrictlyPositiveException.class, () -> optimizer.optimize(f, x, null));
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with negative relative tolerance.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithNegativeRelativeTolerance() {
        double x = 3.0;
        double func = Math.exp(x) - E.E;

        UnivariateFunction f = f -> func;
        BrentOptimizer optimizer = new BrentOptimizer(-0.01, 0.001, null);
        optimizer.optimize(f, x, null);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double[])}
     * with a function that always returns zero.
     */
    @Test
    public void testBrentOptimizerWithZeroFunction() {
        double x = 3.0;
        UnivariateFunction f = f -> 0.0;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        PointValuePair result = optimizer.optimize(f, x, null);
        assertNotNull(result);
        assertEquals(x, result.getPoint(), 1e-7);
        assertEquals(0.0, result.getValue(), 1e-7);
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double[])}
     * with a function that always throws an exception.
     */
    @Test
    public void testBrentOptimizerWithExceptionFunction() {
        double x = 3.0;
        UnivariateFunction f = f -> {
            throw new IllegalArgumentException("Exception test");
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);
        assertThrows(ConvergenceException.class, () -> optimizer.optimize(f, x, null));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double[])}
     * with a convergence checker that always returns true.
     */
    @Test
    public void testBrentOptimizerWithAlwaysTrueChecker() {
        double x = 3.0;
        double func = Math.exp(x) - E.E;

        UnivariateFunction f = f -> func;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, x -> true);
        PointValuePair result = optimizer.optimize(f, x, null);
        assertNotNull(result);
        // The actual result doesn't matter, since the checker always returns true.
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double[])}
     * with a convergence checker that always throws an exception.
     */
    @Test
    public void testBrentOptimizerWithAlwaysFalseChecker() {
        double x = 3.0;
        double func = Math.exp(x) - E.E;

        UnivariateFunction f = f -> func;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, x -> {
            throw new ConvergenceException("Test exception");
        });
        assertThrows(ConvergenceException.class, () -> optimizer.optimize(f, x, null));
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double[])}
     * with a convergence checker that returns false for the initial point.
     */
    @Test
    public void testBrentOptimizerWithInitialFailChecker() {
        double x = 3.0;
        double func = Math.exp(x) - E.E;

        UnivariateFunction f = f -> func;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, x -> false);
        assertFalse(optimizer.optimize(f, x, null).isSuccess());
    }

    /**
     * Test method for {@link BrentOptimizer#optimize(UnivariateFunction, double, double[])}
     * with a convergence checker that always returns true after the first iteration.
     */
    @Test
    public void testBrentOptimizerWithLateSuccessChecker() {
        double x = 3.0;
        double func = Math.exp(x) - E.E;

        UnivariateFunction f = f -> func;
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, x -> {
            if (optimizer.getIterationCount() == 1) {
                return false;
            }
            return true;
        });
        PointValuePair result = optimizer.optimize(f, x, null);
        assertNotNull(result);
        assertEquals(x, result.getPoint(), 1e-7);
        assertEquals(E.E, result.getValue(), 1e-7);
    }
}
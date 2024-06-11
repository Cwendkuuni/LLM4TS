package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.convergence.SimpleLineSearch;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest {

    private BrentOptimizer optimizer;

    @Before
    public void setUp() {
        optimizer = new BrentOptimizer(1e-5, 1e-5);
    }

    /**
     * Tests the minimization of a simple function.
     */
    @Test
    public void testMinimizeSimple() {
        UnivariateFunction function = x -> x * x;
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(0d, result.getPoint(), 1e-9);
        assertEquals(0d, result.getValue(), 1e-9);
    }

    /**
     * Tests the minimization of a function with a non-zero minimum.
     */
    @Test
    public void testMinimizeNonZero() {
        UnivariateFunction function = x -> x * x - 2 * x + 3;
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(-1d, result.getPoint(), 1e-9);
        assertEquals(1d, result.getValue(), 1e-9);
    }

    /**
     * Tests the minimization of a function with an absolute tolerance.
     */
    @Test
    public void testMinimizeAbsoluteTolerance() {
        optimizer = new BrentOptimizer(1e-5, 1e-3);

        UnivariateFunction function = x -> Math.sin(x) / x;
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        // The exact minimum is not reachable due to rounding errors.
        // Check that the result is close enough to the minimum.
        double min = Math.PI / 2 - 1.001;
        assertEquals(min, result.getPoint(), 1e-5);
        assertTrue(Math.abs(Math.sin(result.getPoint()) / result.getValue()) < 1e-6);
    }

    /**
     * Tests the minimization of a function with a relative tolerance.
     */
    @Test
    public void testMinimizeRelativeTolerance() {
        optimizer = new BrentOptimizer(1e-6, 0);

        UnivariateFunction function = x -> Math.exp(-x) / x;
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        // The exact minimum is not reachable due to rounding errors.
        // Check that the result is close enough to the minimum.
        double min = 0.566763007045128;
        assertTrue(Math.abs(result.getValue() - Math.exp(-min) / min) < 1e-6);
        assertEquals(min, result.getPoint(), 1e-7);
    }

    /**
     * Tests the minimization of a function with a custom convergence checker.
     */
    @Test
    public void testMinimizeCustomChecker() {
        SimpleLineSearch lineSearch = new SimpleLineSearch(0.5);
        optimizer = new BrentOptimizer(1e-5, 1e-5, lineSearch);

        UnivariateFunction function = x -> Math.pow(x - 2, 2);
        UnivariatePointValuePair result = optimizer.optimize(function, GoalType.MINIMIZE);
        assertEquals(2d, result.getPoint(), 1e-9);
        assertEquals(0d, result.getValue(), 1e-9);
    }

    /**
     * Tests the minimization with initial bounds outside the interval.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testMinimizeBoundsOutsideInterval() {
        optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateFunction function = x -> x * x;
        optimizer.optimize(function, GoalType.MINIMIZE, -1d, 0d);
    }
}
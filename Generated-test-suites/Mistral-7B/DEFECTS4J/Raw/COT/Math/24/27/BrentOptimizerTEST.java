package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BrentOptimizer.GoalType;
import org.apache.commons.math3.optim.univariate.ChiSquaredDistribution;
import org.apache.commons.math3.optim.univariate.LinearFunction;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizationTest;

/**
 * Tests for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest extends UnivariateOptimizationTest {

    /**
     * Test the constructor with relative and absolute thresholds.
     */
    @Test
    public void testBrentOptimizer_relAbs() {
        final double relThresh = 1e-6;
        final double absThresh = 1e-8;

        final UnivariateFunction f = new LinearFunction(1d, -2d);
        final BrentOptimizer optimizer = new BrentOptimizer(relThresh, absThresh, null);
        final PointValuePair result = optimizer.optimize(f, new GoalType[] { GoalType.MINIMIZE });

        assertEquals(0d, result.getPoint(), 1e-9);
        assertEquals(-2d, result.getValue(), 1e-9);

        final double x = 2.5;
        final double expected = -2d + (0.5 * (3 - Math.sqrt(5)) * (x - 0d));
        final PointValuePair expectedResult = new PointValuePair(x, expected);
        assertEquals(expectedResult, optimizer.optimize(new LinearFunction(1d, -2d + x), new GoalType[] { GoalType.MINIMIZE }));
    }

    /**
     * Test the constructor with only an absolute threshold.
     */
    @Test
    public void testBrentOptimizer_abs() {
        final double absThresh = 1e-8;

        final UnivariateFunction f = new LinearFunction(1d, -2d);
        final BrentOptimizer optimizer = new BrentOptimizer(1e-5, absThresh, null);
        final PointValuePair result = optimizer.optimize(f, new GoalType[] { GoalType.MINIMIZE });

        assertEquals(0d, result.getPoint(), 1e-9);
        assertEquals(-2d, result.getValue(), 1e-9);

        final double x = 2.5;
        final double expected = -2d + (0.5 * (3 - Math.sqrt(5)) * (x - 0d));
        final PointValuePair expectedResult = new PointValuePair(x, expected);
        assertEquals(expectedResult, optimizer.optimize(new LinearFunction(1d, -2d + x), new GoalType[] { GoalType.MINIMIZE }));
    }

    /**
     * Test the constructor with only a relative threshold.
     */
    @Test
    public void testBrentOptimizer_rel() {
        final double relThresh = 1e-6;

        final UnivariateFunction f = new LinearFunction(1d, -2d);
        final BrentOptimizer optimizer = new BrentOptimizer(relThresh, 0d, null);
        final PointValuePair result = optimizer.optimize(f, new GoalType[] { GoalType.MINIMIZE });

        assertEquals(0d, result.getPoint(), 1e-9);
        assertEquals(-2d, result.getValue(), 1e-9);

        final double x = 2.5;
        final double expected = -2d + (0.5 * (3 - Math.sqrt(5)) * (x - 0d));
        final PointValuePair expectedResult = new PointValuePair(x, expected);
        assertEquals(expectedResult, optimizer.optimize(new LinearFunction(1d, -2d + x), new GoalType[] { GoalType.MINIMIZE }));
    }

    /**
     * Test the constructor with only a ConvergenceChecker.
     */
    @Test
    public void testBrentOptimizer_convergenceChecker() {
        final ConvergenceChecker<PointValuePair> checker = (iter, p, q) -> {
            if (iter > 10) {
                return true;
            }
            return false;
        };

        final UnivariateFunction f = new LinearFunction(1d, -2d);
        final BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8, checker);
        final PointValuePair result = optimizer.optimize(f, new GoalType[] { GoalType.MINIMIZE });

        assertEquals(0d, result.getPoint(), 1e-9);
        assertEquals(-2d, result.getValue(), 1e-9);

        final int maxIter = 20;
        final PointValuePair expectedResult = new PointValuePair(2.5, -2d + (0.5 * (3 - Math.sqrt(5)) * (2.5 - 0d)));
        assertEquals(expectedResult, optimizer.optimize(new LinearFunction(1d, -2d + x), new GoalType[] { GoalType.MINIMIZE }));

        // Check that the optimizer stops after a fixed number of iterations.
        assertFalse(optimizer.optimize(new LinearFunction(1d, x -> x * x), new GoalType[] { GoalType.MINIMIZE }).isPresent());
    }

    /**
     * Test the constructor with invalid arguments.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_absInvalid() {
        final double absThresh = 0d;
        new BrentOptimizer(1e-5, absThresh, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_relInvalid() {
        final double relThresh = 0d;
        new BrentOptimizer(relThresh, 1e-8, null);
    }

    /**
     * Test the constructor with no arguments.
     */
    @Test
    public void testBrentOptimizer_noArgs() {
        final UnivariateFunction f = new LinearFunction(1d, -2d);
        final BrentOptimizer optimizer = new BrentOptimizer(null);
        final PointValuePair result = optimizer.optimize(f, new GoalType[] { GoalType.MINIMIZE });

        assertEquals(0d, result.getPoint(), 1e-9);
        assertEquals(-2d, result.getValue(), 1e-9);

        final double x = 2.5;
        final double expected = -2d + (0.5 * (3 - Math.sqrt(5)) * (x - 0d));
        final PointValuePair expectedResult = new PointValuePair(x, expected);
        assertEquals(expectedResult, optimizer.optimize(new LinearFunction(1d, -2d + x), new GoalType[] { GoalType.MINIMIZE }));
    }

    /**
     * Test optimization of a function with multiple local minima.
     */
    @Test
    public void testBrentOptimizer_multiLocalMinima() {
        final UnivariateFunction f = new ChiSquaredDistribution(1, 1);
        final BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8, null);
        final PointValuePair result = optimizer.optimize(f, new GoalType[] { GoalType.MINIMIZE });

        // The chi-squared distribution has many local minima,
        // but the global minimum is at x = 0.
        assertNotEquals(0d, result.getPoint(), 1e-9);

        // Let's check that the optimizer finds the minimum at x = 0 if we
        // restrict the search interval.
        final PointValuePair expected = new PointValuePair(0d, 0d);
        assertEquals(expected, optimizer.optimize(f, new GoalType[] { GoalType.MINIMIZE }, new double[] { -5d, 5d }));
    }

    /**
     * Test optimization of a function with a flat minimum.
     */
    @Test
    public void testBrentOptimizer_flatMinimum() {
        final UnivariateFunction f = new LinearFunction(1d, 0d);
        final BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-8, null);
        final PointValuePair result = optimizer.optimize(f, new GoalType[] { GoalType.MINIMIZE });

        // The minimum is flat, so the optimizer may return any point in it.
        // Let's check that the function value is zero at the result point.
        assertEquals(0d, result.getValue(), 1e-9);
    }
}
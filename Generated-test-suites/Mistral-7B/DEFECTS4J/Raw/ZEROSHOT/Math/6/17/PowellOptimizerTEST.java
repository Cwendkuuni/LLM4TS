package org.apache.commons.math3.optimization.direct;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.PointValuePairFunction;
import org.apache.commons.math3.optim.point.PointValuePairComparator;
import org.apache.commons.math3.optim.search.BracketingMethod;
import org.apache.commons.math3.optim.search.LineSearch;
import org.apache.commons.math3.optim.search.SimpleLineSearch;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.BracketFinder;
import org.apache.commons.math3.optim.univariate.UnivariateFunctionUtils;
import org.apache.commons.math3.optim.univariate.UnivariateValueChecker;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for the PowellOptimizer class.
 */
public class PowellOptimizerTest {

    /**
     * The optimizer under test.
     */
    private PowellOptimizer optimizer;

    /**
     * Sets up the test by creating an optimizer.
     */
    @Before
    public void setUp() {
        // Create a Powell optimizer with default settings.
        optimizer = new PowellOptimizer(1e-6, 1e-6);
    }

    /**
     * Test that the constructor does not throw an exception when an
     * absolute threshold is provided that is smaller than zero.
     */
    @Test
    public void testNegativeAbsoluteThreshold() {
        assertDoesNotThrow(() -> {
            new PowellOptimizer(1e-6, -1e-6);
        });
    }

    /**
     * Test that the constructor throws an exception when the relative
     * threshold is less than the machine epsilon.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testTooSmallRelativeThreshold() {
        new PowellOptimizer(1e-15, 1e-6);
    }

    /**
     * Test that the optimizer finds the minimum of a quadratic function.
     */
    @Test
    public void testQuadraticFunction() {
        final ObjectiveFunction function = new ObjectiveFunction() {
                public double value(double[] x) {
                    final double x2 = x[0] * x[0];
                    final double x3 = x[0] * x[1] + x[1] * x[1];
                    return x2 + x3;
                }
            };
        final double[] startPoint = new double[]{-1.5, -1.5};
        final PointValuePair result = optimizer.optimize(function, new SimpleBounds(-5, 5), startPoint);
        final double expected = -2.25;
        assertEquals(expected, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-1.5, -1.5}, result.getPoint(), 1e-6);
    }

    /**
     * Test that the optimizer finds the minimum of a quadratic function
     * with a user-defined convergence checker.
     */
    @Test
    public void testQuadraticFunctionWithCustomChecker() {
        final ObjectiveFunction function = new ObjectiveFunction() {
                public double value(double[] x) {
                    final double x2 = x[0] * x[0];
                    final double x3 = x[0] * x[1] + x[1] * x[1];
                    return x2 + x3;
                }
            };
        final double[] startPoint = new double[]{-2, -2};
        final ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                final double error = Math.abs(previous.getValue() - current.getValue());
                return error < 1e-7;
            }
        };
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);
        final PointValuePair result = optimizer.optimize(function, new SimpleBounds(-5, 5), startPoint);
        final double expected = -2.25;
        assertEquals(expected, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-1.5, -1.5}, result.getPoint(), 1e-6);
    }

    /**
     * Test that the optimizer is able to find the minimum of a function
     * with multiple local minima.
     */
    @Test
    public void testFunctionWithMultipleLocalMinima() {
        final ObjectiveFunction function = new ObjectiveFunction() {
                public double value(double[] x) {
                    final double x1 = x[0];
                    final double x2 = x[1];
                    final double term1 = x1 * x1 * (x1 - 1) * (x1 - 1) + x1 * x2 * (x1 - 1) * (x2 - 1);
                    final double term2 = x2 * x2 * (x2 - 1) * (x2 - 1) + x1 * x2 * (x1 - 1) * (x2 - 1);
                    return term1 + term2;
                }
            };
        final double[] startPoint = new double[]{-1.5, -1.5};
        final PointValuePair result = optimizer.optimize(function, new SimpleBounds(-5, 5), startPoint);
        final double expected = -3.5;
        assertEquals(expected, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-2, -2}, result.getPoint(), 1e-6);
    }

    /**
     * Test that the optimizer is able to find the minimum of a function
     * with multiple local minima using a user-defined convergence checker.
     */
    @Test
    public void testFunctionWithMultipleLocalMinimaWithCustomChecker() {
        final ObjectiveFunction function = new ObjectiveFunction() {
                public double value(double[] x) {
                    final double x1 = x[0];
                    final double x2 = x[1];
                    final double term1 = x1 * x1 * (x1 - 1) * (x1 - 1) + x1 * x2 * (x1 - 1) * (x2 - 1);
                    final double term2 = x2 * x2 * (x2 - 1) * (x2 - 1) + x1 * x2 * (x1 - 1) * (x2 - 1);
                    return term1 + term2;
                }
            };
        final double[] startPoint = new double[]{-2, -2};
        final ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                final double error = Math.abs(previous.getValue() - current.getValue());
                return error < 1e-7;
            }
        };
        final PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);
        final PointValuePair result = optimizer.optimize(function, new SimpleBounds(-5, 5), startPoint);
        final double expected = -3.5;
        assertEquals(expected, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-2, -2}, result.getPoint(), 1e-6);
    }

    /**
     * Test that the optimizer finds the minimum of a function with a
     * singularity at the starting point.
     */
    @Test
    public void testSingularityAtStartingPoint() {
        final ObjectiveFunction function = new ObjectiveFunction() {
                public double value(double[] x) {
                    final double x1 = x[0];
                    final double x2 = x[1];
                    if (x1 * x1 + x2 * x2 == 0) {
                        throw new ArithmeticException("Singularity at x");
                    }
                    return x1 * x1 + x2 * x2;
                }
            };
        final double[] startPoint = new double[]{-1, 1};
        try {
            optimizer.optimize(function, new SimpleBounds(-5, 5), startPoint);
            fail("Expected an ArithmeticException due to a singularity at the starting point.");
        } catch (ArithmeticException e) {
            // Expected
        }
    }

    /**
     * Test that the optimizer with a custom line search is able to find the
     * minimum of a function.
     */
    @Test
    public void testFunctionWithCustomLineSearch() {
        final ObjectiveFunction function = new ObjectiveFunction() {
                public double value(double[] x) {
                    final double x1 = x[0];
                    final double x2 = x[1];
                    final double term1 = x1 * x1 * (x1 - 1) * (x1 - 1) + x1 * x2 * (x1 - 1) * (x2 - 1);
                    final double term2 = x2 * x2 * (x2 - 1) * (x2 - 1) + x1 * x2 * (x1 - 1) * (x2 - 1);
                    return term1 + term2;
                }
            };
        final double[] startPoint = new double[]{-2, -2};
        final LineSearch lineSearch = new SimpleLineSearch() {
            @Override
            public double search(double x, double[] fPrime, double step) {
                final double xNext = x + step;
                final double fNext = function.value(new double[]{xNext});
                if (fNext < x) {
                    return xNext;
                }
                throw new IllegalStateException("The line search failed.");
            }
        };
        final PowellOptimizer optimizer = new PowellOptimizer(new AveragedStartingPointGenerator(2), lineSearch);
        final PointValuePair result = optimizer.optimize(function, new SimpleBounds(-5, 5), startPoint);
        final double expected = -3.5;
        assertEquals(expected, result.getValue(), 1e-6);
        assertArrayEquals(new double[]{-2, -2}, result.getPoint(), 1e-6);
    }
}
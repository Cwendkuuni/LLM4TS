package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentMinimizer;
import org.apache.commons.math3.test.AbstractTest;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link BrentOptimizer}.
 */
public class BrentOptimizerTest extends AbstractTest {

    /**
     * Test function for {@link BrentOptimizer}.
     */
    private static class TestFunction implements ObjectiveFunction {
        private static final double EPSILON = 1e-10;

        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * (Math.cos(x) * Math.sin(x) - Math.sin(x) * Math.cos(x));
        }

        @Override
        public GoalType getGoalType() {
            return GoalType.MINIMIZE;
        }

        @Override
        public double intervalMin() {
            return -3.141592653589793;
        }

        @Override
        public double intervalMax() {
            return 3.141592653589793;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof TestFunction)) {
                return false;
            }
            final TestFunction other = (TestFunction) obj;
            if (Double.doubleToLongBits(this.intervalMin) != Double.doubleToLongBits(other.intervalMin)) {
                return false;
            }
            if (Double.doubleToLongBits(this.intervalMax) != Double.doubleToLongBits(other.intervalMax)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "TestFunction()";
        }
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with default ConvergenceChecker.
     */
    @Test
    public void testBrentOptimizerDoubleDouble() {
        final double tolerance = 1e-6;
        final double absoluteThreshold = 1e-5;
        final double relativeThreshold = 1e-4;
        final ConvergenceChecker<PointValuePair> checker = null;

        final TestFunction testFunction = new TestFunction();

        final UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, checker);
        final PointValuePair optimum = optimizer.optimize(testFunction, -5.0, 5.0);
        assertEquals(0.0, optimum.getPoint(), tolerance);
        assertEquals(0.0, optimum.getValue(), tolerance);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * default ConvergenceChecker.
     */
    @Test
    public void testBrentOptimizerDoubleDoubleDefaultChecker() {
        final double tolerance = 1e-6;
        final double absoluteThreshold = 1e-5;
        final double relativeThreshold = 1e-4;

        final TestFunction testFunction = new TestFunction();

        final UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        final PointValuePair optimum = optimizer.optimize(testFunction, -5.0, 5.0);
        assertEquals(0.0, optimum.getPoint(), tolerance);
        assertEquals(0.0, optimum.getValue(), tolerance);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with custom ConvergenceChecker.
     */
    @Test
    public void testBrentOptimizerDoubleDoubleCustomChecker() {
        final double tolerance = 1e-6;
        final double absoluteThreshold = 1e-5;
        final double relativeThreshold = 1e-4;

        final TestFunction testFunction = new TestFunction();
        final int maxIterations = 1000;

        final ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                if (iter > maxIterations) {
                    return true;
                }
                assertEquals(previous, current);
                return false;
            }
        };

        final UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, checker);
        final PointValuePair optimum = optimizer.optimize(testFunction, -5.0, 5.0);
        assertEquals(0.0, optimum.getPoint(), tolerance);
        assertEquals(0.0, optimum.getValue(), tolerance);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * custom ConvergenceChecker.
     */
    @Test
    public void testBrentOptimizerDoubleDoubleCustomCheckerDefault() {
        final double tolerance = 1e-6;
        final double absoluteThreshold = 1e-5;
        final double relativeThreshold = 1e-4;

        final TestFunction testFunction = new TestFunction();
        final int maxIterations = 1000;

        final ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iter, PointValuePair previous, PointValuePair current) {
                if (iter > maxIterations) {
                    return true;
                }
                if (previous != null && current != null) {
                    assertEquals(previous.getPoint(), current.getPoint(), tolerance);
                    assertEquals(previous.getValue(), current.getValue(), tolerance);
                }
                return false;
            }
        };

        final UnivariateOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold);
        final PointValuePair optimum = optimizer.optimize(testFunction, -5.0, 5.0, checker);
        assertEquals(0.0, optimum.getPoint(), tolerance);
        assertEquals(0.0, optimum.getValue(), tolerance);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with error cases.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerDoubleDoubleNegativeRelativeThreshold() {
        final double tolerance = 1e-6;
        final double absoluteThreshold = 1e-5;
        final double relativeThreshold = -1e-4;
        final ConvergenceChecker<PointValuePair> checker = null;

        new BrentOptimizer(relativeThreshold, absoluteThreshold, checker);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with error cases.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerDoubleDoubleNegativeAbsoluteThreshold() {
        final double tolerance = 1e-6;
        final double relativeThreshold = 1e-4;
        final double absoluteThreshold = -1e-5;
        final ConvergenceChecker<PointValuePair> checker = null;

        new BrentOptimizer(relativeThreshold, absoluteThreshold, checker);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double, ConvergenceChecker)}
     * with error cases.
     */
    @Test(expected = NullPointerException.class)
    public void testBrentOptimizerDoubleDoubleNullChecker() {
        final double tolerance = 1e-6;
        final double absoluteThreshold = 1e-5;
        final double relativeThreshold = 1e-4;

        new BrentOptimizer(relativeThreshold, absoluteThreshold, null);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * error cases.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerDoubleDoubleNegativeRelativeThreshold() {
        final double tolerance = 1e-6;
        final double absoluteThreshold = 1e-5;
        final double relativeThreshold = -1e-4;

        new BrentOptimizer(relativeThreshold, absoluteThreshold);
    }

    /**
     * Test method for {@link BrentOptimizer#BrentOptimizer(double, double)} with
     * error cases.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerDoubleDoubleNegativeAbsoluteThreshold() {
        final double tolerance = 1e-6;
        final double relativeThreshold = 1e-4;
        final double absoluteThreshold = -1e-5;

        new BrentOptimizer(relativeThreshold, absoluteThreshold);
    }
}
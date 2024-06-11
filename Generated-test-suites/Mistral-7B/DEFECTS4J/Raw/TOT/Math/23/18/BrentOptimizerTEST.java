package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.special.E;
import org.apache.commons.math3.test.AbstractMathTestCase;

/**
 * Tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest extends AbstractMathTestCase {

    /**
     * Tests that {@code BrentOptimizer} finds the minimum of a simple function.
     */
    public void testSimpleFunction() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        double x = 3.0;
        double tolerance = 1e-6;

        UnivariateOptimizer optimizer = new BrentOptimizer(tolerance / 10, tolerance);
        PointValuePair result = optimizer.optimize(f, x);
        assertEquals(0.0, result.getValue(), tolerance);
        assertEquals(0.0, result.getPoint(), tolerance);
    }

    /**
     * Tests that {@code BrentOptimizer} finds the minimum of a simple function with negative values.
     */
    public void testSimpleFunctionNegative() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return -Math.exp(x);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        double x = -1.0;
        double tolerance = 1e-6;

        UnivariateOptimizer optimizer = new BrentOptimizer(tolerance / 10, tolerance);
        PointValuePair result = optimizer.optimize(f, x);
        assertEquals(-E.E, result.getValue(), tolerance);
        assertEquals(-E.E, result.getPoint(), tolerance);
    }

    /**
     * Tests that {@code BrentOptimizer} handles the case where the minimum is at the boundary of the interval.
     */
    public void testMinimumAtBoundary() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 2.0) / 4.0;
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        double x = 1.0;
        double tolerance = 1e-6;

        UnivariateOptimizer optimizer = new BrentOptimizer(tolerance / 10, tolerance);
        PointValuePair result = optimizer.optimize(f, x);
        assertEquals(0.25, result.getValue(), tolerance);
        assertEquals(1.0, result.getPoint(), tolerance);
    }

    /**
     * Tests that {@code BrentOptimizer} handles the case where the minimum is at the boundary and the function value is zero.
     */
    public void testMinimumAtZeroBoundary() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2.0);
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        double x = 0.0;
        double tolerance = 1e-6;

        UnivariateOptimizer optimizer = new BrentOptimizer(tolerance / 10, tolerance);
        PointValuePair result = optimizer.optimize(f, x);
        assertEquals(0.0, result.getValue(), tolerance);
        assertEquals(0.0, result.getPoint(), tolerance);
    }

    /**
     * Tests that {@code BrentOptimizer} handles the case where the function is not defined on the interval.
     */
    public void testFunctionNotDefinedOnInterval() {
        ObjectiveFunction f = new ObjectiveFunction() {
            @Override
            public double value(double x) {
                throw new UnsupportedOperationException("Function not supported on this interval.");
            }

            @Override
            public GoalType getGoalType() {
                return GoalType.MINIMIZE;
            }
        };
        double x = 0.0;
        double tolerance = 1e-6;

        try {
            UnivariateOptimizer optimizer = new BrentOptimizer(tolerance / 10, tolerance);
            PointValuePair result = optimizer.optimize(f, x);
            fail("Expected an IllegalArgumentException.");
        } catch (IllegalArgumentException e) {
            // Expected.
        }
    }

    /**
     * Tests that {@code BrentOptimizer} handles the case where the user provides an invalid threshold.
     */
    public void testInvalidThresholds() {
        try {
            new BrentOptimizer(-0.1, 0.1);
            fail("Expected a NotStrictlyPositiveException.");
        } catch (NotStrictlyPositiveException e) {
            // Expected.
        }

        try {
            new BrentOptimizer(0.1, -0.1);
            fail("Expected a NumberIsTooSmallException.");
        } catch (NumberIsTooSmallException e) {
            // Expected.
        }
    }
}
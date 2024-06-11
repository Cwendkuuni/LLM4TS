package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.test.AbstractMathTestCase;
import org.apache.commons.math3.util.Precision;

/**
 * Tests for {@link BrentOptimizer} class.
 */
public class BrentOptimizerTest extends AbstractMathTestCase {

    /**
     * Test constructor with absolute and relative thresholds.
     * @throws NotStrictlyPositiveException if the thresholds are not positive.
     */
    @Test
    public void testBrentOptimizerConstructor() {
        double rel = 1.0e-6;
        double abs = 1.0e-5;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        assertEquals(rel, optimizer.getRelativeThreshold(), 1e-9);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 1e-9);
    }

    /**
     * Test constructor with only relative threshold.
     * @throws NotStrictlyPositiveException if the threshold is not positive.
     * @throws NumberIsTooSmallException if the threshold is less than 2 * macheps.
     */
    @Test
    public void testBrentOptimizerConstructorWithRelativeThreshold() {
        double rel = 1.0e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel);
        assertEquals(rel, optimizer.getRelativeThreshold(), 1e-9);
        assertNull(optimizer.getAbsoluteThreshold());
    }

    /**
     * Test minimization of a simple function.
     */
    @Test
    public void testMinimizeSimpleFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0, GoalType.MINIMIZE);
        assertEquals(0.0, result.getX(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test minimization of a function with a minimum at a boundary.
     */
    @Test
    public void testMinimizeFunctionWithMinimumAtBoundary() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x * (Math.abs(x) - 1.0);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        UnivariatePointValuePair result = optimizer.optimize(function, -2.0, 2.0, GoalType.MINIMIZE);
        assertEquals(-1.0, result.getX(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    /**
     * Test minimization of a function with a minimum at a precise point.
     */
    @Test
    public void testMinimizeFunctionWithMinimumAtPrecisePoint() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2.0);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0e-9, 1.0e-9);
        UnivariatePointValuePair result = optimizer.optimize(function, 1.5, 2.5, GoalType.MINIMIZE);
        assertEquals(2.0, result.getX(), 1e-9);
        assertEquals(0.0, result.getValue(), 1e-9);
    }

    /**
     * Test maximization of a simple function.
     */
    @Test
    public void testMaximizeSimpleFunction() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        UnivariatePointValuePair result = optimizer.optimize(function, 0.0, 1.0, GoalType.MAXIMIZE);
        assertEquals(1.0, result.getX(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    /**
     * Test maximization of a function with a maximum at a boundary.
     */
    @Test
    public void testMaximizeFunctionWithMaximumAtBoundary() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return x * x * (1.0 - Math.abs(x));
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0e-5, 1.0e-5);
        UnivariatePointValuePair result = optimizer.optimize(function, -1.0, 1.0, GoalType.MAXIMIZE);
        assertEquals(1.0, result.getX(), 1e-6);
        assertEquals(1.0, result.getValue(), 1e-6);
    }

    /**
     * Test maximization of a function with a maximum at a precise point.
     */
    @Test
    public void testMaximizeFunctionWithMaximumAtPrecisePoint() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 3.0, 2.0);
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1.0e-9, 1.0e-9);
        UnivariatePointValuePair result = optimizer.optimize(function, 2.5, 3.5, GoalType.MAXIMIZE);
        assertEquals(3.0, result.getX(), 1e-9);
        assertEquals(9.0, result.getValue(), 1e-9);
    }

    /**
     * Test optimization with incorrect thresholds.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithInvalidThresholds() {
        new BrentOptimizer(-0.5, 1.0);
    }

    /**
     * Test optimization with relative threshold smaller than 2 * macheps.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithRelativeThresholdTooSmall() {
        new BrentOptimizer(1.0e-8, 1.0e-5);
    }

    /**
     * Test optimization with convergence checker.
     */
    @Test
    public void testOptimizeWithConvergenceChecker() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.abs(Math.sin(x) - 1.0);
            }
        };

        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return current.getValue() < MaxEval.MAX_ITERATIONS * 1.0e-6;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6, checker);
        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0, GoalType.MINIMIZE);
        assertEquals(-3.141592653589793, result.getX(), 1e-6);
        assertEquals(1.192092895507939E-7, result.getValue(), 1e-6);
    }
}
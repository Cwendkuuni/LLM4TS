package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

/**
 * Test cases for BrentOptimizer class.
 */
public class BrentOptimizerTest {

    /**
     * Test constructor with positive relative and absolute thresholds.
     */
    @Test
    public void testConstructorPositive() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        assertNotNull(optimizer);
    }

    /**
     * Test constructor with non-positive absolute threshold.
     */
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorNegativeAbs() {
        new BrentOptimizer(1.0, -1.0);
    }

    /**
     * Test constructor with non-positive relative threshold.
     */
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorNegativeRel() {
        new BrentOptimizer(0.5, 1.0);
    }

    /**
     * Test optimization with simple function.
     */
    @Test
    public void testOptimizationSimple() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariateFunction function = (x) -> Math.pow(x, 2);
        UnivariatePointValuePair result = optimizer.optimize(function, -5.0, 5.0);
        assertEquals(-2.0, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization with non-converging function.
     */
    @Test(expected = AssertionError.class)
    public void testOptimizationNonConverging() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariateFunction function = (x) -> {
            if (x > 0.0) {
                return Math.pow(x, 0.5);
            } else {
                throw new IllegalArgumentException();
            }
        };
        optimizer.optimize(function, -5.0, 5.0);
    }

    /**
     * Test optimization with converging function.
     */
    @Test
    public void testOptimizationConverging() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariateFunction function = (x) -> Math.exp(-x) * Math.sin(x);
        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0);
        assertEquals(-1.1781, result.getPoint(), 1e-5);
    }

    /**
     * Test optimization with custom convergence checker.
     */
    @Test
    public void testOptimizationCustomChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariateFunction function = (x) -> Math.exp(-x) * Math.sin(x);
        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, previous, current) -> {
            double delta = Math.abs(current.getValue() - previous.getValue());
            return delta < 1e-7;
        };
        UnivariatePointValuePair result = optimizer.optimize(function, -10.0, 10.0, checker);
        assertEquals(-1.1781, result.getPoint(), 1e-5);
    }

    /**
     * Test optimization with specified goal type.
     */
    @Test
    public void testOptimizationGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariateFunction function = (x) -> Math.pow(x, 2);
        OptimizationProblem problem = new OptimizationProblem(function, new PointValue(0.0, 0.0));
        problem.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.optimize(problem);
        assertEquals(2.0, result.getPoint(), 1e-6);
    }

    /**
     * Test optimization with empty interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationEmptyInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariateFunction function = (x) -> Math.pow(x, 2);
        optimizer.optimize(function, 0.0, 0.0);
    }

    /**
     * Test optimization with overlapping interval.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationOverlappingInterval() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0e-6, 1.0e-6);
        UnivariateFunction function = (x) -> Math.pow(x, 2);
        optimizer.optimize(function, 1.0, 1.0);
    }
}
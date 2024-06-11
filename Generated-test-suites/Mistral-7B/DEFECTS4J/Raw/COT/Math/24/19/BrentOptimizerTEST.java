package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateFunction;
import org.apache.commons.math3.optim.points.PointValuePair;
import org.apache.commons.math3.optim.initializer.ValueRange;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerBasicFunctionality() {
        UnivariateFunction simpleFunction = new org.apache.commons.math3.optim.functions.Sine();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        UnivariateOptimizer result = optimizer.optimize(simpleFunction, new ValueRange(0, 10), GoalType.MINIMIZE);
        PointValuePair expectedResult = new PointValuePair(Math.PI, Math.sin(Math.PI));
        assertEquals(expectedResult, result);
    }

    @Test
    public void testBrentOptimizerMaximize() {
        UnivariateFunction simpleFunction = new org.apache.commons.math3.optim.functions.Sine();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, GoalType.MAXIMIZE);
        UnivariateOptimizer result = optimizer.optimize(simpleFunction, new ValueRange(0, 10));
        PointValuePair expectedResult = new PointValuePair(-Math.PI, Math.sin(-Math.PI));
        assertEquals(expectedResult, result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, -1e-5);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(-1e-5, 1e-5);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerTooSmallRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-5);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        UnivariateFunction simpleFunction = new org.apache.commons.math3.optim.functions.Sine();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, null);
        ConvergenceChecker<PointValuePair> noConvergenceChecker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return false;
            }
        };
        optimizer.setConvergenceChecker(noConvergenceChecker);
        UnivariateOptimizer result = optimizer.optimize(simpleFunction, new ValueRange(0, 10), GoalType.MINIMIZE);
        // The optimization should not converge because the convergence checker does not allow it.
        assertFalse(result instanceof PointValuePair);
    }

    @Test
    public void testBrentOptimizerConvergence() {
        UnivariateFunction simpleFunction = new org.apache.commons.math3.optim.functions.Sine();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5, new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                return current.getValue().isZero();
            }
        });
        UnivariateOptimizer result = optimizer.optimize(simpleFunction, new ValueRange(0, 10), GoalType.MINIMIZE);
        PointValuePair expectedResult = new PointValuePair(Math.PI, Math.sin(Math.PI));
        assertEquals(expectedResult, result);
    }

    @Test
    public void testBrentOptimizerEmptyInterval() {
        UnivariateFunction simpleFunction = new org.apache.commons.math3.optim.functions.Sine();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        try {
            optimizer.optimize(simpleFunction, new ValueRange(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY), GoalType.MINIMIZE);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

    @Test
    public void testBrentOptimizerOutOfRangeInterval() {
        UnivariateFunction simpleFunction = new org.apache.commons.math3.optim.functions.Sine();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        try {
            optimizer.optimize(simpleFunction, new ValueRange(1, Double.NEGATIVE_INFINITY), GoalType.MINIMIZE);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }

    @Test
    public void testBrentOptimizerFunctionNotDefinedInterval() {
        UnivariateFunction simpleFunction = new org.apache.commons.math3.optim.functions.Sine();
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-5);
        try {
            optimizer.optimize(simpleFunction, new ValueRange(Math.PI, Math.PI), GoalType.MINIMIZE);
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            // Expected result
        }
    }
}
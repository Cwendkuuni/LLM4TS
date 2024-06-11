package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class BrentOptimizerTest {
    // Basic tests for constructors

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbs() {
        new BrentOptimizer(0.5, -0.1, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerTooSmallRel() {
        new BrentOptimizer(0.0, 0.1, null);
    }

    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        assertNotNull(optimizer);
    }

    @Test
    public void testBrentOptimizerNullChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0, null);
        assertNotNull(optimizer);
    }

    // Tests for doOptimize method

    @Test(expected = IllegalArgumentException.class)
    public void testDoOptimizeNullFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setFunction(null);
        optimizer.optimize();
    }

    @Test
    public void testDoOptimizeSimpleFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setFunction(x -> Math.pow(x, 2));
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimizeFunctionWithInflectionPoint() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setFunction(x -> Math.pow(x, 3) - 2 * Math.pow(x, 2) + x);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(-1.0, result.getPoint(), 0.0001);
        assertEquals(2.0, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimizeFunctionWithMultipleRoots() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setFunction(x -> Math.pow(x, 3) - 3 * Math.pow(x, 2) + 3 * x - 1);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(-1.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
        assertEquals(1.0, result.getPoint(), 0.0001);
        assertEquals(1.0, result.getValue(), 0.0001);
    }

    @Test
    public void testDoOptimizeFunctionWithIntervals() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setFunction(x -> Math.pow(x, 2) * FastMath.sin(FastMath.PI * x));
        optimizer.setMin(-5.0);
        optimizer.setMax(5.0);
        UnivariatePointValuePair result = optimizer.optimize();
        assertTrue(result.getValue() < 0.0);
    }

    @Test
    public void testDoOptimizeFunctionWithLimitCycle() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setFunction(x -> FastMath.sin(x) / x);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }

    // Tests for setGoalType method

    @Test(expected = IllegalArgumentException.class)
    public void testSetGoalTypeNull() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setGoalType(null);
    }

    @Test
    public void testSetGoalTypeMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        assertFalse(optimizer.isGoalTypeMaximize());
        assertTrue(optimizer.isGoalTypeMinimize());
    }

    @Test
    public void testSetGoalTypeMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        assertFalse(optimizer.isGoalTypeMinimize());
        assertTrue(optimizer.isGoalTypeMaximize());
    }

    // Tests for setConvergenceChecker method

    @Test
    public void testSetConvergenceCheckerNull() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setConvergenceChecker(null);
        assertNotNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testSetConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> myChecker = (iter, previous, current) -> {
            if (iter > 10) {
                return true;
            }
            if (Precision.abs(current.getValue() - previous.getValue()) < 0.001) {
                return true;
            }
            return false;
        };
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setFunction(x -> Math.pow(x, 2));
        optimizer.setConvergenceChecker(myChecker);
        UnivariatePointValuePair result = optimizer.optimize();
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
}
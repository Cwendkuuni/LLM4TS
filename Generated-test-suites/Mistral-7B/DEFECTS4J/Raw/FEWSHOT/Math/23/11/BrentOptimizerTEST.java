package org.apache.commons.cli;

import org.apache.commons.math3.optimization.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerConstruction() {
        // Test constructor with relative and absolute thresholds, and null checker
        double relThreshold = 1.0e-6;
        double absThreshold = 1.0e-8;
        BrentOptimizer optimizer = new BrentOptimizer(relThreshold, absThreshold, null);
        assertEquals(relThreshold, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absThreshold, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());

        // Test constructor with relative and absolute thresholds only
        optimizer = new BrentOptimizer(relThreshold, absThreshold);
        assertEquals(relThreshold, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absThreshold, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithNegativeAbsoluteThreshold() {
        double relThreshold = 1.0e-6;
        double absThreshold = -1.0e-8;
        new BrentOptimizer(relThreshold, absThreshold, null);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithTooSmallRelativeThreshold() {
        double relThreshold = 1.0e-9;
        double absThreshold = 1.0e-8;
        new BrentOptimizer(relThreshold, absThreshold, null);
    }

    @Test
    public void testBrentOptimizerNullFunction() {
        double relThreshold = 1.0e-6;
        double absThreshold = 1.0e-8;
        BrentOptimizer optimizer = new BrentOptimizer(relThreshold, absThreshold, null);
        try {
            optimizer.optimize(null, null, null);
        } catch (NullPointerException e) {
            // Expected
            assertSame("The function cannot be null.", e.getMessage());
        }
    }

    @Test
    public void testBrentOptimizerWithConvergenceChecker() {
        double relThreshold = 1.0e-6;
        double absThreshold = 1.0e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = new MockConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(relThreshold, absThreshold, checker);
        assertEquals(relThreshold, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(absThreshold, optimizer.getAbsoluteThreshold(), 0.0);
        assertSame(checker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testBrentOptimizerGoalType() {
        double relThreshold = 1.0e-6;
        double absThreshold = 1.0e-8;
        BrentOptimizer optimizer = new BrentOptimizer(relThreshold, absThreshold, null);
        assertEquals(GoalType.MINIMIZE, optimizer.getGoalType());
    }

    @Test
    public void testBrentOptimizerDefaultFunctions() {
        double relThreshold = 1.0e-6;
        double absThreshold = 1.0e-8;
        BrentOptimizer optimizer = new BrentOptimizer(relThreshold, absThreshold, null);
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] fx = {3.0, 2.0, 1.0, 4.0, 5.0};
        UnivariatePointValuePair result = optimizer.optimize(x, fx, null);
        assertEquals(x[2], result.getX(), 0.0);
        assertEquals(fx[2], result.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerCustomFunctions() {
        double relThreshold = 1.0e-6;
        double absThreshold = 1.0e-8;
        Function function = new MockFunction();
        BrentOptimizer optimizer = new BrentOptimizer(relThreshold, absThreshold, null);
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] fx = {3.0, 2.0, 1.0, 4.0, 5.0};
        UnivariatePointValuePair result = optimizer.optimize(x, fx, function);
        assertEquals(x[2], result.getX(), 0.0);
        assertEquals(fx[2], result.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerWithConvergenceCheckerCustomFunctions() {
        double relThreshold = 1.0e-6;
        double absThreshold = 1.0e-8;
        Function function = new MockFunction();
        ConvergenceChecker<UnivariatePointValuePair> checker = new MockConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(relThreshold, absThreshold, checker);
        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] fx = {3.0, 2.0, 1.0, 4.0, 5.0};
        UnivariatePointValuePair result = optimizer.optimize(x, fx, function);
        assertEquals(x[2], result.getX(), 0.0);
        assertEquals(fx[2], result.getValue(), 0.0);

        // Test convergence checker
        double[] xChecker = {1.0, 2.0, 1.5, 2.5, 3.0};
        double[] fxChecker = {3.0, 2.0, 0.5, 4.5, 5.0};
        checker.converged(0, null, null); // No previous and current points
        checker.converged(0, new UnivariatePointValuePair(xChecker[0], fxChecker[0]), null); // Initial point
        checker.converged(0, new UnivariatePointValuePair(xChecker[1], fxChecker[1]), new UnivariatePointValuePair(xChecker[0], fxChecker[0])); // First iteration
        checker.converged(1, new UnivariatePointValuePair(xChecker[2], fxChecker[2]), new UnivariatePointValuePair(xChecker[1], fxChecker[1])); // Second iteration
        checker.converged(1, result, new UnivariatePointValuePair(xChecker[2], fxChecker[2])); // Third iteration, with best point
    }

    @Test
    public void testBrentOptimizerWithDifferentGoalTypes() {
        double relThreshold = 1.0e-6;
        double absThreshold = 1.0e-8;
        BrentOptimizer optimizerMin = new BrentOptimizer(relThreshold, absThreshold, null);
        BrentOptimizer optimizerMax = new BrentOptimizer(relThreshold, absThreshold, null);
        optimizerMin.setGoalType(GoalType.MINIMIZE);
        optimizerMax.setGoalType(GoalType.MAXIMIZE);

        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] fx = {3.0, 2.0, 1.0, 4.0, 5.0};
        UnivariatePointValuePair resultMin = optimizerMin.optimize(x, fx, null);
        UnivariatePointValuePair resultMax = optimizerMax.optimize(x, fx, null);

        assertEquals(x[2], resultMin.getX(), 0.0);
        assertEquals(fx[2], resultMin.getValue(), 0.0);
        assertEquals(x[4], resultMax.getX(), 0.0);
        assertEquals(fx[4], resultMax.getValue(), 0.0);
    }

    @Test
    public void testBrentOptimizerWithCustomConvergenceChecker() {
        double relThreshold = 1.0e-6;
        double absThreshold = 1.0e-8;

        ConvergenceChecker<UnivariatePointValuePair> customChecker = new CustomConvergenceChecker();
        BrentOptimizer optimizer = new BrentOptimizer(relThreshold, absThreshold, customChecker);

        double[] x = {1.0, 2.0, 3.0, 4.0, 5.0};
        double[] fx = {3.0, 2.0, 1.0, 4.0, 5.0};

        UnivariatePointValuePair result = optimizer.optimize(x, fx, null);
        assertEquals(x[2], result.getX(), 0.0);
        assertEquals(fx[2], result.getValue(), 0.0);

        // Test convergence checker
        customChecker.reset();
        optimizer.optimize(x, fx, null);
        assertFalse(customChecker.isConverged());

        optimizer.optimize(x, fx, null);
        assertTrue(customChecker.isConverged());
    }
}

class MockFunction implements UnivariateFunction {
    @Override
    public double value(double x) {
        return Math.pow(x, 2.0);
    }
}

class MockConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
    private int count;

    @Override
    public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
        count++;
        return count > 2;
    }

    public void reset() {
        count = 0;
    }
}

class CustomConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
    private boolean isConverged = false;

    @Override
    public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
        if (previous != null) {
            isConverged = (previous.getValue() - current.getValue()) < 0.001;
        } else {
            isConverged = true;
        }
        return isConverged;
    }
}
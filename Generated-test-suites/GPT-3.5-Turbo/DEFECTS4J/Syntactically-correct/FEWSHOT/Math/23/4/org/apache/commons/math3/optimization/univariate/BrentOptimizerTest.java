package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Test;

import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeRelativeThreshold() {
        new BrentOptimizer(-0.5, 1);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerSmallRelativeThreshold() {
        new BrentOptimizer(1.2E-15, 1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerNegativeAbsoluteThreshold() {
        new BrentOptimizer(1.2, -1);
    }

    @Test
    public void testBrentOptimizerWithoutUserConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.2, 0.001);
        optimizer.optimize();
    }

    @Test
    public void testBrentOptimizerWithUserConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.2, 0.001, (iteration, previous, current)
                -> iteration > 10 || Math.abs(previous.getValue() - current.getValue()) < 1E-6);
        optimizer.optimize();
    }

    @Test
    public void testBrentOptimizerBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1.2, 0.001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        UnivariatePointValuePair best = optimizer.best(a, b, true);
        assertEquals(b, best);
    }

    @Test
    public void testBrentOptimizerBestEqualValues() {
        BrentOptimizer optimizer = new BrentOptimizer(1.2, 0.001);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 10.0);
        UnivariatePointValuePair best = optimizer.best(a, b, true);
        assertEquals(a, best);
    }
}
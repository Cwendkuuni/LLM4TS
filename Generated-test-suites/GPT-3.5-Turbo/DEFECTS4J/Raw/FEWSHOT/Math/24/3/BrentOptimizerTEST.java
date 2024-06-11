package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerRelativeThresholdTooSmall() {
        new BrentOptimizer(1e-15, 0.1);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerAbsoluteThresholdIsZero() {
        new BrentOptimizer(1e-12, 0.0);
    }

    @Test
    public void testBrentOptimizerGetRelativeThreshold() {
        double rel = 1e-12;
        double abs = 0.1;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        Assert.assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
    }

    @Test
    public void testBrentOptimizerGetAbsoluteThreshold() {
        double rel = 1e-12;
        double abs = 0.1;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        Assert.assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
    }

    @Test
    public void testBrentOptimizerDoOptimizeMinimize() {
        double rel = 1e-12;
        double abs = 0.1;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        Assert.assertNotNull(result);
    }

    @Test
    public void testBrentOptimizerDoOptimizeMaximize() {
        double rel = 1e-12;
        double abs = 0.1;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        Assert.assertNotNull(result);
    }

    @Test
    public void testBrentOptimizerDoOptimizeConvergenceCheckerNull() {
        double rel = 1e-12;
        double abs = 0.1;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        Assert.assertNotNull(result);
    }

    @Test
    public void testBrentOptimizerDoOptimizeConvergenceCheckerConverged() {
        double rel = 1e-12;
        double abs = 0.1;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return true;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.doOptimize();
        Assert.assertNotNull(result);
    }

    @Test
    public void testBrentOptimizerBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 20.0);
        boolean isMinim = true;
        BrentOptimizer optimizer = new BrentOptimizer(1e-12, 0.1);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        Assert.assertNotNull(result);
    }

}
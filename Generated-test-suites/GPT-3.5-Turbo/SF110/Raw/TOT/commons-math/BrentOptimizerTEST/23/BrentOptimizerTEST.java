package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer_LowerThanMinRelativeTolerance_ThrowsException() {
        double rel = -1.0;
        double abs = 0.01;
        ConvergenceChecker<UnivariatePointValuePair> checker = new SimplePointChecker<>();

        try {
            BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        } catch (NumberIsTooSmallException e) {
            Assert.assertEquals(rel, e.getArgument(), 0.0);
            Assert.assertEquals(BrentOptimizer.MIN_RELATIVE_TOLERANCE, e.getMinimum(), 0.0);
            Assert.assertTrue(e.getIncludingMin());
        }
    }

    @Test
    public void testBrentOptimizer_NonPositiveAbsoluteTolerance_ThrowsException() {
        double rel = 0.1;
        double abs = 0.0;
        ConvergenceChecker<UnivariatePointValuePair> checker = new SimplePointChecker<>();

        try {
            BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals(abs, e.getArgument(), 0.0);
        }
    }

    @Test
    public void testBrentOptimizer_DefaultConvergenceChecker() {
        double rel = 0.1;
        double abs = 0.01;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);

        ConvergenceChecker<UnivariatePointValuePair> checker = optimizer.getConvergenceChecker();
        Assert.assertNull(checker);
    }

    @Test
    public void testDoOptimize_MinimizationGoalType_ComputesOptimalSolution() {
        double rel = 0.1;
        double abs = 0.01;
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double mid = 0.0;
        double max = 10.0;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setGoalType(goalType);
        optimizer.setMin(min);
        optimizer.setStartValue(mid);
        optimizer.setMax(max);

        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Assume that the optimal solution should be around the middle of the range
        Assert.assertEquals(mid, result.getPoint(), abs);
    }

    @Test
    public void testDoOptimize_MaximizationGoalType_ComputesOptimalSolution() {
        double rel = 0.1;
        double abs = 0.01;
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -10.0;
        double mid = 0.0;
        double max = 10.0;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        optimizer.setGoalType(goalType);
        optimizer.setMin(min);
        optimizer.setStartValue(mid);
        optimizer.setMax(max);

        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // Assume that the optimal solution should be around the middle of the range
        Assert.assertEquals(mid, result.getPoint(), abs);
    }

    @Test
    public void testBest_NullFirstArgument_ReturnsSecondArgument() {
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 0.5);

        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair result = optimizer.best(a, b, true);

        Assert.assertEquals(b, result);
    }

    @Test
    public void testBest_NullSecondArgument_ReturnsFirstArgument() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = null;

        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair result = optimizer.best(a, b, true);

        Assert.assertEquals(a, result);
    }

    @Test
    public void testBest_IsMinimizationGoalType_FirstValueIsLess_ReturnsFirstArgument() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.4);

        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair result = optimizer.best(a, b, true);

        Assert.assertEquals(a, result);
    }

    @Test
    public void testBest_IsMinimizationGoalType_SecondValueIsLess_ReturnsSecondArgument() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.4);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.5);

        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair result = optimizer.best(a, b, true);

        Assert.assertEquals(b, result);
    }

    @Test
    public void testBest_IsMinimizationGoalType_ValuesAreEqual_ReturnsFirstArgument() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.5);

        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair result = optimizer.best(a, b, true);

        Assert.assertEquals(a, result);
    }

    @Test
    public void testBest_IsMaximizationGoalType_FirstValueIsGreater_ReturnsFirstArgument() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.4);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.5);

        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair result = optimizer.best(a, b, false);

        Assert.assertEquals(a, result);
    }

    @Test
    public void testBest_IsMaximizationGoalType_SecondValueIsGreater_ReturnsSecondArgument() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.4);

        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair result = optimizer.best(a, b, false);

        Assert.assertEquals(b, result);
    }

    @Test
    public void testBest_IsMaximizationGoalType_ValuesAreEqual_ReturnsFirstArgument() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.5);

        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.01);
        UnivariatePointValuePair result = optimizer.best(a, b, false);

        Assert.assertEquals(a, result);
    }

}
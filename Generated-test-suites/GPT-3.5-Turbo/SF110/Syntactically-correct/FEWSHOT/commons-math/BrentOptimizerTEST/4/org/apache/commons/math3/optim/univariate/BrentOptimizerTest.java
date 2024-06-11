package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.util.FastMath;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        double rel = 0.01;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);

        double expectedRel = optimizer.relativeThreshold;
        double expectedAbs = optimizer.absoluteThreshold;

        assertEquals(rel, expectedRel, 0.0);
        assertEquals(abs, expectedAbs, 0.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerThrowsNumberIsTooSmallException() {
        double rel = 0.0001;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        new BrentOptimizer(rel, abs, checker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerThrowsNotStrictlyPositiveException() {
        double rel = 0.01;
        double abs = 0.0;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;

        new BrentOptimizer(rel, abs, checker);
    }

    @Test
    public void testDoOptimize() {
        double epsilon = 1e-6;

        double lo = -10.0;
        double mid = 0.0;
        double hi = 10.0;
        boolean isMinim = true;

        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        double relativeThreshold = 0.01;
        double absoluteThreshold = 0.001;

        BrentOptimizer optimizer = new BrentOptimizer(relativeThreshold, absoluteThreshold, checker);

        optimizer.setMin(lo);
        optimizer.setStartValue(mid);
        optimizer.setMax(hi);
        optimizer.setGoalType(isMinim ? GoalType.MINIMIZE : GoalType.MAXIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        double expectedValue = 0.0;
        double expectedPoint = 0.0;

        assertEquals(expectedValue, result.getValue(), epsilon);
        assertEquals(expectedPoint, result.getPoint(), epsilon);
    }

    @Test
    public void testBest() {
        double epsilon = 1e-6;

        UnivariatePointValuePair a = new UnivariatePointValuePair(-10.0, -5.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(10.0, 5.0);

        boolean isMinim = true;

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        UnivariatePointValuePair expected = a;

        assertEquals(expected.getValue(), result.getValue(), epsilon);
        assertEquals(expected.getPoint(), result.getPoint(), epsilon);
    }

    @Test
    public void testBestNull() {
        double epsilon = 1e-6;

        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(10.0, 5.0);

        boolean isMinim = true;

        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001, null);

        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);

        UnivariatePointValuePair expected = b;

        assertEquals(expected.getValue(), result.getValue(), epsilon);
        assertEquals(expected.getPoint(), result.getPoint(), epsilon);
    }

}
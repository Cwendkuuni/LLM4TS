package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.LineSearch;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.BracketFinder;
import org.apache.commons.math3.optimization.univariate.SimpleUnivariateValueChecker;
import org.junit.Test;
import org.junit.Assert;

import static org.mockito.Mockito.*;

public class PowellOptimizerTest {

    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer();
        Assert.assertNull(optimizer.getConvergenceChecker());
        Assert.assertEquals(0.004, optimizer.getRelativeThreshold(), 0.001);
        Assert.assertEquals(Double.MIN_VALUE, optimizer.getAbsoluteThreshold(), 0.0);
        Assert.assertNull(optimizer.getLineSearch());
    }

    @Test
    public void testCustomConstructor() {
        double relativeThreshold = 1e-3;
        double absoluteThreshold = 1e-5;
        ConvergenceChecker<PointValuePair> customChecker = mock(ConvergenceChecker.class);
        LineSearch lineSearch = mock(LineSearch.class);

        PowellOptimizer optimizer = new PowellOptimizer(relativeThreshold, absoluteThreshold, customChecker, lineSearch);
        Assert.assertEquals(relativeThreshold, optimizer.getRelativeThreshold(), 0.001);
        Assert.assertEquals(absoluteThreshold, optimizer.getAbsoluteThreshold(), 0.0);
        Assert.assertSame(customChecker, optimizer.getConvergenceChecker());
        Assert.assertSame(lineSearch, optimizer.getLineSearch());
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testCustomConstructorWithInvalidAbsoluteThreshold() {
        double relativeThreshold = 1e-3;
        double absoluteThreshold = 0.0;
        ConvergenceChecker<PointValuePair> customChecker = mock(ConvergenceChecker.class);
        LineSearch lineSearch = mock(LineSearch.class);

        new PowellOptimizer(relativeThreshold, absoluteThreshold, customChecker, lineSearch);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testCustomConstructorWithInvalidRelativeThreshold() {
        double relativeThreshold = 1e-7;
        double absoluteThreshold = 1e-5;
        ConvergenceChecker<PointValuePair> customChecker = mock(ConvergenceChecker.class);
        LineSearch lineSearch = mock(LineSearch.class);

        new PowellOptimizer(relativeThreshold, absoluteThreshold, customChecker, lineSearch);
    }

    @Test
    public void testOptimizeWithDefaultConvergenceChecker() {
        double[] startPoint = {1.0, 2.0};
        MultivariateFunction function = mock(MultivariateFunction.class);
        GoalType goalType = GoalType.MINIMIZE;

        PowellOptimizer optimizer = new PowellOptimizer();
        PointValuePair result = optimizer.optimize(startPoint, function, goalType);

        verify(function, times(2)).value(anyDoubleArray());
    }

    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        double[] startPoint = {1.0, 2.0};
        MultivariateFunction function = mock(MultivariateFunction.class);
        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> customChecker = mock(ConvergenceChecker.class);

        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.0001, customChecker, null);
        PointValuePair result = optimizer.optimize(startPoint, function, goalType);

        verify(customChecker, times(1)).converged(eq(0), any(PointValuePair.class), any(PointValuePair.class));
        verify(function, times(3)).value(anyDoubleArray());
    }

    @Test
    public void testOptimizeWithCustomLineSearch() {
        double[] startPoint = {1.0, 2.0};
        MultivariateFunction function = mock(MultivariateFunction.class);
        GoalType goalType = GoalType.MINIMIZE;
        LineSearch lineSearch = mock(LineSearch.class);

        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.0001, null, lineSearch);
        PointValuePair result = optimizer.optimize(startPoint, function, goalType);

        verify(lineSearch, times(1)).search(anyDoubleArray(), anyDoubleArray());
        verify(function, times(3)).value(anyDoubleArray());
    }

    @Test
    public void testOptimizeWithBothCustomConvergenceCheckerAndLineSearch() {
        double[] startPoint = {1.0, 2.0};
        MultivariateFunction function = mock(MultivariateFunction.class);
        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> customChecker = mock(ConvergenceChecker.class);
        LineSearch lineSearch = mock(LineSearch.class);

        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.0001, customChecker, lineSearch);
        PointValuePair result = optimizer.optimize(startPoint, function, goalType);

        verify(customChecker, times(1)).converged(eq(0), any(PointValuePair.class), any(PointValuePair.class));
        verify(lineSearch, times(1)).search(anyDoubleArray(), anyDoubleArray());
        verify(function, times(3)).value(anyDoubleArray());
    }

    @Test
    public void testOptimizeWithInvalidStartPoint() {
        MultivariateFunction function = mock(MultivariateFunction.class);
        GoalType goalType = GoalType.MINIMIZE;

        PowellOptimizer optimizer = new PowellOptimizer();
        try {
            optimizer.optimize(null, function, goalType);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }

        PowellOptimizer optimizer2 = new PowellOptimizer();
        try {
            optimizer2.optimize(new double[0], function, goalType);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}
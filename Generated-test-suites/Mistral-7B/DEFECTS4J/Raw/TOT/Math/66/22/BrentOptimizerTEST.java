package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeComparator;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testSimpleMinimization() {
        UnivariateRealFunction f = new MyFunction();
        double lowerBound = -10.0;
        double upperBound = 10.0;
        double initialGuess = 0.0;
        GoalType goalType = GoalType.MINIMIZE;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, lowerBound, upperBound, initialGuess);

        Assert.assertEquals("Minimization result is incorrect.", -2.0, result.getValue(), 1e-7);
        Assert.assertArrayEquals("Minimization point is incorrect.", new double[]{3.0}, result.getPoint(), 1e-7);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        UnivariateRealFunction f = new MyFunction();
        double lowerBound = -10.0;
        double upperBound = 10.0;
        double initialGuess = 0.0;
        GoalType goalType = GoalType.MINIMIZE;

        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaxIterations(0);
        optimizer.optimize(f, goalType, lowerBound, upperBound, initialGuess);
    }

    @Test
    public void testMaximization() {
        UnivariateRealFunction f = new MyFunction();
        double lowerBound = -10.0;
        double upperBound = 10.0;
        double initialGuess = 0.0;
        GoalType goalType = GoalType.MAXIMIZE;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, lowerBound, upperBound, initialGuess);

        Assert.assertEquals("Maximization result is incorrect.", 2.0, result.getValue(), 1e-7);
        Assert.assertArrayEquals("Maximization point is incorrect.", new double[]{7.0}, result.getPoint(), 1e-7);
    }

    @Test
    public void testLowerBoundEqualUpperBound() {
        UnivariateRealFunction f = new MyFunction();
        double lowerBound = 1.0;
        double upperBound = 1.0;
        double initialGuess = 0.0;
        GoalType goalType = GoalType.MINIMIZE;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, lowerBound, upperBound, initialGuess);

        Assert.assertEquals("Minimization result is incorrect.", Double.NaN, result.getValue(), 1e-7);
        Assert.assertEquals("Minimization point is incorrect.", Double.NaN, result.getPoint()[0], 1e-7);
    }

    @Test
    public void testFunctionWithSingularPointAtMinimum() {
        UnivariateRealFunction f = new MyFunctionWithSingularPoint();
        double lowerBound = -10.0;
        double upperBound = 10.0;
        double initialGuess = 0.0;
        GoalType goalType = GoalType.MINIMIZE;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair result = optimizer.optimize(f, goalType, lowerBound, upperBound, initialGuess);

        Assert.assertEquals("Minimization result is incorrect.", -1.0, result.getValue(), 1e-7);
        Assert.assertArrayEquals("Minimization point is incorrect.", new double[]{1.0}, result.getPoint(), 1e-7);
    }

    private static class MyFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x - 2 * x + 1;
        }
    }

    private static class MyFunctionWithSingularPoint implements UnivariateRealFunction {
        public double value(double x) {
            if (x >= 0) {
                return Math.pow(x, 2) - 1;
            } else {
                return Math.pow(x, 2) + 1;
            }
        }
    }

    @Test
    public void testGoalTypeComparator() {
        UnivariateRealFunction f = new MyFunction();
        double lowerBound = -10.0;
        double upperBound = 10.0;
        double initialGuess = 0.0;

        UnivariateOptimizer optimizer = new BrentOptimizer();
        PointValuePair resultMin = optimizer.optimize(f, GoalType.MINIMIZE, lowerBound, upperBound, initialGuess);
        PointValuePair resultMax = optimizer.optimize(f, GoalType.MAXIMIZE, lowerBound, upperBound, initialGuess);

        Assert.assertThat("Minimization result should have a lower value than maximization result.",
                new GoalTypeComparator(GoalType.MINIMIZE, GoalType.MAXIMIZE()).compare(resultMin.getValue(), resultMax.getValue()),
                is(lessThan(0)));
    }
}
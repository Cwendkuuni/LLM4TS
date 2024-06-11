package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.NonUnaryOptimizer;
import org.apache.commons.math3.optim.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.InitialGuessFactory;
import org.apache.commons.math3.optim.PointValuePairFactory;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMin() {
        UnivariateFunction function = new FunctionUnderTest();
        NonUnaryOptimizer optimizer = new BrentOptimizer(1e-4, 1e-4);

        InitialGuess initialGuess = new InitialGuessFactory().create(function, 0.5);
        PointValuePair result = optimizer.optimize(function, initialGuess, GoalType.MINIMIZE);
        PointValuePair expected = new PointValuePairFactory().create(0.25, 0.242140625);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testBrentOptimizerMinWithCustomChecker() {
        UnivariateFunction function = new FunctionUnderTest();
        NonUnaryOptimizer optimizer = new BrentOptimizer(1e-4, 1e-4);
        ConvergenceChecker<PointValuePair> checker = new MyConvergenceChecker(10);
        optimizer.setConvergenceChecker(checker);

        InitialGuess initialGuess = new InitialGuessFactory().create(function, 0.5);
        PointValuePair result = optimizer.optimize(function, initialGuess, GoalType.MINIMIZE);
        PointValuePair expected = new PointValuePairFactory().create(0.25, 0.242140625);
        Assert.assertEquals(expected, result);
        Assert.assertTrue(checker.isConverged());
    }

    @Test
    public void testBrentOptimizerMinNegativeFunction() {
        UnivariateFunction function = new NegativeFunctionUnderTest();
        NonUnaryOptimizer optimizer = new BrentOptimizer(1e-4, 1e-4);

        InitialGuess initialGuess = new InitialGuessFactory().create(function, 0.5);
        PointValuePair result = optimizer.optimize(function, initialGuess, GoalType.MINIMIZE);
        PointValuePair expected = new PointValuePairFactory().create(-0.5, -1);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerMinInvalidAbsThreshold() {
        UnivariateFunction function = new FunctionUnderTest();
        NonUnaryOptimizer optimizer = new BrentOptimizer(-1e-4, 1e-4);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerMinInvalidRelThreshold() {
        UnivariateFunction function = new FunctionUnderTest();
        NonUnaryOptimizer optimizer = new BrentOptimizer(1e-6, 1e-4);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerMinNullFunction() {
        NonUnaryOptimizer optimizer = new BrentOptimizer(1e-4, 1e-4);
        optimizer.optimize(null, null, GoalType.MINIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerMinNullInitialGuess() {
        UnivariateFunction function = new FunctionUnderTest();
        NonUnaryOptimizer optimizer = new BrentOptimizer(1e-4, 1e-4);
        optimizer.optimize(function, null, GoalType.MINIMIZE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerMinNullGoalType() {
        UnivariateFunction function = new FunctionUnderTest();
        NonUnaryOptimizer optimizer = new BrentOptimizer(1e-4, 1e-4);
        optimizer.optimize(function, new InitialGuessFactory().create(function, 0.5), null);
    }

    private static class FunctionUnderTest implements UnivariateFunction {

        @Override
        public double value(double x) {
            return 0.25 * x * x - 0.5 * x + 2;
        }

        @Override
        public double derivative(double x) {
            return 0.5 * x - 0.5;
        }
    }

    private static class NegativeFunctionUnderTest implements UnivariateFunction {

        @Override
        public double value(double x) {
            return -x * x;
        }

        @Override
        public double derivative(double x) {
            return -2 * x;
        }
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {

        private int iterations;

        public MyConvergenceChecker(int maxIterations) {
            this.iterations = maxIterations;
        }

        @Override
        public boolean isConverged() {
            return iterations >= this.iterations;
        }
    }
}
package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.junit.Assert;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest<FUNC extends UnivariateRealFunction> {

    /**
     * Test optimize method for minimization with 2 starts.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testMinimizeWithTwoStarts() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new MyFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new FibonacciSearchOptimizer(), 2, new RandomDataGenerator());

        PointValuePair result = optimizer.optimize(f, goal, min, max);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getValue(), -1.0, 1e-6);
        Assert.assertEquals(result.getPoint(), 1.0, 1e-6);
    }

    /**
     * Test optimize method for maximization with 3 starts.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testMaximizeWithThreeStarts() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new MyFunction();
        GoalType goal = GoalType.MAXIMIZE;
        double min = -5.0;
        double max = 5.0;

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new FibonacciSearchOptimizer(), 3, new RandomDataGenerator());

        PointValuePair result = optimizer.optimize(f, goal, min, max);
        Assert.assertNotNull(result);
        Assert.assertEquals(result.getValue(), 1.0, 1e-6);
        Assert.assertEquals(result.getPoint(), 4.0, 1e-6);
    }

    /**
     * Test optimize method for minimization with 1 start.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test(expected = ConvergenceException.class)
    public void testMinimizeWithOneStart() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new MyFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new FibonacciSearchOptimizer(), 1, new RandomDataGenerator());

        PointValuePair result = optimizer.optimize(f, goal, min, max);
        Assert.assertNull(result);
    }

    /**
     * Test optimize method for maximization with 4 starts.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testMaximizeWithFourStarts() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new MyFunction();
        GoalType goal = GoalType.MAXIMIZE;
        double min = -5.0;
        double max = 5.0;

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new FibonacciSearchOptimizer(), 4, new RandomDataGenerator());

        PointValuePair[] optima = optimizer.getOptima();
        Assert.assertArrayNotNull(optima);
        Assert.assertEquals(optima.length, 4);

        for (PointValuePair pair : optima) {
            if (pair == null) {
                continue;
            }
            Assert.assertNotNull(pair.getValue());
            Assert.assertEquals(pair.getValue(), 1.0, 1e-6);
            Assert.assertEquals(pair.getPoint(), Math.floor(Math.random() * (max - min + 1) + min), 1e-6);
        }
    }

    /**
     * Test convergence checker.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test
    public void testConvergenceChecker() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new MyFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new FibonacciSearchOptimizer(), 2, new RandomDataGenerator());
        ConvergenceChecker<PointValuePair> convergenceChecker =
                new ConvergenceChecker<PointValuePair>() {
                    private int counter = 0;

                    @Override
                    public boolean test(PointValuePair pointValuePair) {
                        if (counter++ > 0) {
                            return true;
                        }
                        return false;
                    }
                };

        optimizer.setConvergenceChecker(convergenceChecker);

        try {
            optimizer.optimize(f, goal, min, max);
            Assert.fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        PointValuePair[] optima = optimizer.getOptima();
        Assert.assertArrayNotNull(optima);
        Assert.assertEquals(optima.length, 2);
        Assert.assertNull(optima[0]);
        Assert.assertNull(optima[1]);
    }

    /**
     * Test out of bounds exception.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOutOfBounds() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new MyFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new FibonacciSearchOptimizer(), 2, new RandomDataGenerator());

        optimizer.optimize(f, goal, 10.0, 0.0);
    }

    /**
     * Test max evaluations exceeded exception.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testMaxEvaluationsExceeded() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new MyFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new FibonacciSearchOptimizer(), 2, new RandomDataGenerator());
        optimizer.setMaxEvaluations(1);

        optimizer.optimize(f, goal, min, max);
    }

    /**
     * Test no bracketing exception.
     * @throws FunctionEvaluationException
     * @throws ConvergenceException
     */
    @Test(expected = NoBracketingException.class)
    public void testNoBracketing() throws FunctionEvaluationException, ConvergenceException {
        UnivariateRealFunction f = new MyFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = -5.0;
        double max = -5.0;

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(new FibonacciSearchOptimizer(), 2, new RandomDataGenerator());

        optimizer.optimize(f, goal, min, max);
    }

    /**
     * Function to test.
     */
    public static class MyFunction implements UnivariateRealFunction {

        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2);
        }

        @Override
        public double derivative(double x) {
            return -2 * Math.sin(x) * Math.cos(x);
        }

        @Override
        public SimpleBounds getRange() {
            return new SimpleBounds(-10.0, 10.0);
        }

        @Override
        public InitialGuess getInitialGuess() {
            return new InitialGuess() {
                @Override
                public double getInitialGuessValue() {
                    return 0.0;
                }
            };
        }
    }
}
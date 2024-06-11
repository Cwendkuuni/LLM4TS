package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MaxEvaluationsExceededException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer<SimpleFunction> multiOptimizer;
    private MersenneTwister generator;

    @Before
    public void setUp() {
        optimizer = new FibonacciOptimizer();
        multiOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwister(System.nanoTime()));
    }

    @After
    public void tearDown() {
        multiOptimizer = null;
        optimizer = null;
        generator = null;
    }

    @Test
    public void testSimpleMinimum() throws Exception {
        SimpleFunction f = new SimpleFunction(-5.0);
        GoalType goal = GoalType.MINIMIZE;
        PointValuePair pv = multiOptimizer.optimize(f, goal, -10.0, 10.0);
        Assert.assertNotNull(pv);
        Assert.assertEquals(-3.14159265359, pv.getPoint(), 0.0001);
    }

    @Test
    public void testSimpleMaximum() throws Exception {
        SimpleFunction f = new SimpleFunction(5.0);
        GoalType goal = GoalType.MAXIMIZE;
        PointValuePair pv = multiOptimizer.optimize(f, goal, -10.0, 10.0);
        Assert.assertNotNull(pv);
        Assert.assertEquals(3.14159265359, pv.getPoint(), 0.0001);
    }

    @Test(expected = ConvergenceException.class)
    public void testNoConvergence() throws Exception {
        SimpleFunction f = new SimpleFunction(0.0);
        GoalType goal = GoalType.MINIMIZE;
        multiOptimizer.optimize(f, goal, -10.0, 10.0);
    }

    @Test
    public void testMaxEvaluations() throws Exception {
        SimpleFunction f = new SimpleFunction(Math.PI / 2.0);
        GoalType goal = GoalType.MINIMIZE;
        MultiStartUnivariateRealOptimizer<SimpleFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwister(System.nanoTime()));
        optimizer.setMaxEvaluations(10);
        try {
            optimizer.optimize(f, goal, -10.0, 10.0);
            Assert.fail();
        } catch (MaxEvaluationsExceededException e) {
            // expected
        }
    }

    @Test
    public void testCustomInitialGuess() throws Exception {
        SimpleFunction f = new SimpleFunction(0.0);
        GoalType goal = GoalType.MINIMIZE;
        MultiStartUnivariateRealOptimizer<SimpleFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwister(System.nanoTime()));
        double[] guesses = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0, 10.0};
        for (double guess : guesses) {
            optimizer.setInitialGuess(new InitialGuess(guess));
            PointValuePair pv = optimizer.optimize(f, goal, -10.0, 10.0);
            Assert.assertNotNull(pv);
            Assert.assertNotEquals(guess, pv.getPoint(), 0.0001);
        }
    }

    @Test
    public void testCustomConvergenceChecker() throws Exception {
        SimpleFunction f = new SimpleFunction(0.0);
        GoalType goal = GoalType.MINIMIZE;
        MultiStartUnivariateRealOptimizer<SimpleFunction> optimizer =
                new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwister(System.nanoTime()));
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            @Override
            public boolean isConverged(PointValuePair pvp) {
                return pvp.getValue() < 0.0;
            }
        };
        optimizer.setConvergenceChecker(checker);
        PointValuePair pv = optimizer.optimize(f, goal, -10.0, 10.0);
        Assert.assertNotNull(pv);
        Assert.assertLess(pv.getValue(), 0.0);
    }

    /**
     * A simple function to test the optimizer.
     */
    private static class SimpleFunction implements UnivariateRealFunction {
        private final double goal;

        public SimpleFunction(double goal) {
            this.goal = goal;
        }

        @Override
        public double value(double x) throws FunctionEvaluationException {
            return Math.pow(Math.sin(x), 2.0) - goal;
        }

        @Override
        public double derivative(double x) throws FunctionEvaluationException {
            return -2.0 * Math.sin(x) * Math.cos(x);
        }
    }

    /**
     * A simple optimizer to test the multi-start optimizer.
     */
    private static class FibonacciOptimizer implements UnivariateOptimizer {
        @Override
        public PointValuePair optimize(UnivariateRealFunction f, GoalType goal, double x1, double x2)
                throws FunctionEvaluationException, ConvergenceException, InterruptedException {
            MaxEval maxEval = new MaxEval(5);
            return optimize(f, goal, x1, x2, maxEval);
        }

        @Override
        public PointValuePair optimize(UnivariateRealFunction f, GoalType goal, double x, double step, double stepMagnitude)
                throws FunctionEvaluationException, ConvergenceException, InterruptedException {
            MaxEval maxEval = new MaxEval(1);
            return optimize(f, goal, x, step, stepMagnitude, maxEval);
        }

        @Override
        public PointValuePair optimize(UnivariateRealFunction f, GoalType goal, double x, double step, double stepMagnitude,
                                       MaxEval maxEval) throws FunctionEvaluationException, ConvergenceException,
                InterruptedException {
            double value = f.value(x);
            int evaluations = 1 + maxEval.evaluationsDone();
            if (evaluations > maxEval.getMaxEvaluations()) {
                throw new MaxEvaluationsExceededException(evaluations, maxEval.getMaxEvaluations());
            }
            if (goal.isMinimizationGoal() && value <= 0.0) {
                return new PointValuePair(x, value);
            }
            double stepSign = (goal.isMinimizationGoal() ? -1.0 : 1.0);
            double newX = x + stepSign * stepMagnitude;
            double newValue = f.value(newX);
            evaluations++;
            if (goal.isMinimizationGoal() && newValue < value) {
                stepMagnitude *= 0.5;
                return optimize(f, goal, newX, step, stepMagnitude, maxEval);
            }
            throw new ConvergenceException("Failed to converge: " + value);
        }
    }
}
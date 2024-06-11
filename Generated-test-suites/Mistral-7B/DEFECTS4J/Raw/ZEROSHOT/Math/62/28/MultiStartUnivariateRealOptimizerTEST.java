package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeMassFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.FibonacciSearchOptimizer;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test function for optimization.
     */
    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(Math.sin(x), 2) + Math.pow(Math.cos(x), 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * Math.sin(x) * Math.cos(x) - 2 * Math.cos(x) * Math.sin(x);
        }

        @Override
        public double derivative(double x, int derivativeOrder) {
            if (derivativeOrder > 1) {
                throw new UnsupportedOperationException();
            }
            return this.derivative(x);
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(BaseUnivariateRealOptimizer, int, RandomGenerator)}.
     */
    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        final UnivariateOptimizer optimizer = new FibonacciSearchOptimizer();
        final MultiStartUnivariateRealOptimizer<TestFunction> optimizerMS =
                new MultiStartUnivariateRealOptimizer<TestFunction>(
                        (BaseUnivariateRealOptimizer<TestFunction>) optimizer, 5, new org.apache.commons.math3.random.MersenneTwisterFastRandom());

        Assert.assertNotNull(optimizerMS);
        Assert.assertNull(optimizerMS.getOptima());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     */
    @Test
    public void testGetOptima() throws FunctionEvaluationException, MathIllegalStateException {
        final UnivariateOptimizer optimizer = new FibonacciSearchOptimizer();
        final MultiStartUnivariateRealOptimizer<TestFunction> optimizerMS =
                new MultiStartUnivariateRealOptimizer<TestFunction>(
                        (BaseUnivariateRealOptimizer<TestFunction>) optimizer, 5, new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        final TestFunction f = new TestFunction();
        optimizerMS.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        final UnivariateRealPointValuePair[] optima = optimizerMS.getOptima();
        Assert.assertNotNull(optima);
        Assert.assertEquals(5, optima.length);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimize() throws FunctionEvaluationException, MathIllegalStateException {
        final UnivariateOptimizer optimizer = new FibonacciSearchOptimizer();
        final MultiStartUnivariateRealOptimizer<TestFunction> optimizerMS =
                new MultiStartUnivariateRealOptimizer<TestFunction>(
                        (BaseUnivariateRealOptimizer<TestFunction>) optimizer, 5, new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        final TestFunction f = new TestFunction();
        final PointValuePair best = optimizerMS.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        Assert.assertNotNull(best);
        Assert.assertNotNull(best.getPoint());
        Assert.assertNotNull(best.getValue());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testOptimizeWithInitialGuess()
            throws FunctionEvaluationException, MathIllegalStateException, ConvergenceException {
        final UnivariateOptimizer optimizer = new FibonacciSearchOptimizer();
        final MultiStartUnivariateRealOptimizer<TestFunction> optimizerMS =
                new MultiStartUnivariateRealOptimizer<TestFunction>(
                        (BaseUnivariateRealOptimizer<TestFunction>) optimizer, 5, new org.apache.commons.math3.random.MersenneTwisterFastRandom());
        optimizerMS.setMaxEvaluations(100);
        final TestFunction f = new TestFunction();
        final double initialGuess = 1.2;
        final GoalType goal = GoalType.MINIMIZE;
        final SimpleBounds bounds = new SimpleBounds(-10.0, 10.0);
        final InitialGuess initial = new InitialGuess(initialGuess);
        final ConvergenceChecker<PointValuePair> convergenceChecker =
                new GoalTypeMassFunction(goal, new MassFunction(1.0));
        optimizerMS.setConvergenceChecker(convergenceChecker);
        final PointValuePair best = optimizerMS.optimize(f, goal, bounds, initial);
        Assert.assertNotNull(best);
        Assert.assertNotNull(best.getPoint());
        Assert.assertNotNull(best.getValue());
        Assert.assertEquals(goal, best.getGoalType());
    }
}
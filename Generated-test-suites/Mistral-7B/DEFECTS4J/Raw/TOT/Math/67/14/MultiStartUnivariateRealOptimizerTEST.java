package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.goaltypes.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeConvergenceChecker;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateRealOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartOptimizerMinSimple() {
        UnivariateRealFunction f = new SimpleFunction(-0.5 * x * x + 2 * x + 1);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchRealOptimizer(), 10, new MersenneTwisterFastRandom());

        PointValuePair result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);

        assertEquals("The optimizer should have found the minimum x = -1.0 and f(x) = 1.5",
                -1.0, result.getPoint(), 1e-6);
        assertEquals("The optimizer should have found the minimum value f(x) = 1.5",
                1.5, result.getValue(), 1e-6);
    }

    @Test
    public void testMultiStartOptimizerMaxSimple() {
        UnivariateRealFunction f = new SimpleFunction(Math.exp(x));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new GoldenSectionRealOptimizer(), 10, new MersenneTwisterFastRandom());

        PointValuePair result = optimizer.optimize(f, GoalType.MAXIMIZE, -10, 10);

        assertEquals("The optimizer should have found the maximum x = 0.0 and f(x) = e",
                1.0, Math.E, 1e-5);
        assertEquals("The optimizer should have found the maximum value f(x) = e",
                Math.E, result.getValue(), 1e-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMultiStartOptimizerWrongNumberOfStarts() {
        UnivariateRealFunction f = new SimpleFunction(Math.exp(x));
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new GoldenSectionRealOptimizer(), 0, new MersenneTwisterFastRandom());
        optimizer.optimize(f, GoalType.MAXIMIZE, -10, 10);
    }

    @Test(expected = ConvergenceException.class)
    public void testMultiStartOptimizerBadFunction() {
        UnivariateRealFunction f = new SimpleFunction(x -> 1.0 / x);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchRealOptimizer(), 10, new MersenneTwisterFastRandom());
        optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testMultiStartOptimizerEvaluationFailure() {
        UnivariateRealFunction f = new SimpleFunction(x -> {
            throw new ArithmeticException();
        });
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new FibonacciSearchRealOptimizer(), 10, new MersenneTwisterFastRandom());
        optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
    }

    @Test
    public void testMultiStartOptimizerFewConvergingStarts() {
        UnivariateRealFunction f = new SimpleFunction(x -> Math.pow(Math.sin(x), 2) + 2 * Math.cos(x) - 1);
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentRealOptimizer(), 5, new MersenneTwisterFastRandom());

        PointValuePair[] results = new PointValuePair[optimizer.getStarts()];
        for (int i = 0; i < optimizer.getStarts(); i++) {
            results[i] = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
        }

        GoalTypeConvergenceChecker checker = new GoalTypeConvergenceChecker(
                1e-6, MaxEval.REACHED_MAX_EVALUATIONS, results.length);
        UnivariateRealOptimizer singleOptimizer = new BrentRealOptimizer();
        PointValuePair singleResult = singleOptimizer.optimize(f, GoalType.MINIMIZE, -10, 10, checker);

        assertArrayEquals("The optimizer should have found the minimum in most starts",
                results, singleResult.getPointArray(), 1e-6);
    }

}

class SimpleFunction implements UnivariateFunction {

    private final UnivariateFunction func;

    public SimpleFunction(UnivariateFunction func) {
        this.func = func;
    }

    @Override
    public double value(double x) {
        return func.value(x);
    }

    @Override
    public double derivative(double x) {
        return func.derivative(x);
    }

}
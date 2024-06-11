package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testBasicFunctionality() throws Exception {
        UnivariateRealFunction f = new MyFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        MultiStartUnivariateRealOptimizer<MyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FixedPointUnivariateOptimizer(new MyFunction(), new SimpleBounds(min, max)), 5, new RandomGenerator()) {
            @Override
            public void setMaxEvaluations(int maxEvaluations) {
                super.setMaxEvaluations(50);
            }
        };

        PointValuePair result = optimizer.optimize(f, goal, min, max);
        assertEquals(0.362485923106258, result.getPoint().doubleValue(), 0.001);
        assertEquals(-0.362485923106258, result.getValue(), 0.001);
        assertEquals(5, optimizer.getEvaluations());
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() throws Exception {
        UnivariateRealFunction f = new MyExceptionFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        MultiStartUnivariateRealOptimizer<MyExceptionFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FixedPointUnivariateOptimizer(new MyExceptionFunction(), new SimpleBounds(min, max)), 5, new RandomGenerator());

        optimizer.optimize(f, goal, min, max);
    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() throws Exception {
        UnivariateRealFunction f = new MyNonConvergentFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        MultiStartUnivariateRealOptimizer<MyNonConvergentFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FixedPointUnivariateOptimizer(new MyNonConvergentFunction(), new SimpleBounds(min, max)), 5, new RandomGenerator());

        optimizer.optimize(f, goal, min, max);
    }

    @Test
    public void testGetOptima() throws Exception {
        UnivariateRealFunction f = new MyFunction();
        GoalType goal = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 10.0;

        MultiStartUnivariateRealOptimizer<MyFunction> optimizer = new MultiStartUnivariateRealOptimizer<>(
                new FixedPointUnivariateOptimizer(new MyFunction(), new SimpleBounds(min, max)), 5, new RandomGenerator());

        optimizer.optimize(f, goal, min, max);

        UnivariateRealPointValuePair[] optima = optimizer.getOptima();
        assertArrayEquals(new UnivariateRealPointValuePair[] {
                new UnivariateRealPointValuePair(0.362485923106258, -0.362485923106258),
                new UnivariateRealPointValuePair(1.234567890123456, -1.234567890123456),
                new UnivariateRealPointValuePair(5.555555555555555, -5.555555555555555),
                new UnivariateRealPointValuePair(7.654321098765432, -7.654321098765432),
                new UnivariateRealPointValuePair(null, null)
        }, optima, 0.001);
    }

    static class MyFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) - 2 * x + 1;
        }

        @Override
        public double derivative(double x) {
            return 2 * x - 2;
        }
    }

    static class MyExceptionFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            if (x < 0) {
                throw new FunctionEvaluationException("Negative x value");
            }
            return Math.pow(x, 2);
        }

        @Override
        public double derivative(double x) {
            return 2 * x;
        }
    }

    static class MyNonConvergentFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) {
            if (Math.abs(x - 5.0) < 0.0001) {
                throw new ConvergenceException("Function did not converge");
            }
            return Math.sin(x);
        }

        @Override
        public double derivative(double x) {
            return Math.cos(x);
        }
    }
}
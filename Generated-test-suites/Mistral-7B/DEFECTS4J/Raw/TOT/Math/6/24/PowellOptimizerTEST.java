package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.DirectSearchOptimizer;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MultivariateFunction;
import org.apache.commons.math3.optimization.SimpleUnivariateValueChecker;
import org.apache.commons.math3.optimization.UnivariateFunction;
import org.apache.commons.math3.special.ContinuousFunction;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Test class for {@link PowellOptimizer}.
 */
public class PowellOptimizerTest {

    /**
     * A multivariate function for testing.
     */
    static class TestFunction implements MultivariateFunction {
        double[] x;

        @Override
        public double value(double[] x) {
            this.x = x;
            return -Math.log(Math.sin(Math.PI * x[0]) * Math.exp(-x[1] * x[1]) *
                    (1 - x[0] * x[0]) * (1 - x[0]));
        }

        @Override
        public double[] gradient(double[] x) {
            this.x = x;
            double[] result = new double[x.length];
            result[0] = Math.PI * Math.cos(Math.PI * x[0]) *
                    (Math.exp(-x[1] * x[1]) * (1 - x[0] * x[0]) -
                            Math.sin(Math.PI * x[0]) * 2 * x[0] * Math.exp(-x[1] * x[1])) /
                    Math.pow(Math.sin(Math.PI * x[0]), 2);
            result[1] = -2 * x[1] * Math.exp(-x[1] * x[1]) *
                    (Math.sin(Math.PI * x[0]) * (1 - x[0] * x[0]) -
                            Math.log(Math.sin(Math.PI * x[0])) *
                            Math.exp(-x[1] * x[1]));
            return result;
        }
    }

    /**
     * Test the default constructor.
     */
    @Test
    public void testDefaultConstructor() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        assertNotNull(optimizer);
    }

    /**
     * Test the constructor with custom convergence checker.
     */
    @Test
    public void testCustomConvergenceChecker() {
        UnivariateFunction customChecker = new ContinuousFunction() {
            @Override
            public double value(double x) {
                return Math.abs(Math.sin(x) - 0.5);
            }
        };
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001,
                new SimpleUnivariateValueChecker(0.001, 0.001), customChecker);
        assertNotNull(optimizer);
    }

    /**
     * Test the optimization with a simple function.
     */
    @Test
    public void testOptimizationSimple() {
        MultivariateFunction f = new TestFunction();
        double[] startPoint = {1.5, 0.5};
        double[] solution = {-1.0, -2.0};
        GoalType goal = GoalType.MINIMIZE;

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        PointValuePair result = optimizer.optimize(f, goal, startPoint);
        assertEquals(goal, result.getGoalType());
        assertArrayEquals(solution, result.getPoint(), 0.0001);
    }

    /**
     * Test the optimization with a more complex function.
     */
    @Test
    public void testOptimizationComplex() {
        MultivariateFunction f = new TestFunction();
        double[] startPoint = {-3.0, 2.0};
        double[] solution = {1.0, 1.0};
        GoalType goal = GoalType.MINIMIZE;

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        PointValuePair result = optimizer.optimize(f, goal, startPoint);
        assertEquals(goal, result.getGoalType());
        assertArrayEquals(solution, result.getPoint(), 0.0001);
    }

    /**
     * Test the optimization with an incorrect starting point.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizationInvalidStartPoint() {
        MultivariateFunction f = new TestFunction();
        double[] startPoint = {0.5, 0.5};
        double[] solution = {-1.0, -2.0};
        GoalType goal = GoalType.MINIMIZE;

        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        optimizer.optimize(f, goal, startPoint);
    }
}
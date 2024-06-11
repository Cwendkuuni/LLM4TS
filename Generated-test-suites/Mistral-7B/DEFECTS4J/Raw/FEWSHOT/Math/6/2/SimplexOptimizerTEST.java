package org.apache.commons.cli.optimization.direct;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleValueChecker;
import org.apache.commons.math3.optim.PointValues;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.NonSmoothedPoint;
import org.apache.commons.math3.optim.SimpleSimplex;
import org.apache.commons.math3.optim.PointValuePairComparator;
import org.junit.Test;

public class SimplexOptimizerTest {
    @Test
    public void testOptimizeSimpleFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();

        MultivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0}, 1);
        double[] startPoint = {2.0, 2.0};
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optimizationData = new OptimizationData[]{
                new InitialGuess(startPoint),
                new SimpleSimplex(4, 5)
        };

        PointValuePair solution = optimizer.optimize(100, function, goalType, optimizationData);
        assertTrue(solution.getValue() < 0);
        assertArrayEquals(new double[]{0.0, 0.0}, solution.getPoint(), 0.001);
    }

    @Test(expected = NullArgumentException.class)
    public void testOptimizeNullFunction() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = null;
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optimizationData = new OptimizationData[]{
                new InitialGuess(new double[]{0.0, 0.0}),
                new SimpleSimplex(2, 3)
        };

        optimizer.optimize(100, function, goalType, optimizationData);
    }

    @Test
    public void testOptimizeInvalidGoalType() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new PolynomialFunction(new double[]{0.0, 0.0}, 1);
        GoalType goalType = GoalType.MAXIMIZE; // Should be MINIMIZE
        OptimizationData[] optimizationData = new OptimizationData[]{
                new InitialGuess(new double[]{0.0, 0.0}),
                new SimpleSimplex(2, 3)
        };

        PointValuePair solution = optimizer.optimize(100, function, goalType, optimizationData);
        assertNull(solution);
    }

    @Test
    public void testOptimizeNullOptimizationData() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optimizationData = new OptimizationData[0];

        PointValuePair solution = optimizer.optimize(100, function, goalType, optimizationData);
        assertNull(solution);
    }

    @Test
    public void testOptimizeNullInitialGuess() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optimizationData = new OptimizationData[]{
                null,
                new SimpleSimplex(2, 3)
        };

        PointValuePair solution = optimizer.optimize(100, function, goalType, optimizationData);
        assertNull(solution);
    }

    @Test
    public void testOptimizeNullSimplex() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new PolynomialFunction(new double[]{-1.0, 2.0}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optimizationData = new OptimizationData[]{
                new InitialGuess(new double[]{0.0, 0.0}),
                null
        };

        PointValuePair solution = optimizer.optimize(100, function, goalType, optimizationData);
        assertNull(solution);
    }

    @Test
    public void testOptimizeInvalidSimplexSize() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new PolynomialFunction(new double[]{0.0, 0.0}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optimizationData = new OptimizationData[]{
                new InitialGuess(new double[]{0.0, 0.0}),
                new SimpleSimplex(1, 3)
        };

        PointValuePair solution = optimizer.optimize(100, function, goalType, optimizationData);
        assertNull(solution);
    }

    @Test
    public void testOptimizeValidParameters() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new PolynomialFunction(new double[]{0.0, 0.0}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optimizationData = new OptimizationData[]{
                new InitialGuess(new double[]{0.0, 0.0}),
                new SimpleSimplex(2, 3)
        };

        PointValuePair solution = optimizer.optimize(100, function, goalType, optimizationData);
        assertArrayEquals(new double[]{0.0, 0.0}, solution.getPoint(), 0.001);
        assertTrue(solution.getValue() < 0);
    }

    @Test
    public void testOptimizeEmptyFunctionEvaluations() {
        SimplexOptimizer optimizer = new SimplexOptimizer();
        MultivariateFunction function = new PolynomialFunction(new double[]{0.0, 0.0}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optimizationData = new OptimizationData[]{
                new InitialGuess(new double[]{0.0, 0.0}),
                new SimpleSimplex(2, 3)
        };

        PointValuePair solution = optimizer.optimize(0, function, goalType, optimizationData);
        assertNull(solution);
    }

    @Test
    public void testOptimizeConvergenceChecker() {
        SimplexOptimizer optimizer = new SimplexOptimizer(new SimpleValueChecker(1e-3, 1e-5));
        MultivariateFunction function = new PolynomialFunction(new double[]{0.0, 0.0}, 1);
        GoalType goalType = GoalType.MINIMIZE;
        OptimizationData[] optimizationData = new OptimizationData[]{
                new InitialGuess(new double[]{0.0, 0.0}),
                new SimpleSimplex(2, 3)
        };

        PointValuePair solution = optimizer.optimize(100, function, goalType, optimizationData);
        assertArrayEquals(new double[]{0.0, 0.0}, solution.getPoint(), 0.001);
        assertTrue(solution.getValue() < 0);
    }
}
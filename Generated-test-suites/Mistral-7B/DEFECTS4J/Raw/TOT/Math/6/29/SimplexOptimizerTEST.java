package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.InitialGuess;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Test class for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /**
     * Test simple optimization with a simple function and default parameters.
     */
    @Test
    public void testSimpleOptimization() {
        // Set up a simple optimization problem.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 1e-6);
        MaxEval maxEval = new MaxEval(100);
        AbstractSimplex simplex = new SimpleSimplex(2);
        InitialGuess startPoint = new InitialGuess(new double[] {1.0, 1.0});

        // Run the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(maxEval, f, goalType, new OptimizationData[] {startPoint});

        // Check the result.
        double[] expected = new double[] {0.0, 0.0};
        assertArrayEquals(expected, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a non-convex function and default parameters.
     */
    @Test
    public void testNonConvexOptimization() {
        // Set up a non-convex optimization problem.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                if (x[0] > 0.5) {
                    return Math.pow(Math.sin(x[0]) * x[1], 2);
                }
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-6, 1e-6);
        MaxEval maxEval = new MaxEval(100);
        AbstractSimplex simplex = new SimpleSimplex(2);
        InitialGuess startPoint = new InitialGuess(new double[] {0.5, 1.0});

        // Run the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        optimizer.setSimplex(simplex);
        PointValuePair result = optimizer.optimize(maxEval, f, goalType, new OptimizationData[] {startPoint});

        // Check the result.
        double[] expected = new double[] {0.5, Math.PI / 2};
        assertArrayEquals(expected, result.getPoint(), 1e-6);
        assertEquals(0.25 * Math.pow(Math.sin(Math.PI / 2), 2), result.getValue(), 1e-6);
    }

    /**
     * Test optimization with a simple function and custom parameters.
     */
    @Test
    public void testSimpleOptimizationWithCustomParameters() {
        // Set up a simple optimization problem.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-4, 1e-4);
        MaxEval maxEval = new MaxEval(50);
        AbstractSimplex simplex = new SimpleSimplex(2);
        InitialGuess startPoint = new InitialGuess(new double[] {2.0, 2.0});

        // Set up a custom optimizer.
        SimplexOptimizer optimizer
            = new SimplexOptimizer(checker, 0.01, 0.01);

        // Run the optimization.
        PointValuePair result
            = optimizer.optimize(maxEval, f, goalType, new OptimizationData[] {startPoint});

        // Check the result.
        double[] expected = new double[] {0.0, 0.0};
        assertArrayEquals(expected, result.getPoint(), 1e-4);
        assertEquals(0.0, result.getValue(), 1e-4);
    }

    /**
     * Test optimization with a non-convex function and custom parameters.
     */
    @Test
    public void testNonConvexOptimizationWithCustomParameters() {
        // Set up a non-convex optimization problem.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                if (x[0] > 0.5) {
                    return Math.pow(Math.sin(x[0]) * x[1], 2);
                }
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1e-4, 1e-4);
        MaxEval maxEval = new MaxEval(50);
        AbstractSimplex simplex = new SimpleSimplex(2);
        InitialGuess startPoint = new InitialGuess(new double[] {0.5, 1.0});

        // Set up a custom optimizer.
        SimplexOptimizer optimizer
            = new SimplexOptimizer(checker, 0.01, 0.01);

        // Run the optimization.
        PointValuePair result
            = optimizer.optimize(maxEval, f, goalType, new OptimizationData[] {startPoint});

        // Check the result.
        double[] expected = new double[] {0.5, Math.PI / 2};
        assertArrayEquals(expected, result.getPoint(), 1e-4);
        assertEquals(0.25 * Math.pow(Math.sin(Math.PI / 2), 2), result.getValue(), 1e-4);
    }
}
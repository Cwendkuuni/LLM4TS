package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.SimpleValueChecker;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.OptimizationData;
import org.apache.commons.math3.optimization.AbstractSimplex;
import org.apache.commons.math3.optimization.SimpleSimplex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link SimplexOptimizer}.
 */
public class SimplexOptimizerTest {

    /**
     * Test optimization of a simple function.
     */
    @Test
    public void testOptimization() {
        // Prepare test data.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                return x[0] * x[0] + x[1] * x[1];
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        AbstractSimplex simplex = new SimpleSimplex(5);
        OptimizationData optData = new OptimizationData();
        optData.setParameter("InitialGuess", new double[] { 2.0, 2.0 });
        optData.setParameter("Simplex", simplex);

        // Run the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        PointValuePair result = optimizer.optimize(1000, f, goalType, optData);

        // Check results.
        assertNotNull(result);
        assertEquals(2.0, result.getPoint()[0], 1.0e-6);
        assertEquals(2.0, result.getPoint()[1], 1.0e-6);
        assertEquals(2.0, result.getValue(), 1.0e-6);
    }

    /**
     * Test optimization with a non-converging function.
     */
    @Test(expected = org.apache.commons.math3.exception.MaxEvalExceededException.class)
    public void testMaxEvalExceeded() {
        // Prepare test data.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) {
                throw new RuntimeException("Not implemented");
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        AbstractSimplex simplex = new SimpleSimplex(5);
        OptimizationData optData = new OptimizationData();
        optData.setParameter("InitialGuess", new double[] { 2.0, 2.0 });
        optData.setParameter("Simplex", simplex);

        // Run the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        optimizer.optimize(1, f, goalType, optData);
    }

    /**
     * Test optimization with a null objective function.
     */
    @Test(expected = NullPointerException.class)
    public void testNullObjectiveFunction() {
        // Prepare test data.
        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        AbstractSimplex simplex = new SimpleSimplex(5);
        OptimizationData optData = new OptimizationData();

        // Run the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        optimizer.optimize(1, null, goalType, optData);
    }

    /**
     * Test optimization with a null convergence checker.
     */
    @Test
    public void testNullConvergenceChecker() {
        // Prepare test data.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) { return 0.0; }
        };
        GoalType goalType = GoalType.MINIMIZE;
        AbstractSimplex simplex = new SimpleSimplex(5);
        OptimizationData optData = new OptimizationData();
        optData.setParameter("InitialGuess", new double[] { 2.0, 2.0 });
        optData.setParameter("Simplex", simplex);

        // Run the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(null);
        try {
            optimizer.optimize(1, f, goalType, optData);
            fail("Expected exception not thrown.");
        } catch (NullArgumentException e) {
            // Expected.
        }
    }

    /**
     * Test optimization with a null optimization data.
     */
    @Test
    public void testNullOptimizationData() {
        // Prepare test data.
        MultivariateFunction f = new MultivariateFunction() {
            public double value(double[] x) { return 0.0; }
        };
        GoalType goalType = GoalType.MINIMIZE;
        ConvergenceChecker<PointValuePair> checker = new SimpleValueChecker(1.0e-6, 1.0e-6);
        AbstractSimplex simplex = new SimpleSimplex(5);

        // Run the optimization.
        SimplexOptimizer optimizer = new SimplexOptimizer(checker);
        try {
            optimizer.optimize(1, f, goalType, null);
            fail("Expected exception not thrown.");
        } catch (NullArgumentException e) {
            // Expected.
        }
    }
}
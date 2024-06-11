package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.BrentOptimizer;
import org.apache.commons.math3.random.MersenneTwister;

public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test the optimization of a simple quadratic function
     * @throws Exception in case of errors
     */
    @org.junit.Test
    public void testQuadraticFunctionOptimization() throws Exception {
        // Build the function to optimize
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 2) + 2;
            }
        };

        // Define the optimization goal
        GoalType goalType = GoalType.MINIMIZE;

        // Define the optimization bounds
        double min = -5.0;
        double max = 5.0;

        // Initialize the optimizer
        RandomGenerator generator = new MersenneTwister();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 3, generator);

        // Perform the optimization
        double result = optimizer.optimize(function, goalType, min, max);

        // Verify the result
        assertEquals(1.0, result, 1e-5);

        // Verify the optima found
        double[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        assertEquals(1.0, optima[0], 1e-5);
        assertEquals(1.0, optima[1], 1e-5);
        assertEquals(1.0, optima[2], 1e-5);

        // Verify the optima values
        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(3, optimaValues.length);
        assertEquals(1.0, optimaValues[0], 1e-5);
        assertEquals(1.0, optimaValues[1], 1e-5);
        assertEquals(1.0, optimaValues[2], 1e-5);

        // Verify the number of iterations and evaluations performed
        assertEquals(3 * MaxEval.DEFAULT_MAX_EVALUATIONS, optimizer.getIterationCount());
        assertEquals(3 * MaxEval.DEFAULT_MAX_EVALUATIONS, optimizer.getEvaluations());

    }

    /**
     * Test the optimization of a function with no solutions
     * @throws Exception in case of errors
     */
    @org.junit.Test
    public void testNoSolutionFunctionOptimization() throws Exception {
        // Build the function to optimize
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.sin(Math.PI) * Math.cos(Math.PI) * x + 1;
            }
        };

        // Define the optimization goal
        GoalType goalType = GoalType.MINIMIZE;

        // Define the optimization bounds
        double min = -5.0;
        double max = 5.0;

        // Initialize the optimizer
        RandomGenerator generator = new MersenneTwister();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 3, generator);

        // Perform the optimization
        assertThrows(ConvergenceException.class, () -> {
            optimizer.optimize(function, goalType, min, max);
        });

        // Verify the optima found
        double[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        for (int i = 0; i < optima.length; i++) {
            assertEquals(Double.NaN, optima[i]);
        }

        // Verify the optima values
        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(3, optimaValues.length);
        for (int i = 0; i < optimaValues.length; i++) {
            assertEquals(Double.NaN, optimaValues[i]);
        }

    }

    /**
     * Test the optimization of a function with a single solution
     * @throws Exception in case of errors
     */
    @org.junit.Test
    public void testSingleSolutionFunctionOptimization() throws Exception {
        // Build the function to optimize
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 1.0, 2);
            }
        };

        // Define the optimization goal
        GoalType goalType = GoalType.MINIMIZE;

        // Define the optimization bounds
        double min = -5.0;
        double max = 5.0;

        // Initialize the optimizer
        RandomGenerator generator = new MersenneTwister();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 3, generator);

        // Perform the optimization
        double result = optimizer.optimize(function, goalType, min, max);

        // Verify the result
        assertEquals(1.0, result, 1e-5);

        // Verify the optima found
        double[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        assertEquals(1.0, optima[0], 1e-5);
        assertEquals(1.0, optima[1], 1e-5);
        assertEquals(1.0, optima[2], 1e-5);

        // Verify the optima values
        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(3, optimaValues.length);
        assertEquals(0.0, optimaValues[0], 1e-5);
        assertEquals(0.0, optimaValues[1], 1e-5);
        assertEquals(0.0, optimaValues[2], 1e-5);

    }

    /**
     * Test the optimization of a function with multiple solutions
     * @throws Exception in case of errors
     */
    @org.junit.Test
    public void testMultipleSolutionFunctionOptimization() throws Exception {
        // Build the function to optimize
        UnivariateRealFunction function = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x - 2.0, 2) - Math.pow(x - 3.0, 2);
            }
        };

        // Define the optimization goal
        GoalType goalType = GoalType.MINIMIZE;

        // Define the optimization bounds
        double min = -5.0;
        double max = 5.0;

        // Initialize the optimizer
        RandomGenerator generator = new MersenneTwister();
        MultiStartUnivariateRealOptimizer optimizer = new MultiStartUnivariateRealOptimizer(
                new BrentOptimizer(), 3, generator);

        // Perform the optimization
        double result = optimizer.optimize(function, goalType, min, max);

        // Verify the result
        assertEquals(2.0, result, 1e-5);

        // Verify the optima found
        double[] optima = optimizer.getOptima();
        assertEquals(3, optima.length);
        assertEquals(2.0, optima[0], 1e-5);
        assertEquals(2.0, optima[1], 1e-5);
        assertEquals(2.0, optima[2], 1e-5);

        // Verify the optima values
        double[] optimaValues = optimizer.getOptimaValues();
        assertEquals(3, optimaValues.length);
        assertEquals(-3.0, optimaValues[0], 1e-5);
        assertEquals(0.0, optimaValues[1], 1e-5);
        assertEquals(-3.0, optimaValues[2], 1e-5);

    }

}
package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.MaxEval;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.UnivariateRealFunction;
import org.apache.commons.math.optimization.UnivariateRealOptimizer;
import org.junit.Test;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(UnivariateRealOptimizer, int, RandomGenerator)}.
     */
    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        // Create a test function
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return x * x;
            }
        };

        // Create a test optimizer
        UnivariateRealOptimizer testOptimizer = new FibonacciSearchOptimizer();

        // Create a multi-start optimizer
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(
                testOptimizer, 3, new UniformRandomGenerator(1));

        // Test properties
        assertEquals(5, multiStartOptimizer.getIterationCount());
        assertNull(multiStartOptimizer.getResult());
        assertEquals(3, multiStartOptimizer.getStarts());
        assertNull(multiStartOptimizer.getOptima());
        assertNull(multiStartOptimizer.getOptimaValues());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimize() throws ConvergenceException, FunctionEvaluationException {
        // Create a test function
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        // Create a test optimizer
        UnivariateRealOptimizer testOptimizer = new GoldenSectionSearchOptimizer();

        // Create a multi-start optimizer
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(
                testOptimizer, 3, new UniformRandomGenerator(1));

        // Test optimization
        double result = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0, Math.PI);
        assertEquals(Math.PI / 2, result, 0.001);

        // Test optima
        double[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[] { Math.PI / 2, Math.PI / 2 + Math.PI / 4, Math.PI / 2 - Math.PI / 4 }, optima, 0.001);

        // Test optima values
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[] { Math.sin(Math.PI / 2) * Math.sin(Math.PI / 2),
            Math.sin(Math.PI / 2 + Math.PI / 4) * Math.sin(Math.PI / 2 + Math.PI / 4),
            Math.sin(Math.PI / 2 - Math.PI / 4) * Math.sin(Math.PI / 2 - Math.PI / 4) }, optimaValues, 0.001);

        // Test convergence
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(testOptimizer, 1, new UniformRandomGenerator(1));
        try {
            multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0, Math.PI);
            fail();
        } catch (OptimizationException e) {
            // Expected
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @Test
    public void testOptimizeWithStartValue() throws ConvergenceException, FunctionEvaluationException {
        // Create a test function
        UnivariateRealFunction testFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) {
                return Math.pow(Math.sin(x), 2);
            }
        };

        // Create a test optimizer
        UnivariateRealOptimizer testOptimizer = new GoldenSectionSearchOptimizer();

        // Create a multi-start optimizer
        MultiStartUnivariateRealOptimizer multiStartOptimizer = new MultiStartUnivariateRealOptimizer(
                testOptimizer, 3, new UniformRandomGenerator(1));

        // Test optimization with start value
        double result = multiStartOptimizer.optimize(testFunction, GoalType.MINIMIZE, 0, Math.PI, Math.PI / 2);
        assertEquals(Math.PI / 2, result, 0.001);

        // Test optima
        double[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new double[] { Math.PI / 2, Math.PI / 2 + Math.PI / 4, Math.PI / 2 - Math.PI / 4 }, optima, 0.001);

        // Test optima values
        double[] optimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(new double[] { Math.sin(Math.PI / 2) * Math.sin(Math.PI / 2),
                Math.sin(Math.PI / 2 + Math.PI / 4) * Math.sin(Math.PI / 2 + Math.PI / 4),
                Math.sin(Math.PI / 2 - Math.PI / 4) * Math.sin(Math.PI / 2 - Math.PI / 4) }, optimaValues, 0.001);
    }
}
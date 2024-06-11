package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private static final int MAX_ITERATIONS = 500;
    private static final int MAX_EVALUATIONS = 1000;
    private static final int NUM_STARTS = 3;

    private UnivariateOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;

    @Before
    public void setUp() {
        optimizer = new org.apache.commons.math3.optim.nonlinear.scalar.NelderMeadSimplex();
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, NUM_STARTS, new MersenneTwister());
    }

    @Test
    public void testMultiStartOptimization() {
        UnivariateRealFunction function = new PolynomialFunction(new double[]{-1d, 1d, 1d, -1d}, 3);
        double min = -10d;
        double max = 10d;
        GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = multiStartOptimizer.optimize(function, goalType, min, max);

        assertEquals(result.getPoint(), 0d, 1e-12);
        assertEquals(result.getValue(), -1d, 1e-12);

        double[] optima = multiStartOptimizer.getOptima();
        double[] optimaValues = multiStartOptimizer.getOptimaValues();

        assertEquals(optima.length, NUM_STARTS);
        assertEquals(optimaValues.length, NUM_STARTS);

        for (int i = 0; i < optima.length; i++) {
            assertEquals(optima[i], result.getPoint(), 1e-12);
            assertEquals(optimaValues[i], result.getValue(), 1e-12);
        }

        // Verify that the optimizer respects the given bounds
        optimizer = new InitialGuess(new double[]{-1d}, new SimpleBounds(min, max));
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, NUM_STARTS, new MersenneTwister());

        // Test with a function which converges on the bounds
        UnivariateRealFunction convergentFunction = new PolynomialFunction(new double[]{1d, 1d, -1d}, 3);
        result = multiStartOptimizer.optimize(convergentFunction, goalType, min, max);

        assertEquals(result.getPoint(), result.getPoint(), 1e-15);
        assertEquals(result.getValue(), result.getValue(), 1e-15);

        optima = multiStartOptimizer.getOptima();
        optimaValues = multiStartOptimizer.getOptimaValues();

        for (int i = 0; i < optima.length; i++) {
            assertEquals(optima[i], result.getPoint(), 1e-15);
            assertEquals(optimaValues[i], result.getValue(), 1e-15);
        }

        // Test with a function which converges outside the bounds
        UnivariateRealFunction divergentFunction = new PolynomialFunction(new double[]{1d, 1d, -1d, -1d}, 3);
        result = multiStartOptimizer.optimize(divergentFunction, goalType, min, max);

        assertFalse(result instanceof PointValuePair);

        optima = multiStartOptimizer.getOptima();
        optimaValues = multiStartOptimizer.getOptimaValues();

        for (int i = 0; i < optima.length; i++) {
            assertNaN(optima[i]);
            assertNaN(optimaValues[i]);
        }

        // Test with a function which converges within the bounds but not at the minimum
        UnivariateRealFunction nonMinimumFunction = new PolynomialFunction(new double[]{1d, 1d, 1d, 1d}, 3);
        result = multiStartOptimizer.optimize(nonMinimumFunction, goalType, min, max);

        assertFalse(result instanceof PointValuePair);

        optima = multiStartOptimizer.getOptima();
        optimaValues = multiStartOptimizer.getOptimaValues();

        for (int i = 0; i < optima.length; i++) {
            assertNaN(optima[i]);
            assertNaN(optimaValues[i]);
        }

    }

    @Test(expected = ConvergenceException.class)
    public void testConvergenceException() {
        UnivariateRealFunction nonConvergingFunction = new PolynomialFunction(new double[]{1d, 1d, 1d, 0d}, 3);
        multiStartOptimizer.optimize(nonConvergingFunction, GoalType.MINIMIZE, -1d, 1d);
    }

    private void assertEquals(double expected, double actual, double tolerance) {
        assertEquals("Expected: " + expected + ", Actual: " + actual, expected, actual, tolerance);
    }

    private void assertNaN(double value) {
        assertEquals("Expected NaN, but got: " + value, Double.NaN, value);
    }

}
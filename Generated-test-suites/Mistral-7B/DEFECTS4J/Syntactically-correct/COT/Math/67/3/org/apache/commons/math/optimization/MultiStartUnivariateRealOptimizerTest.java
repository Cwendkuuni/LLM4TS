package org.apache.commons.math.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateRealFunction;
import org.apache.commons.math3.random.MersenneTwister;

/**
 * Test cases for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        UnivariateRealOptimizer optimizer = new DifferentialEvolution();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        assertNotNull(multiStartOptimizer);
    }

    /**
     * Test getFunctionValue() method.
     */
    @org.junit.Test
    public void testGetFunctionValue() {
        UnivariateRealOptimizer optimizer = new DifferentialEvolution();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        UnivariateRealFunction function = new PolynomialFunction(2, 1, -1.0, 1.0);
        try {
            double result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0)[0];
            assertEquals(-1.0, result, 1e-6);
            double functionValue = multiStartOptimizer.getFunctionValue();
            assertEquals(-1.0, functionValue, 1e-6);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test getResult() method.
     */
    @org.junit.Test
    public void testGetResult() {
        UnivariateRealOptimizer optimizer = new DifferentialEvolution();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        UnivariateRealFunction function = new PolynomialFunction(2, 1, -1.0, 1.0);
        try {
            double[] optima = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            double result = optima[0];
            assertEquals(-1.0, result, 1e-6);
            double[] resultArray = multiStartOptimizer.getOptima();
            assertEquals(-1.0, resultArray[0], 1e-6);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test getIterationCount() method.
     */
    @org.junit.Test
    public void testGetIterationCount() {
        UnivariateRealOptimizer optimizer = new DifferentialEvolution();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        UnivariateRealFunction function = new PolynomialFunction(2, 1, -1.0, 1.0);
        try {
            int iterations = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0)[3];
            int maximumIterations = multiStartOptimizer.getIterationCount();
            assertEquals(maximumIterations, iterations);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test getMaxIterations() method.
     */
    @org.junit.Test
    public void testGetMaxIterations() {
        UnivariateRealOptimizer optimizer = new DifferentialEvolution();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        int maxIterations = multiStartOptimizer.getMaxIterations();
        assertEquals(Integer.MAX_VALUE, maxIterations);
    }

    /**
     * Test setMaxIterations() method.
     */
    @org.junit.Test
    public void testSetMaxIterations() {
        UnivariateRealOptimizer optimizer = new DifferentialEvolution();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        int maxIterations = 10;
        multiStartOptimizer.setMaxIterations(maxIterations);
        assertEquals(maxIterations, multiStartOptimizer.getMaxIterations());
    }

    /**
     * Test getEvaluations() method.
     */
    @org.junit.Test
    public void testGetEvaluations() {
        UnivariateRealOptimizer optimizer = new DifferentialEvolution();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        UnivariateRealFunction function = new PolynomialFunction(2, 1, -1.0, 1.0);
        try {
            int evaluations = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0)[4];
            int maximumEvaluations = multiStartOptimizer.getEvaluations();
            assertEquals(maximumEvaluations, evaluations);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test getMaxEvaluations() method.
     */
    @org.junit.Test
    public void testGetMaxEvaluations() {
        UnivariateRealOptimizer optimizer = new DifferentialEvolution();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        int maxEvaluations = multiStartOptimizer.getMaxEvaluations();
        assertEquals(Integer.MAX_VALUE, maxEvaluations);
    }

    /**
     * Test setMaxEvaluations() method.
     */
    @org.junit.Test
    public void testSetMaxEvaluations() {
        UnivariateRealOptimizer optimizer = new DifferentialEvolution();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        int maxEvaluations = 10;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, multiStartOptimizer.getMaxEvaluations());
    }

    /**
     * Test optimize() with multiple starts.
     */
    @org.junit.Test
    public void testOptimizeWithMultipleStarts() {
        UnivariateRealOptimizer optimizer = new DifferentialEvolution();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        UnivariateRealFunction function = new PolynomialFunction(2, 1, -1.0, 1.0);
        try {
            double[] optima = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
            double[] expectedOptima = {-1.0, -0.5, -0.3, 0.5, 0.7, -0.8, 0.2, 0.8, 0.9, 0.1};
            assertArrayEquals(expectedOptima, optima, 1e-6);
            double[] expectedOptimaValues = {-1.0, -0.5113723323941673, -0.2758620289655288, 0.4999999999999999,
                    0.5000000000000001, -0.8000000000000001, 0.1999999999999999, 0.7999999999999999,
                    0.8000000000000001, 0.9999999999999999};
            assertArrayEquals(expectedOptimaValues, multiStartOptimizer.getOptimaValues(), 1e-6);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    /**
     * Test optimize() with all starts failing.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testOptimizeWithAllStartsFailing() {
        UnivariateRealOptimizer optimizer = new DifferentialEvolution();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        UnivariateRealFunction function = new PolynomialFunction(-2, 1, 1.0, 2.0);
        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        } catch (IllegalStateException e) {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test getOptima() method when no optimization has been performed.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetOptimaWhenNoOptimizationPerformed() {
        UnivariateRealOptimizer optimizer = new DifferentialEvolution();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        try {
            multiStartOptimizer.getOptima();
        } catch (IllegalStateException e) {
            assertEquals(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, e.getMessage());
            throw e;
        }
    }

    /**
     * Test getOptimaValues() method when no optimization has been performed.
     */
    @org.junit.Test(expected = IllegalStateException.class)
    public void testGetOptimaValuesWhenNoOptimizationPerformed() {
        UnivariateRealOptimizer optimizer = new DifferentialEvolution();
        MultiStartUnivariateRealOptimizer multiStartOptimizer =
                new MultiStartUnivariateRealOptimizer(optimizer, 10, new MersenneTwister());
        try {
            multiStartOptimizer.getOptimaValues();
        } catch (IllegalStateException e) {
            assertEquals(LocalizedFormats.NO_OPTIMUM_COMPUTED_YET, e.getMessage());
            throw e;
        }
    }

}
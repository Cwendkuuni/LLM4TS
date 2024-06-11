package org.apache.commons.math.optimization.test;

import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateRealOptimizer;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for MultiStartUnivariateRealOptimizer
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(UnivariateRealOptimizer, int, RandomGenerator)}
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerConstruction() {
        UnivariateRealOptimizer optimizer = new FibonacciMinimizer();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        assertNotNull(optimizerMS);
        assertEquals(optimizer, optimizerMS.getOptimizer());
        assertEquals(5, optimizerMS.getStarts());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getIterationCount()}
     * with a simple function and minimal number of iterations
     */
    @Test
    public void testGetIterationCountMinIterations() {
        UnivariateRealFunction f = new SquareFunction();
        UnivariateRealOptimizer optimizer = new GoldenSectionFibonacciSearch();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        double x = optimizerMS.optimize(f, GoalType.MINIMIZE, 0.0, 5.0);
        int iterationCount = optimizerMS.getIterationCount();
        assertEquals(20, iterationCount); // 5 starts * 4 iterations per start (default value)
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a simple function and minimal number of iterations
     */
    @Test
    public void testOptimizeMinIterations() {
        UnivariateRealFunction f = new SquareFunction();
        UnivariateRealOptimizer optimizer = new GoldenSectionFibonacciSearch();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        double x = optimizerMS.optimize(f, GoalType.MINIMIZE, 0.0, 5.0);
        assertEquals(1.0, x, 0.01);
        double fx = f.value(x);
        assertEquals(1.0, fx, 0.01);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a simple function and custom number of iterations and evaluations
     */
    @Test
    public void testOptimizeCustomIterationsEvaluations() {
        UnivariateRealFunction f = new SquareFunction();
        UnivariateRealOptimizer optimizer = new BrentMinimizer();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        optimizerMS.setMaxIterations(10);
        optimizerMS.setMaxEvaluations(15);
        double x = optimizerMS.optimize(f, GoalType.MINIMIZE, 0.0, 5.0);
        int iterationCount = optimizerMS.getIterationCount();
        int evaluationCount = optimizerMS.getEvaluations();
        assertEquals(25, iterationCount); // 5 starts * 5 iterations per start
        assertEquals(37, evaluationCount); // 25 iterations * 1.5 evaluations per iteration on average
        assertEquals(1.0, x, 0.01);
        double fx = f.value(x);
        assertEquals(1.0, fx, 0.01);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}
     */
    @Test
    public void testGetOptima() {
        UnivariateRealFunction f = new SquareFunction();
        UnivariateRealOptimizer optimizer = new BrentMinimizer();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        double[] optima = optimizerMS.optimize(f, GoalType.MINIMIZE, 0.0, 5.0);
        assertNotNull(optima);
        assertEquals(5, optima.length);
        for (double d : optima) {
            assertTrue(Double.isFinite(d));
        }
        double[] optimaValues = optimizerMS.getOptimaValues();
        assertNotNull(optimaValues);
        assertEquals(5, optimaValues.length);
        for (double d : optimaValues) {
            assertTrue(Double.isFinite(d));
        }
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a non-converging function
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeNonConvergingFunction() {
        UnivariateRealFunction f = new SineFunction();
        UnivariateRealOptimizer optimizer = new BrentMinimizer();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        optimizerMS.optimize(f, GoalType.MINIMIZE, 0.0, 5.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a function with multiple local minima
     */
    @Test
    public void testOptimizeMultiLocalMinima() {
        UnivariateRealFunction f = new TwoLocalMinimaFunction();
        UnivariateRealOptimizer optimizer = new NelderMeadSimplex();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        double[] optima = optimizerMS.optimize(f, GoalType.MINIMIZE, 0.0, 10.0);
        assertNotNull(optima);
        assertEquals(5, optima.length);
        for (double d : optima) {
            assertTrue(Double.isFinite(d));
        }
        double[] optimaValues = optimizerMS.getOptimaValues();
        assertNotNull(optimaValues);
        assertEquals(5, optimaValues.length);
        for (double d : optimaValues) {
            assertTrue(Double.isFinite(d));
        }
        double[] expectedOptima = {2.0, 3.0, 5.0, 7.0, 8.0};
        double[] expectedOptimaValues = {0.25, 0.67, 0.12, 0.33, 0.5};
        assertArrayEquals(expectedOptima, optima, 0.01);
        assertArrayEquals(expectedOptimaValues, optimaValues, 0.01);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}
     * with a function with no global minimum
     */
    @Test(expected = OptimizationException.class)
    public void testOptimizeNoGlobalMinimum() {
        UnivariateRealFunction f = new NoGlobalMinimumFunction();
        UnivariateRealOptimizer optimizer = new NelderMeadSimplex();
        MultiStartUnivariateRealOptimizer optimizerMS = new MultiStartUnivariateRealOptimizer(optimizer, 5, new MersenneTwister());
        optimizerMS.optimize(f, GoalType.MINIMIZE, 0.0, 10.0);
    }
}
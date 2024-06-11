package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /** Optimizer to wrap. */
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    /** Multi-start optimizer to test. */
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> msOptimizer;
    /** Random generator for multi-start. */
    private MersenneTwister generator;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
        msOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());
    }

    /**
     * Test optimization of a quadratic function.
     */
    @Test
    public void testQuadraticFunction() throws Exception {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 4, 1});
        final GoalType goal = GoalType.MINIMIZE;
        final SimpleBounds bounds = new SimpleBounds(0.1, 1.0);

        final PointValuePair result = msOptimizer.optimize(f, goal, bounds);
        final double expected = -1.0;
        assertEquals("Unexpected minimum", expected, result.getPoint().getValue(), 1e-12);
    }

    /**
     * Test optimization of a simple polynomial function.
     */
    @Test
    public void testSimplePolynomialFunction() throws Exception {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, 2});
        final GoalType goal = GoalType.MINIMIZE;
        final SimpleBounds bounds = new SimpleBounds(0.0, 1.0);

        final PointValuePair result = msOptimizer.optimize(f, goal, bounds);
        final double expected = -0.25;
        assertEquals("Unexpected minimum", expected, result.getPoint().getValue(), 1e-12);
    }

    /**
     * Test optimization of a function with multiple minima.
     */
    @Test
    public void testFunctionWithMultipleMinima() throws Exception {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 1, -12, 8});
        final GoalType goal = GoalType.MINIMIZE;
        final SimpleBounds bounds = new SimpleBounds(0.0, 1.0);

        final PointValuePair[] results = new PointValuePair[5];
        final double[] expectedValues = new double[]{-0.5857864376269764, -0.3926990816983437,
                -0.339170312626562, -0.12634462667030732, 0.632664252536399};

        for (int i = 0; i < 5; ++i) {
            results[i] = msOptimizer.optimize(f, goal, bounds);
        }

        for (int i = 0; i < 5; ++i) {
            final double expected = expectedValues[i];
            assertEquals("Unexpected minimum for start " + i, expected,
                    results[i].getPoint().getValue(), 1e-12);
        }
    }

    /**
     * Test optimization of a function with no solution.
     */
    @Test(expected = ConvergenceException.class)
    public void testFunctionWithNoSolution() throws Exception {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{0, 1, -1});
        final GoalType goal = GoalType.MINIMIZE;
        final SimpleBounds bounds = new SimpleBounds(0.0, 1.0);

        msOptimizer.optimize(f, goal, bounds);
    }

    /**
     * Test optimization of a function with an infinite solution.
     */
    @Test(expected = ConvergenceException.class)
    public void testFunctionWithInfiniteSolution() throws Exception {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{0, 0, 1});
        final GoalType goal = GoalType.MINIMIZE;
        final SimpleBounds bounds = new SimpleBounds(0.0, 1.0);

        msOptimizer.optimize(f, goal, bounds);
    }

    /**
     * Test optimization of a function with a solution at the edge of a bound.
     */
    @Test
    public void testFunctionWithSolutionAtEdgeOfBound() throws Exception {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 2, 1});
        final GoalType goal = GoalType.MINIMIZE;
        final SimpleBounds bounds = new SimpleBounds(0.0, 1.0);

        final PointValuePair result = msOptimizer.optimize(f, goal, bounds);
        final double expected = 0.0;
        assertEquals("Unexpected minimum", expected, result.getPoint().getValue(), 1e-12);
    }

    /**
     * Test optimization of a function with a solution at the end of a bound.
     */
    @Test
    public void testFunctionWithSolutionAtEndOfBound() throws Exception {
        final UnivariateRealFunction f = new PolynomialFunction(new double[]{-1, 2, 1});
        final GoalType goal = GoalType.MINIMIZE;
        final SimpleBounds bounds = new SimpleBounds(1.0, 2.0);

        final PointValuePair result = msOptimizer.optimize(f, goal, bounds);
        final double expected = 1.5;
        assertEquals("Unexpected minimum", expected, result.getPoint().getValue(), 1e-12);
    }
}
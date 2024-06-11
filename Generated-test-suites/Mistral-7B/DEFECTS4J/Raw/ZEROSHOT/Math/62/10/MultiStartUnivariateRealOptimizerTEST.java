package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.special.E;
import org.junit.Before;
import org.junit.Test;

/**
 * Test MultiStartUnivariateRealOptimizer class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /** Underlying optimizer for testing. */
    private UnivariateOptimizer optimizer;
    /** Multi-start optimizer for testing. */
    private MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer;
    /** Minimal and maximal bounds for testing. */
    private double min = -10.0;
    private double max = 10.0;
    /** Goal type for testing. */
    private GoalType goal = GoalType.MINIMIZE;
    /** Number of starts. */
    private int starts = 5;

    /**
     * Set up test environment.
     */
    @Before
    public void setUp() {
        optimizer = new FibonacciSearchOptimizer(new PolynomialFunction(2), new SimpleBounds(min, max));
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, starts, new MersenneTwister());
    }

    /**
     * Test that all methods are accessible.
     */
    @Test
    public void testAccess() {
        assertFalse(multiStartOptimizer.getClass().isInterface());
        assertTrue(multiStartOptimizer instanceof BaseUnivariateRealOptimizer);
        assertTrue(multiStartOptimizer instanceof MultiStartUnivariateRealOptimizer);
        assertTrue(multiStartOptimizer.getClass().isAssignableFrom(BaseUnivariateRealOptimizer.class));
        assertTrue(multiStartOptimizer.getClass().isAssignableFrom(MultiStartUnivariateRealOptimizer.class));
        assertTrue(multiStartOptimizer.getClass().getDeclaredMethod("optimize", PolynomialFunction.class,
                                                                      GoalType.class, double.class, double.class)
                   .isAccessible());
        assertTrue(multiStartOptimizer.getClass().getDeclaredMethod("setConvergenceChecker",
                                                                    ConvergenceChecker.class)
                   .isAccessible());
        assertTrue(multiStartOptimizer.getClass().getDeclaredMethod("getConvergenceChecker")
                   .isAccessible());
        assertTrue(multiStartOptimizer.getClass().getDeclaredMethod("getMaxEvaluations")
                   .isAccessible());
        assertTrue(multiStartOptimizer.getClass().getDeclaredMethod("getEvaluations")
                   .isAccessible());
        assertTrue(multiStartOptimizer.getClass().getDeclaredMethod("setMaxEvaluations", int.class)
                   .isAccessible());
        assertTrue(multiStartOptimizer.getClass().getDeclaredMethod("getOptima")
                   .isAccessible());
    }

    /**
     * Test null optimizer.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullOptimizer() {
        new MultiStartUnivariateRealOptimizer<>(null, starts, new MersenneTwister());
    }

    /**
     * Test null starts generator.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullGenerator() {
        new MultiStartUnivariateRealOptimizer<>(optimizer, null, null);
    }

    /**
     * Test negative number of starts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeStarts() {
        new MultiStartUnivariateRealOptimizer<>(optimizer, -1, new MersenneTwister());
    }

    /**
     * Test zero number of starts.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testZeroStarts() {
        new MultiStartUnivariateRealOptimizer<>(optimizer, 0, new MersenneTwister());
    }

    /**
     * Test get optima.
     */
    @Test
    public void testGetOptima() {
        final int maxEvaluations = MaxEval.UNLIMITED;
        multiStartOptimizer.setMaxEvaluations(maxEvaluations);
        multiStartOptimizer.optimize(new PolynomialFunction(2), goal, min, max);
        final UnivariateRealPointValuePair[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointValuePair[] {null, null, null, null, null}, optima);
        optimizer.optimize(new PolynomialFunction(2), goal, min, max, InitialGuess.THE_MIN);
        multiStartOptimizer.optimize(new PolynomialFunction(2), goal, min, max, InitialGuess.THE_MAX);
        multiStartOptimizer.optimize(new PolynomialFunction(2), goal, min, max, InitialGuess.THE_MID);
        multiStartOptimizer.optimize(new PolynomialFunction(2), goal, min, max, InitialGuess.RANDOM);
        multiStartOptimizer.optimize(new PolynomialFunction(2), goal, min, max, InitialGuess.VALUE);
        final PointValuePair[] expectedOptima = new PointValuePair[] {
            new PointValuePair(FastMath.PI / 4, E.EULER_NUMBER),
            new PointValuePair(FastMath.PI / 4 + 0.1, E.EULER_NUMBER + 0.1),
            new PointValuePair(FastMath.PI / 4 + 0.2, E.EULER_NUMBER + 0.2),
            new PointValuePair(FastMath.PI / 4 + 0.3, E.EULER_NUMBER + 0.3),
            new PointValuePair(FastMath.PI / 4 + 0.4, E.EULER_NUMBER + 0.4)
        };
        assertArrayEquals(expectedOptima, optima);
        optimizer.optimize(new PolynomialFunction(2), goal, min, max);
        optima = multiStartOptimizer.getOptima();
        assertNull(optima[0]);
        assertArrayEquals(expectedOptima, optima, 1);
    }

    /**
     * Test optimization with one start.
     */
    @Test
    public void testOneStart() {
        optimizer.setMaxEvaluations(MaxEval.UNLIMITED);
        final UnivariateRealPointValuePair optima = multiStartOptimizer.optimize(
                new PolynomialFunction(2), goal, min, max);
        assertArrayEquals(new PointValuePair[] {optima}, multiStartOptimizer.getOptima());
        assertFalse(optima.isNaN());
        assertTrue(optima.getValue() > 0.0);
        assertTrue(optima.getValue() < 1.0);
        assertFalse(Double.isNaN(optimizer.getFunctionValue(optima.getPoint())));
    }

    /**
     * Test optimization with multiple starts.
     */
    @Test
    public void testMultipleStarts() {
        optimizer.setMaxEvaluations(MaxEval.UNLIMITED);
        final UnivariateRealPointValuePair[] optima = multiStartOptimizer.optimize(
                new PolynomialFunction(2), goal, min, max, InitialGuess.THE_MIN);
        assertArrayEquals(new PointValuePair[] {optima}, multiStartOptimizer.getOptima());
        assertFalse(optima.isNaN());
        assertTrue(optima.getValue() > 0.0);
        assertTrue(optima.getValue() < 1.0);
        final double epsilon = 1e-6;
        for (int i = 1; i < starts; ++i) {
            final UnivariateRealPointValuePair pointValuePair = multiStartOptimizer.optimize(
                    new PolynomialFunction(2), goal, min, max, InitialGuess.RANDOM);
            assertFalse(pointValuePair.isNaN());
            final double value = pointValuePair.getValue();
            assertTrue(value > 0.0);
            assertTrue(value < 1.0);
            assertFalse(Double.isNaN(optimizer.getFunctionValue(pointValuePair.getPoint())));
            final boolean isBetter = (goal == GoalType.MINIMIZE) ? (value < optima.getValue()) : (value > optima.getValue());
            assertTrue(isBetter);
            optima = new PointValuePair[] {pointValuePair, optima[0]};
        }
        final UnivariateRealPointValuePair lastOptima = multiStartOptimizer.optimize(
                new PolynomialFunction(2), goal, min, max, InitialGuess.THE_MAX);
        assertArrayEquals(new PointValuePair[] {lastOptima, optima[0]}, multiStartOptimizer.getOptima());
        assertFalse(lastOptima.isNaN());
        assertTrue(lastOptima.getValue() > 0.0);
        assertTrue(lastOptima.getValue() < 1.0);
        assertFalse(Double.isNaN(optimizer.getFunctionValue(lastOptima.getPoint())));
        final boolean isBetter = (goal == GoalType.MINIMIZE) ? (lastOptima.getValue() < optima[0].getValue()) : (lastOptima.getValue() > optima[0].getValue());
        assertTrue(isBetter);
    }
}
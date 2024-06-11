package org.apache.commons.math.optimization.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.Polynomials;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealFunction;
import org.apache.commons.math3.optim.UnivariateRealOptimizer;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math3.optim.nonlinear.scalar.SimpleBoundingMethod;
import org.apache.commons.math3.optim.nonlinear.scalar.noderiv.Fibonacci1PointSearch;
import org.apache.commons.math3.random.MersenneTwister;

import java.util.Arrays;

/**
 * Test class for {@link MultiStartUnivariateRealOptimizer}.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(UnivariateRealOptimizer, int, RandomGenerator)}.
     */
    @org.junit.Test
    public void testMultiStartUnivariateRealOptimizerConstruction() {
        final UnivariateRealOptimizer optimizer = new Fibonacci1PointSearch();
        final int starts = 5;
        final RandomGenerator generator = new MersenneTwister();
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(optimizer, starts, generator);
        assertEquals(optimizer, testee.getOptimizer());
        assertEquals(starts, testee.getStarts());
        assertNotNull(testee.getGenerator());
        assertFalse(testee.isOptimized());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getFunctionValue()}.
     */
    @org.junit.Test
    public void testGetFunctionValue() {
        final UnivariateRealFunction f = new Polynomials.Quartic(1, -1.0, 0.0, 1.0, -1.0);
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 5, new MersenneTwister());
        testee.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(testee.getFunctionValue(), f.value(testee.getResult()), 1e-9);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getResult()}.
     */
    @org.junit.Test
    public void testGetResult() {
        final UnivariateRealFunction f = new Polynomials.Quartic(1, -1.0, 0.0, 1.0, -1.0);
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 5, new MersenneTwister());
        testee.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(testee.getResult(), testee.getOptimizer().getResult(), 1e-9);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getAbsoluteAccuracy()}.
     */
    @org.junit.Test
    public void testGetAbsoluteAccuracy() {
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 5, new MersenneTwister());
        testee.setAbsoluteAccuracy(1e-5);
        assertEquals(1e-5, testee.getAbsoluteAccuracy(), 1e-8);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getIterationCount()}.
     */
    @org.junit.Test
    public void testGetIterationCount() {
        final UnivariateRealFunction f = new Polynomials.Quartic(1, -1.0, 0.0, 1.0, -1.0);
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 5, new MersenneTwister());
        testee.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(testee.getIterationCount(), testee.getOptimizer().getIterationCount());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaxIterations()}.
     */
    @org.junit.Test
    public void testGetMaxIterations() {
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 5, new MersenneTwister());
        testee.setMaxIterations(10);
        assertEquals(10, testee.getMaxIterations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaxEvaluations()}.
     */
    @org.junit.Test
    public void testGetMaxEvaluations() {
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 5, new MersenneTwister());
        testee.setMaxEvaluations(10);
        assertEquals(10, testee.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getEvaluations()}.
     */
    @org.junit.Test
    public void testGetEvaluations() {
        final UnivariateRealFunction f = new Polynomials.Quartic(1, -1.0, 0.0, 1.0, -1.0);
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 5, new MersenneTwister());
        testee.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        assertEquals(testee.getEvaluations(), testee.getOptimizer().getEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getRelativeAccuracy()}.
     */
    @org.junit.Test
    public void testGetRelativeAccuracy() {
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 5, new MersenneTwister());
        testee.setRelativeAccuracy(1e-5);
        assertEquals(1e-5, testee.getRelativeAccuracy(), 1e-8);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaximalIterationCount()}.
     */
    @org.junit.Test
    public void testGetMaximalIterationCount() {
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 5, new MersenneTwister());
        testee.setMaxIterations(10);
        assertEquals(10, testee.getMaximalIterationCount());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @org.junit.Test
    public void testOptimize() {
        final UnivariateRealFunction f = new Polynomials.Quartic(1, -1.0, 0.0, 1.0, -1.0);
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 5, new MersenneTwister());
        testee.setMaxIterations(10);
        testee.setMaxEvaluations(10);
        testee.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
        PointValuePair result = testee.getOptimizer().getOptima();
        double[] optima = testee.getOptima();
        double[] optimaValues = testee.getOptimaValues();
        assertArrayEquals(new double[]{result.getPoint(), testee.getResult()}, optima, 1e-9);
        assertArrayEquals(new double[]{result.getValue(), testee.getFunctionValue()}, optimaValues, 1e-9);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @org.junit.Test(expected = FunctionEvaluationException.class)
    public void testOptimizeWithException() {
        final UnivariateRealFunction f = new Polynomials.Quartic(-1, 1.0, 0.0, 1.0, -1.0);
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 5, new MersenneTwister());
        testee.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testOptimizeWithConvergenceException() {
        final UnivariateRealFunction f = new Polynomials.Ross(0.1);
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(
                new SimpleBoundingMethod(), 5, new MersenneTwister());
        testee.optimize(f, GoalType.MINIMIZE, -10.0, 10.0);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @org.junit.Test
    public void testOptimizeWithCustomStartingPoints() {
        final UnivariateRealFunction f = x -> 2 * Math.pow(Math.E, -Math.pow(x, 2));
        final double[] customStartingPoints = {-3.0, -1.0, 0.0, 1.0, 3.0};
        final MultiStartUnivariateRealOptimizer testee = new MultiStartUnivariateRealOptimizer(
                new Fibonacci1PointSearch(), 5, new MersenneTwister());
        testee.setMaxIterations(10);
        testee.setMaxEvaluations(10);
        testee.setStartingPointGenerator(new AveragedStartingPointGenerator(customStartingPoints));
        testee.optimize(f, GoalType.MAXIMIZE, -10.0, 10.0);
        double[] optima = testee.getOptima();
        assertArrayEquals(customStartingPoints, optima, 1e-9);
    }
}
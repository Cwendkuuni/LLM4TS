package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.*;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MaxEvalException;
import org.apache.commons.math3.exception.NoRootsException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AroundMinimum;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeSorter;
import org.apache.commons.math3.random.MersenneTwister;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.BaseUnivariateRealOptimizer;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(BaseUnivariateRealOptimizer, int, RandomGenerator)}.
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerConstruction() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0, 2.0, 1.0}, 5);
        BaseUnivariateRealOptimizer optimizer = new org.apache.commons.math.optim.nonlinear.scalar.Brent(f, null);
        MultiStartUnivariateRealOptimizer<UnivariateFunction> optimizerWithMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());
        assertNotNull(optimizerWithMultiStart);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateFunction, GoalType, double, double)}.
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerMinimization() throws MaxEvalException, NoRootsException, ConvergenceException {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0, 2.0, 1.0}, 5);
        BaseUnivariateRealOptimizer optimizer = new org.apache.commons.math.optim.nonlinear.scalar.Brent(f, null);
        MultiStartUnivariateRealOptimizer<UnivariateFunction> optimizerWithMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        GoalType goal = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;
        PointValuePair result = optimizerWithMultiStart.optimize(f, goal, min, max);

        double expectedMin = -2.0;
        assertEquals(expectedMin, result.getPoint().getValue(), Precision.EPSILON);
        assertEquals(expectedMin, result.getValue(), Precision.EPSILON);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateFunction, GoalType, double, double)}.
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerMaximization() throws MaxEvalException, NoRootsException, ConvergenceException {
        UnivariateFunction f = new PolynomialFunction(new double[]{1.0, 0.0, -1.0, 2.0, 1.0}, 5);
        BaseUnivariateRealOptimizer optimizer = new org.apache.commons.math.optim.nonlinear.scalar.Brent(f, null);
        MultiStartUnivariateRealOptimizer<UnivariateFunction> optimizerWithMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        GoalType goal = GoalType.MAXIMIZE;
        double min = -5.0;
        double max = 5.0;
        PointValuePair result = optimizerWithMultiStart.optimize(f, goal, min, max);

        double expectedMax = 2.0;
        assertEquals(expectedMax, result.getPoint().getValue(), Precision.EPSILON);
        assertEquals(expectedMax, result.getValue(), Precision.EPSILON);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     */
    @Test
    public void testMultiStartUnivariateRealOptimizerGetOptima() {
        UnivariateFunction f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0, 2.0, 1.0}, 5);
        BaseUnivariateRealOptimizer optimizer = new org.apache.commons.math.optim.nonlinear.scalar.Brent(f, null);
        MultiStartUnivariateRealOptimizer<UnivariateFunction> optimizerWithMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new MersenneTwister());

        GoalType goal = GoalType.MINIMIZE;
        double min = -5.0;
        double max = 5.0;
        optimizerWithMultiStart.optimize(f, goal, min, max);

        PointValuePair[] expectedOptima = new PointValuePair[]{
            new PointValuePair(new org.apache.commons.math3.optim.InitialGuess(new AroundMinimum(f, 1.0), null), -2.0),
            new PointValuePair(new org.apache.commons.math3.optim.InitialGuess(new AroundMinimum(f, 3.0), null), -1.5),
            new PointValuePair(new org.apache.commons.math3.optim.InitialGuess(new AroundMinimum(f, 4.0), null), -1.3),
            new PointValuePair(new org.apache.commons.math3.optim.InitialGuess(new AroundMinimum(f, 2.0), null), -1.0),
            null};
        PointValuePair[] resultOptima = optimizerWithMultiStart.getOptima();

        assertArrayEquals(expectedOptima, resultOptima);
    }
}
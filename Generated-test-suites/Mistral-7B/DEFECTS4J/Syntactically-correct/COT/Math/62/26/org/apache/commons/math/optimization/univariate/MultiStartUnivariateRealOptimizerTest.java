package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalTypeStrategy;
import org.apache.commons.math3.optim.nonlinear.scalar.LocalSearch;
import org.apache.commons.math3.optim.nonlinear.scalar.noderay.HookeJeeves;
import org.apache.commons.math3.random.MersenneTwister;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(BaseUnivariateRealOptimizer, int, RandomGenerator)}.
     */
    @SuppressWarnings("unchecked")
    @org.junit.Test
    public void testMultiStartUnivariateRealOptimizerConstruction() {
        final UnivariateFunction function = new PolynomialFunction(2);
        final HookeJeeves optimizer = new HookeJeeves();
        final MersenneTwister generator = new MersenneTwister();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
        assertNotNull(optimizerMultiStart);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setConvergenceChecker(ConvergenceChecker)}.
     */
    @org.junit.Test
    public void testSetConvergenceChecker() {
        final UnivariateFunction function = new PolynomialFunction(2);
        final HookeJeeves optimizer = new HookeJeeves();
        final MersenneTwister generator = new MersenneTwister();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
        final GoalType strategy = new GoalTypeStrategy(GoalType.MINIMIZE);
        final LocalSearch convergenceChecker = new LocalSearch(function, strategy);
        optimizerMultiStart.setConvergenceChecker(convergenceChecker);
        assertNotNull(optimizerMultiStart.getConvergenceChecker());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getConvergenceChecker()}.
     */
    @org.junit.Test
    public void testGetConvergenceChecker() {
        final UnivariateFunction function = new PolynomialFunction(2);
        final HookeJeeves optimizer = new HookeJeeves();
        final MersenneTwister generator = new MersenneTwister();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
        final GoalType strategy = new GoalTypeStrategy(GoalType.MINIMIZE);
        optimizerMultiStart.setConvergenceChecker(new LocalSearch(function, strategy));
        final LocalSearch convergenceChecker = (LocalSearch) optimizerMultiStart.getConvergenceChecker();
        assertNotNull(convergenceChecker);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getMaxEvaluations()}.
     */
    @org.junit.Test
    public void testGetMaxEvaluations() {
        final UnivariateFunction function = new PolynomialFunction(2);
        final HookeJeeves optimizer = new HookeJeeves();
        final MersenneTwister generator = new MersenneTwister();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
        final int maxEvaluations = 10;
        optimizerMultiStart.setMaxEvaluations(maxEvaluations);
        assertEquals(maxEvaluations, optimizerMultiStart.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getEvaluations()}.
     */
    @org.junit.Test
    public void testGetEvaluations() {
        final UnivariateFunction function = new PolynomialFunction(2);
        final HookeJeeves optimizer = new HookeJeeves();
        final MersenneTwister generator = new MersenneTwister();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
        int evaluations = 0;
        final UnivariateRealPointValuePair pointValuePair = optimizerMultiStart.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        evaluations += optimizerMultiStart.getEvaluations();
        assertEquals(pointValuePair.getValue(), 0.0, 0.001);
        assertEquals(evaluations, 1);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setMaxEvaluations(int)}.
     */
    @org.junit.Test
    public void testSetMaxEvaluations() {
        final UnivariateFunction function = new PolynomialFunction(2);
        final HookeJeeves optimizer = new HookeJeeves();
        final MersenneTwister generator = new MersenneTwister();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
        final int maxEvaluations = 10;
        optimizerMultiStart.setMaxEvaluations(maxEvaluations);
        optimizerMultiStart.optimize(function, GoalType.MINIMIZE, 0.0, 1.0);
        assertEquals(optimizerMultiStart.getMaxEvaluations(), maxEvaluations);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     */
    @org.junit.Test
    public void testGetOptima() {
        final UnivariateFunction function = new PolynomialFunction(2);
        final HookeJeeves optimizer = new HookeJeeves();
        final MersenneTwister generator = new MersenneTwister();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
        final UnivariateRealPointValuePair[] optima = optimizerMultiStart.optimize(function, GoalType.MINIMIZE, 0.0, 1.0, 0.0);
        final UnivariateRealPointValuePair[] optimaExpected = new UnivariateRealPointValuePair[] {
            new UnivariateRealPointValuePair(0.2725562, 0.1232314),
            new UnivariateRealPointValuePair(0.4569522, 0.0301302),
            new UnivariateRealPointValuePair(0.6366697, 0.0),
            null
        };
        assertArrayEquals(optimaExpected, optimizerMultiStart.getOptima());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @org.junit.Test
    public void testOptimize() throws FunctionEvaluationException {
        final UnivariateFunction function = new PolynomialFunction(2);
        final HookeJeeves optimizer = new HookeJeeves();
        final MersenneTwister generator = new MersenneTwister();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
        final double min = 0.0;
        final double max = 1.0;
        final UnivariateRealPointValuePair pointValuePair = optimizerMultiStart.optimize(function, GoalType.MINIMIZE, min, max);
        assertEquals(pointValuePair.getValue(), 0.0, 0.001);
        assertTrue(pointValuePair.getPoint() > 0.5);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @org.junit.Test
    public void testOptimizeWithStartValue() throws FunctionEvaluationException {
        final UnivariateFunction function = new PolynomialFunction(2);
        final HookeJeeves optimizer = new HookeJeeves();
        final MersenneTwister generator = new MersenneTwister();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
        final double min = 0.0;
        final double max = 1.0;
        final double startValue = 0.75;
        final UnivariateRealPointValuePair pointValuePair = optimizerMultiStart.optimize(function, GoalType.MINIMIZE, min, max, startValue);
        assertEquals(pointValuePair.getValue(), 0.0, 0.001);
        assertTrue(pointValuePair.getPoint() > 0.5);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double, double)}.
     */
    @org.junit.Test(expected = ConvergenceException.class)
    public void testOptimizeNoConvergence() throws FunctionEvaluationException {
        final UnivariateFunction function = new PolynomialFunction(-2);
        final HookeJeeves optimizer = new HookeJeeves();
        final MersenneTwister generator = new MersenneTwister();
        final MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart =
            new MultiStartUnivariateRealOptimizer<UnivariateRealFunction>(optimizer, 3, generator);
        final double min = 0.0;
        final double max = 1.0;
        optimizerMultiStart.optimize(function, GoalType.MINIMIZE, min, max);
    }
}
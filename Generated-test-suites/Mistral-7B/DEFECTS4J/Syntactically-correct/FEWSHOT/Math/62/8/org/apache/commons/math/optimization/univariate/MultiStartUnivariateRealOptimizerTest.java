package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.nonlinear.SimpleBounds;
import org.apache.commons.math3.optim.nonlinear.SimpleGoalType;
import org.apache.commons.math3.optim.nonlinear.simple.NelderMeadSimplex;
import org.apache.commons.math3.random.MersenneTwister;

import org.apache.commons.math.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.math.optimization.univariate.UnivariateRealFunction;
import org.apache.commons.math.optimization.univariate.UnivariateRealPointValuePair;

/**
 * Tests for MultiStartUnivariateRealOptimizer class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#MultiStartUnivariateRealOptimizer(BaseUnivariateRealOptimizer, int, RandomGenerator)}.
     */
    @Test
    public void testMultiStartUnivariateRealOptimizer() {
        UnivariateRealFunction func = new PolynomialFunction(2, new double[][]{{-1.0, 1.0}, {1.0, 2.0}});
        UnivariateOptimizer optimizer = new NelderMeadSimplex();
        RandomGenerator random = new MersenneTwister();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, random);
        assertEquals(10, optimizerMS.getStarts());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#setMaxEvaluations(int)}.
     */
    @Test
    public void testSetMaxEvaluations() {
        UnivariateRealFunction func = new PolynomialFunction(2, new double[][]{{-1.0, 1.0}, {1.0, 2.0}});
        UnivariateOptimizer optimizer = new NelderMeadSimplex();
        RandomGenerator random = new MersenneTwister();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, random);
        optimizerMS.setMaxEvaluations(100);
        assertEquals(100, optimizerMS.getMaxEvaluations());
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#getOptima()}.
     */
    @Test
    public void testGetOptima() throws Exception {
        UnivariateRealFunction func = new PolynomialFunction(2, new double[][]{{-1.0, 1.0}, {1.0, 2.0}});
        UnivariateOptimizer optimizer = new NelderMeadSimplex();
        RandomGenerator random = new MersenneTwister();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, random);
        UnivariateRealPointValuePair expectedOptima[] = new UnivariateRealPointValuePair[10];
        for (int i = 0; i < 10; i++) {
            PointValuePair p = optimizer.optimize(func, SimpleGoalType.MINIMIZE, new SimpleBounds(0.0, 1.0), new MaxEval(100));
            expectedOptima[i] = new UnivariateRealPointValuePair(p.getPoint(), p.getValue());
            optimizerMS.setMaxEvaluations(100);
        }
        UnivariateRealPointValuePair[] actualOptima = optimizerMS.optimize(func, GoalType.MINIMIZE, -1.0, 1.0).getOptima();
        assertArrayEquals(expectedOptima, actualOptima);
    }

    /**
     * Test method for {@link MultiStartUnivariateRealOptimizer#optimize(UnivariateRealFunction, GoalType, double, double)}.
     */
    @Test
    public void testOptimize() throws Exception {
        UnivariateRealFunction func = new PolynomialFunction(2, new double[][]{{-1.0, 1.0}, {1.0, 2.0}});
        UnivariateOptimizer optimizer = new NelderMeadSimplex();
        RandomGenerator random = new MersenneTwister();
        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, random);
        UnivariateRealPointValuePair bestOptima = optimizerMS.optimize(func, GoalType.MINIMIZE, -1.0, 1.0);
        assertNotNull(bestOptima);
    }
}
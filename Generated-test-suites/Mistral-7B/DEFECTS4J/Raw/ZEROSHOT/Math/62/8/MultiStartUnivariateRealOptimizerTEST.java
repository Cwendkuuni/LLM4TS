package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math3.analysis.PolynomialFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.initializer.AveragedStartingPointGenerator;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.LocalSearch;
import org.apache.commons.math3.random.MersenneTwister;
import org.junit.Before;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer;
    private PolynomialFunction f;
    private MersenneTwister generator;

    @Before
    public void setUp() {
        optimizer = new LocalSearch();
        optimizer.setMaxIterations(100);
        optimizer.setConvergenceChecker(new ConvergenceChecker<PointValuePair>() {
            public boolean isConverged(PointValuePair current, PointValuePair previous, int iteration) {
                return (current != null && previous != null && Math.abs(current.getValue() - previous.getValue()) < 0.01);
            }
        });

        f = new PolynomialFunction(new double[]{-1.0, 0.0, 1.0}, 2);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 5, new AveragedStartingPointGenerator(2, new SimpleBounds(1.0, 5.0), new MersenneTwister()));
    }

    @Test
    public void testMultiStartMinimization() throws Exception {
        final double min = 0.5;
        final double max = 2.0;
        final GoalType goalType = GoalType.MINIMIZE;

        PointValuePair result = multiStartOptimizer.optimize(f, goalType, min, max);

        assertNotNull(result);
        assertEquals(min, result.getPoint().getX(), 0.01);
        assertEquals(f.value(min), result.getValue(), 0.01);

        PointValuePair[] optima = multiStartOptimizer.getOptima();

        assertArrayEquals(new PointValuePair[]{result, null, null, null, null}, optima);
    }

    @Test
    public void testMultiStartMaximization() throws Exception {
        final double min = 0.5;
        final double max = 2.0;
        final GoalType goalType = GoalType.MAXIMIZE;

        PointValuePair result = multiStartOptimizer.optimize(new PolynomialFunction(new double[]{-1.0, 0.0, 1.0}, -1), goalType, min, max);

        assertNotNull(result);
        assertEquals(max, result.getPoint().getX(), 0.01);
        assertEquals(f.value(max), result.getValue(), 0.01);

        PointValuePair[] optima = multiStartOptimizer.getOptima();

        assertArrayEquals(new PointValuePair[]{null, null, null, null, result}, optima);
    }

    @Test
    public void testMultiStartConvergenceException() throws Exception {
        final double min = 0.5;
        final double max = 2.0;
        final GoalType goalType = GoalType.MINIMIZE;
        PolynomialFunction f = new PolynomialFunction(new double[]{-1.0, 1.0, 1.0}, 2);

        try {
            multiStartOptimizer.optimize(f, goalType, min, max);
            fail("Expected ConvergenceException");
        } catch (ConvergenceException e) {
            // Expected
        }

        PointValuePair[] optima = multiStartOptimizer.getOptima();
        assertArrayEquals(new PointValuePair[5], optima);
        for (PointValuePair pa : optima) {
            assertFalse(pa != null);
        }
    }

    @Test
    public void testMultiStartMaxEvaluations() throws Exception {
        final double min = 0.5;
        final double max = 2.0;
        final GoalType goalType = GoalType.MINIMIZE;
        optimizer.setMaxIterations(1);
        MultiStartUnivariateRealOptimizer<PolynomialFunction> multiStartOptimizer = new MultiStartUnivariateRealOptimizer<>(optimizer, 10, new MersenneTwister());

        try {
            multiStartOptimizer.optimize(f, goalType, min, max);
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }

        assertEquals(10, multiStartOptimizer.getMaxEvaluations());
        assertEquals(10, multiStartOptimizer.getEvaluations());
    }
}
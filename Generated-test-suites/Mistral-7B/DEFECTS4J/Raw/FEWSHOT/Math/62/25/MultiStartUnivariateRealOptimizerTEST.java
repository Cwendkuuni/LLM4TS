package org.apache.commons.cli.test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.commons.math.analysis.PolynomialFunction;
import org.apache.commons.commons.math.exception.FunctionEvaluationException;
import org.apache.commons.commons.math.optimization.GoalType;
import org.apache.commons.commons.math.optimization.UnivariateRealFunction;
import org.apache.commons.commons.math.optimization.UnivariateRealPointValuePair;
import org.apache.commons.commons.random.RandomGenerator;
import org.apache.commons.commons.random.RandomUtils;
import org.apache.commons.commons.math.util.Precision;
import org.apache.commons.commons.math.util.FastMath;
import org.apache.commons.commons.optimization.univariate.MultiStartUnivariateRealOptimizer;
import org.apache.commons.commons.optimization.univariate.BaseUnivariateRealOptimizer;
import org.junit.Test;

public class MultiStartUnivariateRealOptimizerTest {

    @Test
    public void testMultiStartMinimize() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1d, 2d, -1d}, new double[]{0d, 0d, 1d});
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mock(BaseUnivariateRealOptimizer.class);
        when(optimizer.optimize(f, GoalType.MINIMIZE, -1d, 1d)).thenReturn(new UnivariateRealPointValuePair(0d, 0d));
        when(optimizer.optimize(f, GoalType.MINIMIZE, -1d, 1d)).thenReturn(new UnivariateRealPointValuePair(1d, 2d));

        RandomGenerator rg = new RandomUtils();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, rg);

        UnivariateRealPointValuePair result = optimizerMultiStart.optimize(f, GoalType.MINIMIZE, -1d, 1d);

        assertEquals(0d, result.getX(), Precision.SAFE_EPSILON);
        assertEquals(0d, result.getValue(), Precision.SAFE_EPSILON);

        UnivariateRealPointValuePair[] optima = optimizerMultiStart.getOptima();

        assertArrayEquals(new UnivariateRealPointValuePair[]{new UnivariateRealPointValuePair(0d, 0d), new UnivariateRealPointValuePair(1d, 2d)}, optima);
    }

    @Test
    public void testMultiStartMaximize() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1d, 2d, -1d}, new double[]{0d, 0d, 1d});
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mock(BaseUnivariateRealOptimizer.class);
        when(optimizer.optimize(f, GoalType.MAXIMIZE, -1d, 1d)).thenReturn(new UnivariateRealPointValuePair(1d, 2d));
        when(optimizer.optimize(f, GoalType.MAXIMIZE, -1d, 1d)).thenReturn(new UnivariateRealPointValuePair(0d, 0d));

        RandomGenerator rg = new RandomUtils();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, rg);

        UnivariateRealPointValuePair result = optimizerMultiStart.optimize(f, GoalType.MAXIMIZE, -1d, 1d);

        assertEquals(1d, result.getX(), Precision.SAFE_EPSILON);
        assertEquals(2d, result.getValue(), Precision.SAFE_EPSILON);

        UnivariateRealPointValuePair[] optima = optimizerMultiStart.getOptima();

        assertArrayEquals(new UnivariateRealPointValuePair[]{new UnivariateRealPointValuePair(1d, 2d), new UnivariateRealPointValuePair(0d, 0d)}, optima);
    }

    @Test
    public void testMultiStartNoConvergence() throws Exception {
        UnivariateRealFunction f = new PolynomialFunction(new double[]{-1d, 2d, -1d}, new double[]{0d, 0d, 1d});
        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mock(BaseUnivariateRealOptimizer.class);
        when(optimizer.optimize(f, GoalType.MINIMIZE, -1d, 1d)).thenThrow(new ConvergenceException("Mock ConvergenceException"));
        when(optimizer.optimize(f, GoalType.MINIMIZE, -1d, 1d)).thenThrow(new ConvergenceException("Mock ConvergenceException"));

        RandomGenerator rg = new RandomUtils();

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMultiStart = new MultiStartUnivariateRealOptimizer<>(optimizer, 2, rg);

        try {
            optimizerMultiStart.optimize(f, GoalType.MINIMIZE, -1d, 1d);
            fail("Expected ConvergenceException to be thrown");
        } catch (ConvergenceException e) {
            // Expected
        }

        UnivariateRealPointValuePair[] optima = optimizerMultiStart.getOptima();

        assertArrayEquals(new UnivariateRealPointValuePair[0], optima);
    }
}
package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.UnivariateRealOptimizer;
import org.apache.commons.math3.optimization.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optimization.UnivariateRealPointValuePair;
import org.junit.Test;

/**
 * Test file for MultiStartUnivariateRealOptimizer class.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /**
     * Test method for optimize(UnivariateRealFunction, GoalType, double, double)
     * with minimization goal type.
     * @throws Exception
     */
    @Test
    public void testMinimization() throws Exception {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        when(f.value(Double.MIN_VALUE)).thenThrow(new FunctionEvaluationException("Test error", null));
        when(f.value(0.0)).thenReturn(1.0);
        when(f.value(2.0)).thenReturn(0.5);
        when(f.value(3.0)).thenReturn(2.0);

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mock(BaseUnivariateRealOptimizer.class);
        when(optimizer.optimize(f, GoalType.MINIMIZE, 0.0, 4.0)).thenReturn(new UnivariateRealPointValuePair(1.5, 0.75));

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        UnivariateRealPointValuePair result = optimizerMS.optimize(f, GoalType.MINIMIZE, 0.0, 4.0);

        UnivariateRealPointValuePair[] expectedOptima = new UnivariateRealPointValuePair[3];
        expectedOptima[0] = new UnivariateRealPointValuePair(1.5, 0.75);
        expectedOptima[1] = new UnivariateRealPointValuePair(null, null);
        expectedOptima[2] = new UnivariateRealPointValuePair(null, null);
        assertArrayEquals(expectedOptima, optimizerMS.getOptima());

        assertNull(result);
    }

    /**
     * Test method for optimize(UnivariateRealFunction, GoalType, double, double)
     * with maximization goal type.
     * @throws Exception
     */
    @Test
    public void testMaximization() throws Exception {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);
        when(f.value(Double.MIN_VALUE)).thenThrow(new FunctionEvaluationException("Test error", null));
        when(f.value(0.0)).thenReturn(1.0);
        when(f.value(2.0)).thenReturn(0.5);
        when(f.value(3.0)).thenReturn(2.0);

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mock(BaseUnivariateRealOptimizer.class);
        when(optimizer.optimize(f, GoalType.MAXIMIZE, 0.0, 4.0)).thenReturn(new UnivariateRealPointValuePair(1.5, 0.75));

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        UnivariateRealPointValuePair result = optimizerMS.optimize(f, GoalType.MAXIMIZE, 0.0, 4.0);

        UnivariateRealPointValuePair[] expectedOptima = new UnivariateRealPointValuePair[3];
        expectedOptima[0] = new UnivariateRealPointValuePair(1.5, 0.75);
        expectedOptima[1] = new UnivariateRealPointValuePair(null, null);
        expectedOptima[2] = new UnivariateRealPointValuePair(null, null);
        assertArrayEquals(expectedOptima, optimizerMS.getOptima());

        assertNull(result);
    }

    /**
     * Test method for optimize(UnivariateRealFunction, GoalType, double, double)
     * with goal type not supported.
     * @throws Exception
     */
    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedGoalType() throws Exception {
        UnivariateRealFunction f = mock(UnivariateRealFunction.class);

        BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizer = mock(BaseUnivariateRealOptimizer.class);

        MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizerMS = new MultiStartUnivariateRealOptimizer<>(optimizer, 3, new RandomGenerator());
        optimizerMS.optimize(f, GoalType.MAX_ENUM, 0.0, 4.0);
    }
}
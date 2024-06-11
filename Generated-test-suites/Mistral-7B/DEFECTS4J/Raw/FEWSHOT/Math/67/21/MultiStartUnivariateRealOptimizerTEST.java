package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.math.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.OptimizationException;
import org.apache.commons.math.optimization.UnivariateRealOptimizer;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    private UnivariateRealFunction function;
    private UnivariateRealOptimizer optimizer;
    private MultiStartUnivariateRealOptimizer multiStartOptimizer;
    private RandomGenerator generator;

    @Before
    public void setUp() {
        function = mock(UnivariateRealFunction.class);
        optimizer = mock(UnivariateRealOptimizer.class);
        generator = mock(RandomGenerator.class);
        multiStartOptimizer = new MultiStartUnivariateRealOptimizer(optimizer, 5, generator);
    }

    @Test
    public void testGettersAndSetters() {
        assertEquals(0, multiStartOptimizer.getIterationCount());
        assertEquals(0, multiStartOptimizer.getEvaluations());
        assertEquals(Integer.MAX_VALUE, multiStartOptimizer.getMaxIterations());
        assertEquals(Integer.MAX_VALUE, multiStartOptimizer.getMaxEvaluations());
        assertEquals(null, multiStartOptimizer.getOptima());
        assertEquals(null, multiStartOptimizer.getOptimaValues());

        multiStartOptimizer.setMaxIterations(10);
        multiStartOptimizer.setMaxEvaluations(20);
    }

    @Test
    public void testOptimizeWithOneStart() {
        when(optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenReturn(1.0);

        double result = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 5.0);
        assertEquals(1.0, result, 0.001);
        verify(optimizer, times(1)).optimize(function, GoalType.MINIMIZE, anyDouble(), anyDouble());
        assertArrayEquals(new double[] { 1.0 }, multiStartOptimizer.getOptima());
        assertArrayEquals(new double[] { 1.0 }, multiStartOptimizer.getOptimaValues());
    }

    @Test
    public void testOptimizeWithMultipleStarts() {
        when(optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenThrow(new ConvergenceException("ConvergenceException"));
        when(generator.nextDouble()).thenReturn(0.1, 0.4, 0.2, 0.3, 0.5);

        double[] expectedOptima = { 2.1, 3.2, 1.5, 4.5, 5.0 };
        double[] expectedOptimaValues = { 1.2, 2.3, 1.1, 3.5, 4.0 };

        for (int i = 0; i < 5; i++) {
            when(optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                    .thenReturn(expectedOptima[i]);
        }

        double[] resultOptima = multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 5.0);
        assertArrayEquals(Arrays.copyOf(expectedOptima, expectedOptima.length), resultOptima);

        double[] resultOptimaValues = multiStartOptimizer.getOptimaValues();
        assertArrayEquals(Arrays.copyOf(expectedOptimaValues, expectedOptimaValues.length), resultOptimaValues);

        verify(optimizer, times(5)).optimize(function, GoalType.MINIMIZE, anyDouble(), anyDouble());
    }

    @Test(expected = IllegalStateException.class)
    public void testOptimizeWithoutPreviousCall() {
        multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 5.0);
        multiStartOptimizer.getOptima();
    }

    @Test
    public void testOptimizeWithConvergenceException() {
        when(optimizer.optimize(any(UnivariateRealFunction.class), any(GoalType.class), anyDouble(), anyDouble()))
                .thenThrow(new ConvergenceException("ConvergenceException"));

        try {
            multiStartOptimizer.optimize(function, GoalType.MINIMIZE, 0.0, 5.0);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            assertTrue(e.getMessage().contains("NO_OPTIMUM_COMPUTED_YET"));
        }
    }

    @Test
    public void testOptimizeWithNullFunction() {
        try {
            multiStartOptimizer.optimize(null, GoalType.MINIMIZE, 0.0, 5.0);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("function cannot be null"));
        }
    }
}
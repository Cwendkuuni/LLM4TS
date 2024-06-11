package org.apache.commons.math.optimization.univariate;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optim.BaseUnivariateRealOptimizer;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateRealPointValuePair;
import org.apache.commons.math3.random.RandomGenerator;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {

    /** Mock optimizer. */
    private BaseUnivariateRealOptimizer<UnivariateRealFunction> optimizerMock;
    /** Mock random generator. */
    private RandomGenerator generatorMock;
    /** Tested optimizer. */
    private MultiStartUnivariateRealOptimizer<UnivariateRealFunction> optimizer;
    /** Test function. */
    private UnivariateRealFunction function;
    /** Test goal type. */
    private GoalType goal;
    /** Test minimum and maximum bounds. */
    private double min, max;

    /**
     * Set up.
     * @throws Exception for any error.
     */
    @Before
    public void setUp() throws Exception {
        optimizerMock = mock(BaseUnivariateRealOptimizer.class);
        generatorMock = mock(RandomGenerator.class);
        function = mock(UnivariateRealFunction.class);
        goal = GoalType.MINIMIZE;
        min = 0.0;
        max = 1.0;
        optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 3, generatorMock);
    }

    /**
     * Test optimize method with no starts.
     * @throws Exception for any error.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNoStarts() throws Exception {
        when(optimizerMock.optimize(function, goal, min, max)).thenReturn(new PointValuePair(0.0, 0.0));
        optimizer = new MultiStartUnivariateRealOptimizer<>(optimizerMock, 0, generatorMock);
        optimizer.optimize(function, goal, min, max);
    }

    /**
     * Test optimize method with one start.
     * @throws Exception for any error.
     */
    @Test
    public void testOptimizeOneStart() throws Exception {
        when(optimizerMock.optimize(function, goal, min, max)).thenReturn(new PointValuePair(0.5, 0.25));
        when(generatorMock.nextDouble()).thenReturn(0.5);
        optimizer.optimize(function, goal, min, max);
        UnivariateRealPointValuePair expected = new UnivariateRealPointValuePair(0.5, 0.25);
        verify(optimizer).setMaxEvaluations(optimizer.getMaxEvaluations());
        verify(optimizer).optimize(function, goal, min, max);
        assertEquals(expected, optimizer.getOptima()[0]);
    }

    /**
     * Test optimize method with multiple starts.
     * @throws Exception for any error.
     */
    @Test
    public void testOptimizeMultipleStarts() throws Exception {
        when(optimizerMock.optimize(function, goal, min, max)).thenReturn(new PointValuePair(0.5, 0.25));
        when(generatorMock.nextDouble()).thenReturn(0.0, 0.5, 1.0).inOrder();
        optimizer.optimize(function, goal, min, max);
        UnivariateRealPointValuePair[] expected = new UnivariateRealPointValuePair[]{
                new UnivariateRealPointValuePair(0.0, null),
                new UnivariateRealPointValuePair(0.5, 0.25),
                new UnivariateRealPointValuePair(1.0, null)
        };
        verify(optimizer).setMaxEvaluations(optimizer.getMaxEvaluations());
        verify(optimizer, times(3)).optimize(function, goal, min, max);
        assertArrayEquals(expected, optimizer.getOptima());
    }

    /**
     * Test optimize method with convergence exception.
     * @throws Exception for any error.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeConvergenceException() throws Exception {
        when(optimizerMock.optimize(function, goal, min, max)).thenThrow(new ConvergenceException());
        optimizer.optimize(function, goal, min, max);
    }
}
package org.apache.commons.math.optimization.univariate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Comparator;

import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.FunctionEvaluationException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.UnsupportedOperationException;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.random.RandomGenerator;
import org.junit.Test;

/**
 * Test MultiStartUnivariateRealOptimizer.
 */
public class MultiStartUnivariateRealOptimizerTest {
    /** A simple function for testing. */
    private static class TestFunction implements UnivariateRealFunction {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            return Math.pow(Math.sin(x), 2);
        }

        @Override
        public double derivative(double x) throws FunctionEvaluationException {
            return 2 * Math.sin(x) * Math.cos(x);
        }

        @Override
        public double derivative2(double x) throws FunctionEvaluationException {
            return 2 * (Math.cos(x) * Math.cos(x) - Math.sin(x) * Math.sin(x));
        }

        @Override
        public int getNumberOfVariables() {
            throw new UnsupportedOperationException("Not supported yet.");
        }

        @Override
        public String toString() {
            return "TestFunction";
        }
    }

    /** Tested class. */
    private MultiStartUnivariateRealOptimizer<TestFunction> optimizer;

    /** Tested optimizer. */
    private UnivariateOptimizer<TestFunction> wrappedOptimizer;

    /** Random generator for multi-start. */
    private RandomGenerator generator;

    /**
     * Set up.
     */
    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        wrappedOptimizer = mock(UnivariateOptimizer.class);
        optimizer = new MultiStartUnivariateRealOptimizer<TestFunction>(wrappedOptimizer, 10, mock(RandomGenerator.class));
    }

    /**
     * Test getOptima when no optimization has been done yet.
     */
    @Test(expected = MathIllegalStateException.class)
    public void testGetOptimaBeforeOptimize() {
        optimizer.getOptima();
    }

    /**
     * Test getOptima with no converged starts.
     */
    @Test
    public void testGetOptimaNoConvergence() {
        when(wrappedOptimizer.getOptima()).thenThrow(new ConvergenceException("Test"));
        when(wrappedOptimizer.optimize(any(TestFunction.class), eq(GoalType.MINIMIZE), anyDouble(), anyDouble()))
                .thenThrow(new ConvergenceException("Test"));

        optimizer.setMaxEvaluations(10);
        optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0, 1);

        assertEquals(10, optimizer.getEvaluations());
        assertArrayEquals(new UnivariateRealPointValuePair[10], optimizer.getOptima());
    }

    /**
     * Test getOptima with some converged starts.
     */
    @Test
    public void testGetOptimaWithConvergence() {
        when(wrappedOptimizer.getOptima()).thenReturn(new PointValuePair[]{new PointValuePair(1.0, 2.0),
                new PointValuePair(3.0, 5.0), new PointValuePair(4.0, 1.0), null, null, null, null, null, null, null});

        optimizer.setMaxEvaluations(10);
        optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0, 1);

        assertEquals(10, optimizer.getEvaluations());
        assertArrayEquals(new UnivariateRealPointValuePair[]{new UnivariateRealPointValuePair(1.0, 2.0),
                new UnivariateRealPointValuePair(3.0, 5.0), new UnivariateRealPointValuePair(4.0, 1.0)}, optimizer.getOptima());
    }

    /**
     * Test optimize with no evaluations.
     */
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeNoEvaluation() {
        optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0, 1);
    }

    /**
     * Test optimize with no goal type.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNoGoalType() {
        optimizer.optimize(new TestFunction(), null, 0, 1);
    }

    /**
     * Test optimize with no bounds.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeNoBounds() {
        optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0, 0);
    }

    /**
     * Test optimize with some evaluations.
     */
    @Test
    public void testOptimize() throws Exception {
        when(wrappedOptimizer.optimize(any(TestFunction.class), eq(GoalType.MINIMIZE), anyDouble(), anyDouble()))
                .thenThrow(new ConvergenceException("Test"));
        when(wrappedOptimizer.optimize(any(TestFunction.class), eq(GoalType.MINIMIZE), anyDouble(), anyDouble()))
                .thenReturn(new PointValuePair(1.0, 2.0));
        when(wrappedOptimizer.getOptima()).thenReturn(new PointValuePair[]{new PointValuePair(1.0, 2.0), null});

        optimizer.setMaxEvaluations(20);
        optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0, 1);

        assertEquals(10, optimizer.getEvaluations());
        assertArrayEquals(new UnivariateRealPointValuePair[]{new UnivariateRealPointValuePair(1.0, 2.0)}, optimizer.getOptima());
    }

    /**
     * Test optimize with some evaluations and some failed starts.
     */
    @Test
    public void testOptimizeWithFailedStarts() throws Exception {
        when(wrappedOptimizer.optimize(any(TestFunction.class), eq(GoalType.MINIMIZE), anyDouble(), anyDouble()))
                .thenThrow(new ConvergenceException("Test"))
                .thenReturn(new PointValuePair(1.0, 2.0))
                .thenThrow(new NoBracketingException("Test", 0, 1));
        when(wrappedOptimizer.getOptima()).thenReturn(new PointValuePair[]{null, null, new PointValuePair(1.0, 2.0), null, null, null, null, null, null, null});

        optimizer.setMaxEvaluations(20);
        optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0, 1);

        assertEquals(10, optimizer.getEvaluations());
        assertArrayEquals(new UnivariateRealPointValuePair[]{new UnivariateRealPointValuePair(1.0, 2.0)}, optimizer.getOptima());
    }

    /**
     * Test optimize with some evaluations and some failed starts, and maxEvaluations < totalEvaluations.
     */
    @Test(expected = ConvergenceException.class)
    public void testOptimizeWithFailedStartsAndMaxEvaluationsTooLow() throws Exception {
        when(wrappedOptimizer.optimize(any(TestFunction.class), eq(GoalType.MINIMIZE), anyDouble(), anyDouble()))
                .thenThrow(new ConvergenceException("Test"))
                .thenReturn(new PointValuePair(1.0, 2.0))
                .thenThrow(new NoBracketingException("Test", 0, 1));
        when(wrappedOptimizer.getOptima()).thenReturn(new PointValuePair[]{null, null, new PointValuePair(1.0, 2.0), null, null, null, null, null, null, null});

        optimizer.setMaxEvaluations(5);
        optimizer.optimize(new TestFunction(), GoalType.MINIMIZE, 0, 1);
    }
}
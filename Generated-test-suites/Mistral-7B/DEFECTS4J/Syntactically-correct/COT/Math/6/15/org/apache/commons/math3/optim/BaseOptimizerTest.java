package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.SimpleBounds;
import org.apache.commons.math3.optim.univariate.UnivariateConvergenceChecker;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateOptimizerFactory;
import org.apache.commons.math3.optim.univariate.UnivariateValueChecker;
import org.apache.commons.math3.optim.univariate.convergence.ConvergenceChecker;
import org.apache.commons.math3.optim.univariate.differencers.PowDifferencer;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class BaseOptimizerTest<PAIR> {

    private BaseOptimizer<PAIR> optimizer;
    private int maxEvaluations;
    private ConvergenceChecker<PAIR> convergenceChecker;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        optimizer = mock(BaseOptimizer.class);
        maxEvaluations = 10;
        convergenceChecker = mock(ConvergenceChecker.class);
    }

    @Test
    public void testGetMaxEvaluations() {
        when(optimizer.getMaxEvaluations()).thenReturn(maxEvaluations);
        assertEquals(maxEvaluations, optimizer.getMaxEvaluations());
        verify(optimizer, times(1)).getMaxEvaluations();
    }

    @Test
    public void testGetEvaluations() {
        when(optimizer.getEvaluations()).thenReturn(5);
        assertEquals(5, optimizer.getEvaluations());
        verify(optimizer, times(1)).getEvaluations();
    }

    @Test
    public void testGetConvergenceChecker() {
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
        verify(optimizer, times(1)).getConvergenceChecker();
    }

    @Test(expected = IllegalStateException.class)
    public void testGetConvergenceCheckerBeforeInitialization() {
        when(optimizer.getConvergenceChecker()).thenThrow(new IllegalStateException("Not initialized"));
        optimizer.getConvergenceChecker();
    }

    @Test
    public void testGetMaxEvaluationsNegative() {
        when(optimizer.getMaxEvaluations()).thenReturn(-2);
        try {
            optimizer.getMaxEvaluations();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("-2 is less than 0", e.getMessage());
        }
        verify(optimizer, times(1)).getMaxEvaluations();
    }

    @Test
    public void testGetMaxEvaluationsNull() {
        when(optimizer.getMaxEvaluations()).thenReturn(null);
        try {
            optimizer.getMaxEvaluations();
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
        verify(optimizer, times(1)).getMaxEvaluations();
    }

    @Test
    public void testGetEvaluationsNegative() {
        when(optimizer.getEvaluations()).thenReturn(-2);
        try {
            optimizer.getEvaluations();
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            assertEquals("-2 is less than 0", e.getMessage());
        }
        verify(optimizer, times(1)).getEvaluations();
    }

    @Test
    public void testGetEvaluationsNull() {
        when(optimizer.getEvaluations()).thenReturn(null);
        try {
            optimizer.getEvaluations();
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
        verify(optimizer, times(1)).getEvaluations();
    }

    @Test
    public void testGetConvergenceCheckerNull() {
        when(optimizer.getConvergenceChecker()).thenReturn(null);
        try {
            optimizer.getConvergenceChecker();
            fail("NullPointerException expected");
        } catch (NullPointerException e) {
            // expected
        }
        verify(optimizer, times(1)).getConvergenceChecker();
    }

    @Test
    public void testGetConvergenceCheckerNotInitialized() {
        when(optimizer.getConvergenceChecker()).thenThrow(new IllegalStateException("Not initialized"));
        try {
            optimizer.getConvergenceChecker();
            fail("IllegalStateException expected");
        } catch (IllegalStateException e) {
            // expected
        }
        verify(optimizer, times(1)).getConvergenceChecker();
    }
}
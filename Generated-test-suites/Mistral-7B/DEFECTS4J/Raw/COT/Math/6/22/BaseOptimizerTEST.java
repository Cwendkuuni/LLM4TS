package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.PointValuePair;

/**
 * Test class for {@link BaseOptimizer} interface.
 *
 * @author John Doe
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(50);
        assertEquals(50, optimizer.getEvaluations());
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<PointValuePair> convergenceChecker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()} with negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMaxEvaluationsNegative() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenThrow(new IllegalArgumentException());
        optimizer.getMaxEvaluations();
    }

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()} with null return value.
     */
    @Test(expected = NullPointerException.class)
    public void testGetMaxEvaluationsNull() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(null);
        optimizer.getMaxEvaluations();
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()} with negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetEvaluationsNegative() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenThrow(new IllegalArgumentException());
        optimizer.getEvaluations();
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()} with null return value.
     */
    @Test(expected = NullPointerException.class)
    public void testGetEvaluationsNull() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(null);
        optimizer.getEvaluations();
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()} with null return value.
     */
    @Test(expected = NullPointerException.class)
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer<PointValuePair> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getConvergenceChecker()).thenReturn(null);
        optimizer.getConvergenceChecker();
    }
}
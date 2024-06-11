package org.apache.commons.math3.optimization.test;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for BaseOptimizer interface.
 */
public class BaseOptimizerTest {

    /**
     * Test method for {@link BaseOptimizer#getMaxEvaluations()}.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = (BaseOptimizer<Object>) mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(10);
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    /**
     * Test method for {@link BaseOptimizer#getEvaluations()}.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<Object> convergenceChecker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
        when(convergenceChecker.isConverged(any(), anyInt())).thenReturn(true);

        optimizer.optimize(new PointValuePair(new Point(1.0), 2.0));

        verify(optimizer, times(1)).getEvaluations();
        assertEquals(1, optimizer.getEvaluations());
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()}.
     */
    @Test
    public void testGetConvergenceCheckerNullChecker() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        assertNull(optimizer.getConvergenceChecker());
    }

    /**
     * Test method for {@link BaseOptimizer#getConvergenceChecker()} with non-null checker.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<Object> convergenceChecker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }
}
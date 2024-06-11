package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;

/**
 * Test cases for {@link BaseOptimizer} interface.
 *
 * @since 3.0
 */
public class BaseOptimizerTest {

    /**
     * Test getMaxEvaluations method.
     */
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    /**
     * Test getEvaluations method.
     */
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(3);
        assertEquals(3, optimizer.getEvaluations());
    }

    /**
     * Test getConvergenceChecker method.
     */
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<Object> convergenceChecker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    /**
     * Test getMaxEvaluations method with negative value.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetMaxEvaluationsNegative() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenThrow(new IllegalArgumentException("Maximum number of evaluations cannot be negative"));
        optimizer.getMaxEvaluations();
    }

    /**
     * Test getEvaluations method with get before optimize.
     */
    @Test
    public void testGetEvaluationsBeforeOptimize() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(0);
        assertFalse(optimizer.isOptimized());
        optimizer.optimize(new MaxEval(10));
        assertEquals(1, optimizer.getEvaluations());
    }

    /**
     * Test getEvaluations method with optimize with given evaluations.
     */
    @Test
    public void testGetEvaluationsOptimizeWithEvaluations() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(5);
        optimizer.optimize(new MaxEval(10));
        assertEquals(10, optimizer.getEvaluations());
    }
}
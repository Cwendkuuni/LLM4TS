package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.MaxEval;

import org.junit.Test;

public class BaseOptimizerTest<PAIR> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        MaxEval maxEval = new MaxEval(100);
        when(optimizer.getConvergenceChecker()).thenReturn(maxEval);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(5);
        assertEquals(5, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        assertNull(optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceCheckerInstance() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<PAIR> convergenceChecker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }
}
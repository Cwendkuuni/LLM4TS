package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.Test;

public class BaseOptimizerTest<PAIR> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<PAIR> convergenceChecker = mock(ConvergenceChecker.class);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }
}
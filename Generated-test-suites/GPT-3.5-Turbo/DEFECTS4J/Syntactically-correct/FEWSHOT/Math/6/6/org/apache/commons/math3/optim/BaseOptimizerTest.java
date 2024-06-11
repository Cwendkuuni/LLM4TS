package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer<>();
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer<>();
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizer<>();
        assertEquals(null, optimizer.getConvergenceChecker());
    }
}
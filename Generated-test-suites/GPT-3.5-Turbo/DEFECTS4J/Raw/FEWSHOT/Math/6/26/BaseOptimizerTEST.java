package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<?> optimizer = new BaseOptimizerImpl<>();
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<?> optimizer = new BaseOptimizerImpl<>();
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<?> optimizer = new BaseOptimizerImpl<>();
        ConvergenceChecker<?> convergenceChecker = optimizer.getConvergenceChecker();
        assertEquals(null, convergenceChecker);
    }
}

class BaseOptimizerImpl<PAIR> implements BaseOptimizer<PAIR> {
    @Override
    public int getMaxEvaluations() {
        return 0;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public ConvergenceChecker<PAIR> getConvergenceChecker() {
        return null;
    }
}
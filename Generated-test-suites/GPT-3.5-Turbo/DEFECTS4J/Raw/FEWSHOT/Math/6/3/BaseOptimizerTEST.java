package org.apache.commons.math3.optimization;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvaluations);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        int evaluations = optimizer.getEvaluations();
        assertEquals(0, evaluations);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        ConvergenceChecker checker = optimizer.getConvergenceChecker();
        assertNotNull(checker);
    }
}

class BaseOptimizerImpl implements BaseOptimizer<Object> {
    @Override
    public int getMaxEvaluations() {
        return 0;
    }

    @Override
    public int getEvaluations() {
        return 0;
    }

    @Override
    public ConvergenceChecker<Object> getConvergenceChecker() {
        return new ConvergenceCheckerImpl();
    }
}

class ConvergenceCheckerImpl implements ConvergenceChecker<Object> {
    @Override
    public boolean converged(int iteration, Object previous, Object current) {
        return false;
    }
}
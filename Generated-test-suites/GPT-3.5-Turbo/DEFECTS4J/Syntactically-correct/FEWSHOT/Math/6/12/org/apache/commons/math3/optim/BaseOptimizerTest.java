package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        assertEquals(0, optimizer.getEvaluations());

        optimizer.optimize();
        assertEquals(1, optimizer.getEvaluations());

        optimizer.optimize();
        assertEquals(2, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizerImpl();
        assertNotNull(optimizer.getConvergenceChecker());
    }

    private class BaseOptimizerImpl implements BaseOptimizer<Object> {
        @Override
        public int getMaxEvaluations() {
            return 100;
        }

        @Override
        public int getEvaluations() {
            return 0;
        }

        @Override
        public ConvergenceChecker<Object> getConvergenceChecker() {
            return null;
        }
    }
}
package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {
    private BaseOptimizer<Object> optimizer;
    private ConvergenceChecker<Object> convergenceChecker;

    @Before
    public void setUp() {
        convergenceChecker = mock(ConvergenceChecker.class);
        optimizer = new BaseOptimizerImpl<>(convergenceChecker);
    }

    @Test
    public void testGetMaxEvaluations() {
        assertEquals(0, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    private class BaseOptimizerImpl<PAIR> implements BaseOptimizer<PAIR> {
        private ConvergenceChecker<PAIR> convergenceChecker;

        public BaseOptimizerImpl(ConvergenceChecker<PAIR> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }

        public int getMaxEvaluations() {
            return 0;
        }

        public int getEvaluations() {
            return 0;
        }

        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return convergenceChecker;
        }
    }
}
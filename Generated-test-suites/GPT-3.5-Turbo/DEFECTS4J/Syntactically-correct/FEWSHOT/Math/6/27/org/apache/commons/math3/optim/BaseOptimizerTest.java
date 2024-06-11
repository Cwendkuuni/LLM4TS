package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

public class BaseOptimizerTest {

    private BaseOptimizer<PAIR> optimizer;

    @Before
    public void setUp() {
        optimizer = new BaseOptimizerImpl<>();
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
        assertNotNull(optimizer.getConvergenceChecker());
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
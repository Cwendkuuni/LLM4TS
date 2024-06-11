package org.apache.commons.math3.optimization;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<String> optimizer = new BaseOptimizerImpl<>();
        assertEquals(10, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<String> optimizer = new BaseOptimizerImpl<>();
        assertEquals(0, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<String> optimizer = new BaseOptimizerImpl<>();
        assertNotNull(optimizer.getConvergenceChecker());
    }
}
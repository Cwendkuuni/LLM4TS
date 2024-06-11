package org.apache.commons.math3.optimization;

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        int maxEvals = optimizer.getMaxEvaluations();
        assertEquals(0, maxEvals);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer optimizer = new BaseOptimizer();
        int evals = optimizer.getEvaluations();
        assertEquals(0, evals);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer optimizer = new BaseOptimizer();
        ConvergenceChecker checker = optimizer.getConvergenceChecker();
        assertNull(checker);
    }
}
package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;

public class BaseOptimizerTest<PAIR extends PointValuePair> {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        assertEquals(MaxEval.UNLIMITED, optimizer.getMaxEvaluations());
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
        optimizer.setConvergenceChecker(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer<PAIR> optimizer = mock(BaseOptimizer.class);
        assertNull(optimizer.getConvergenceChecker());
    }
}
package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Test;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        assertEquals(Integer.MAX_VALUE, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        assertEquals(0, optimizer.getEvaluations());

        // Simulate a call to optimize()
        optimizer.optimize(new Object[]{"x"}, new double[]{0.1});
        assertEquals(1, optimizer.getEvaluations());

        optimizer.optimize(new Object[]{"x"}, new double[]{0.2, 0.3});
        assertEquals(3, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<Object> convergenceChecker = mock(ConvergenceChecker.class);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer<Object> optimizer = mock(BaseOptimizer.class);
        assertNull(optimizer.getConvergenceChecker());
    }
}
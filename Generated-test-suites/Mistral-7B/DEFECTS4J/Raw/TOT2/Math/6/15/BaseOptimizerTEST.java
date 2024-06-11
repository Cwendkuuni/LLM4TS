package org.apache.commons.math3.optimization.test;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.AbstractValuePoint;
import org.junit.Test;
import org.mockito.Mockito;

public class BaseOptimizerTest {

    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<AbstractValuePoint> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenReturn(100);
        int maxEvaluations = optimizer.getMaxEvaluations();
        assertThat(maxEvaluations).isEqualTo(100);
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<AbstractValuePoint> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenReturn(5);
        int evaluations = optimizer.getEvaluations();
        assertThat(evaluations).isEqualTo(5);
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<AbstractValuePoint> optimizer = mock(BaseOptimizer.class);
        ConvergenceChecker<AbstractValuePoint> convergenceChecker = mock(ConvergenceChecker.class);
        when(optimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
        assertSame(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetMaxEvaluationsNegative() {
        BaseOptimizer<AbstractValuePoint> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getMaxEvaluations()).thenThrow(new IllegalArgumentException("Max evaluations should be non-negative"));
        int maxEvaluations = optimizer.getMaxEvaluations();
        fail("Max evaluations should not be retrieved in this case");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetEvaluationsNegative() {
        BaseOptimizer<AbstractValuePoint> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getEvaluations()).thenThrow(new IllegalArgumentException("Evaluations should be non-negative"));
        int evaluations = optimizer.getEvaluations();
        fail("Evaluations should not be retrieved in this case");
    }

    @Test(expected = NullPointerException.class)
    public void testGetConvergenceCheckerNull() {
        BaseOptimizer<AbstractValuePoint> optimizer = mock(BaseOptimizer.class);
        when(optimizer.getConvergenceChecker()).thenReturn(null);
        assertNull(optimizer.getConvergenceChecker());
    }
}
package org.apache.commons.math3.optimization.base;

import org.apache.commons.math3.optimization.BaseOptimizer;
import org.apache.commons.math3.optimization.MaxEval;
import org.apache.commons.math3.optimization.convergence.ConvergenceChecker;
import org.apache.commons.math3.optimization.convergence.SimpleValueChecker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(org.junit.runner.JUnit4.class)
public class BaseOptimizerTest {

    @Mock
    private ConvergenceChecker<Object[]> convergenceChecker;

    @InjectMocks
    private BaseOptimizer<Object[]> baseOptimizer;

    @Test
    public void testGetMaxEvaluations() {
        when(baseOptimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
        when(convergenceChecker.isConverged(baseOptimizer, new Object[0][], 0)).thenReturn(false);

        assertEquals(0, baseOptimizer.getMaxEvaluations());

        BaseOptimizer<Object[]> optimizer = new BaseOptimizer<Object[]>() {
            @Override
            public int getMaxEvaluations() {
                return 10;
            }

            @Override
            public ConvergenceChecker<Object[]> getConvergenceChecker() {
                return convergenceChecker;
            }
        };

        when(convergenceChecker.isConverged(optimizer, new Object[0][], 0)).thenReturn(true);

        when(baseOptimizer.getMaxEvaluations()).thenCallRealMethod();

        assertEquals(10, baseOptimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        when(baseOptimizer.getConvergenceChecker()).thenReturn(convergenceChecker);
        when(convergenceChecker.isConverged(baseOptimizer, new Object[0][], 0)).thenReturn(false);

        assertEquals(0, baseOptimizer.getEvaluations());

        BaseOptimizer<Object[]> optimizer = new BaseOptimizer<Object[]>() {
            @Override
            public int getMaxEvaluations() {
                return 10;
            }

            @Override
            public int getEvaluations() {
                return 5;
            }

            @Override
            public ConvergenceChecker<Object[]> getConvergenceChecker() {
                return convergenceChecker;
            }
        };

        when(convergenceChecker.isConverged(optimizer, new Object[0][], 0)).thenReturn(true);

        when(baseOptimizer.getEvaluations()).thenCallRealMethod();

        assertEquals(5, baseOptimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Object[]> optimizer = new BaseOptimizer<Object[]>() {
            @Override
            public ConvergenceChecker<Object[]> getConvergenceChecker() {
                return convergenceChecker;
            }
        };

        when(convergenceChecker.isConverged(optimizer, new Object[0][], 0)).thenReturn(false);

        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    @Test
    public void testConvergenceCheckerIsCalled() {
        BaseOptimizer<Object[]> optimizer = new BaseOptimizer<Object[]>() {
            @Override
            public ConvergenceChecker<Object[]> getConvergenceChecker() {
                return new SimpleValueChecker<Object[]>(1e-6);
            }
        };

        Object[] point = new Object[2];
        double[] objective = new double[1];
        double[] gradient = new double[2];

        when(convergenceChecker.isConverged(optimizer, new Object[][]{point}, 0)).thenReturn(false);

        optimizer.optimize(new MaxEval(10), point, objective);

        verify(convergenceChecker).isConverged(optimizer, new Object[][]{point}, 0);
    }
}
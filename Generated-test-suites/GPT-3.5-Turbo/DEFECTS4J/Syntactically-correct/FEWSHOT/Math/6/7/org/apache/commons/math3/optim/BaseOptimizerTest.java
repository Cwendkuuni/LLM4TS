package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizer<PAIR>() {
            @Override
            public int getMaxEvaluations() {
                return 100;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return null;
            }
        };

        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizer<PAIR>() {
            @Override
            public int getMaxEvaluations() {
                return 0;
            }

            @Override
            public int getEvaluations() {
                return 10;
            }

            @Override
            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return null;
            }
        };

        assertEquals(10, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<PAIR> optimizer = new BaseOptimizer<PAIR>() {
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
                return new ConvergenceChecker<PAIR>() {
                    @Override
                    public boolean converged(int iteration, PAIR previous, PAIR current) {
                        return false;
                    }
                };
            }
        };

        assertEquals(false, optimizer.getConvergenceChecker().converged(0, null, null));
    }
}
package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizer<Integer>() {
            public int getMaxEvaluations() {
                return 10;
            }

            public int getEvaluations() {
                return 0;
            }

            public ConvergenceChecker<Integer> getConvergenceChecker() {
                return null;
            }
        };
        
        assertEquals(10, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizer<Integer>() {
            public int getMaxEvaluations() {
                return 0;
            }

            public int getEvaluations() {
                return 5;
            }

            public ConvergenceChecker<Integer> getConvergenceChecker() {
                return null;
            }
        };
        
        assertEquals(5, optimizer.getEvaluations());
    }
    
    @Test
    public void testGetConvergenceChecker() {
        BaseOptimizer<Integer> optimizer = new BaseOptimizer<Integer>() {
            public int getMaxEvaluations() {
                return 0;
            }

            public int getEvaluations() {
                return 0;
            }

            public ConvergenceChecker<Integer> getConvergenceChecker() {
                return new ConvergenceChecker<Integer>() {
                    public boolean converged(Integer point1, Integer point2) {
                        return point1.equals(point2);
                    }
                };
            }
        };
        
        assertEquals(true, optimizer.getConvergenceChecker().converged(1, 1));
        assertEquals(false, optimizer.getConvergenceChecker().converged(1, 2));
    }
}
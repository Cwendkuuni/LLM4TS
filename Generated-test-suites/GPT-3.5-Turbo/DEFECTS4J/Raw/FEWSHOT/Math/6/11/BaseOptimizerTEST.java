package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        // Create BaseOptimizer instance
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            @Override
            public int getMaxEvaluations() {
                return 100;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return null;
            }
        };
        
        // Test getMaxEvaluations
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        // Create BaseOptimizer instance
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            @Override
            public int getMaxEvaluations() {
                return 0;
            }

            @Override
            public int getEvaluations() {
                return 50;
            }

            @Override
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return null;
            }
        };
        
        // Test getEvaluations
        assertEquals(50, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        // Create BaseOptimizer instance
        ConvergenceChecker<Object> convergenceChecker = new ConvergenceChecker<Object>() {
            @Override
            public boolean converged(int iteration, Object previous, Object current) {
                return true;
            }
        };
        BaseOptimizer<Object> optimizer = new BaseOptimizer<Object>() {
            @Override
            public int getMaxEvaluations() {
                return 0;
            }

            @Override
            public int getEvaluations() {
                return 0;
            }

            @Override
            public ConvergenceChecker<Object> getConvergenceChecker() {
                return convergenceChecker;
            }
        };
        
        // Test getConvergenceChecker
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }
}
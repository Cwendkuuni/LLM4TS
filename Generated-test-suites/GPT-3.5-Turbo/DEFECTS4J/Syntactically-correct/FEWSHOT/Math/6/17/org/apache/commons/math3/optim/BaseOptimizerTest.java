package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        // create test object
        BaseOptimizer<PAIR> optimizer = new BaseOptimizer<PAIR>() {
            public int getMaxEvaluations() {
                return 100;
            }

            public int getEvaluations() {
                return 0;
            }

            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return null;
            }
        };

        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        // create test object
        BaseOptimizer<PAIR> optimizer = new BaseOptimizer<PAIR>() {
            public int getMaxEvaluations() {
                return 0;
            }

            public int getEvaluations() {
                return 10;
            }

            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return null;
            }
        };

        assertEquals(10, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        // create test object
        ConvergenceChecker<PAIR> checker = new ConvergenceChecker<PAIR>() {
            public boolean isConverged(int iteration, PAIR previous, PAIR current) {
                return false;
            }
        };

        BaseOptimizer<PAIR> optimizer = new BaseOptimizer<PAIR>() {
            public int getMaxEvaluations() {
                return 0;
            }

            public int getEvaluations() {
                return 0;
            }

            public ConvergenceChecker<PAIR> getConvergenceChecker() {
                return checker;
            }
        };

        assertEquals(checker, optimizer.getConvergenceChecker());
    }
}
package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        // Create instance of BaseOptimizer
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl();

        // Test with default value
        assertEquals(0, optimizer.getMaxEvaluations());

        // Set new value and test
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
    }

    @Test
    public void testGetEvaluations() {
        // Create instance of BaseOptimizer
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl();

        // Test with default value
        assertEquals(0, optimizer.getEvaluations());

        // Call optimize method and test
        optimizer.optimize();
        assertEquals(1, optimizer.getEvaluations());
    }

    @Test
    public void testGetConvergenceChecker() {
        // Create instance of BaseOptimizer
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl();

        // Test with default value
        assertNull(optimizer.getConvergenceChecker());

        // Set new convergence checker and test
        ConvergenceChecker<PAIR> convergenceChecker = new ConvergenceCheckerImpl();
        optimizer.setConvergenceChecker(convergenceChecker);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }

    private class BaseOptimizerImpl implements BaseOptimizer<PAIR> {
        private int maxEvaluations;
        private int evaluations;
        private ConvergenceChecker<PAIR> convergenceChecker;

        public int getMaxEvaluations() {
            return maxEvaluations;
        }

        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }

        public int getEvaluations() {
            return evaluations;
        }

        public void optimize() {
            evaluations++;
        }

        public ConvergenceChecker<PAIR> getConvergenceChecker() {
            return convergenceChecker;
        }

        public void setConvergenceChecker(ConvergenceChecker<PAIR> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }
    }

    private class ConvergenceCheckerImpl implements ConvergenceChecker<PAIR> {
        public boolean converged(PAIR previous, PAIR current) {
            // Some implementation
        }
    }
}
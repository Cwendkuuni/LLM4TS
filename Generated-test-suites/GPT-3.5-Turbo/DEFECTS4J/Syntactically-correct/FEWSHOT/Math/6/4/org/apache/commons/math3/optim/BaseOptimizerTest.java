package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BaseOptimizerTest {
    
    @Test
    public void testGetMaxEvaluations() {
        int expected = 100;
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<PAIR>();
        optimizer.setMaxEvaluations(expected);
        int actual = optimizer.getMaxEvaluations();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetEvaluations() {
        int expected = 50;
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<PAIR>();
        optimizer.setEvaluations(expected);
        int actual = optimizer.getEvaluations();
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetConvergenceChecker() {
        ConvergenceChecker<PAIR> expected = new ConvergenceCheckerImpl<PAIR>();
        BaseOptimizer<PAIR> optimizer = new BaseOptimizerImpl<PAIR>();
        optimizer.setConvergenceChecker(expected);
        ConvergenceChecker<PAIR> actual = optimizer.getConvergenceChecker();
        assertEquals(expected, actual);
    }
    
    private class BaseOptimizerImpl<T> implements BaseOptimizer<T> {
        private int maxEvaluations;
        private int evaluations;
        private ConvergenceChecker<T> convergenceChecker;
        
        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }
        
        public int getMaxEvaluations() {
            return maxEvaluations;
        }
        
        public void setEvaluations(int evaluations) {
            this.evaluations = evaluations;
        }
        
        public int getEvaluations() {
            return evaluations;
        }
        
        public void setConvergenceChecker(ConvergenceChecker<T> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }
        
        public ConvergenceChecker<T> getConvergenceChecker() {
            return convergenceChecker;
        }
    }
    
    private class ConvergenceCheckerImpl<T> implements ConvergenceChecker<T> {
        public boolean converged(T point1, T point2) {
            // implementation
        }
    }
}
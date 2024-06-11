package org.apache.commons.math3.optimization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class BaseOptimizerTest {
    @Test
    public void testGetMaxEvaluations() {
        // Create a dummy implementation of BaseOptimizer
        BaseOptimizerImpl optimizer = new BaseOptimizerImpl();
        
        // Test with a valid max evaluations value
        optimizer.setMaxEvaluations(100);
        assertEquals(100, optimizer.getMaxEvaluations());
        
        // Test with a negative max evaluations value
        optimizer.setMaxEvaluations(-100);
        assertEquals(-100, optimizer.getMaxEvaluations());
    }
    
    @Test
    public void testGetEvaluations() {
        // Create a dummy implementation of BaseOptimizer
        BaseOptimizerImpl optimizer = new BaseOptimizerImpl();
        
        // Test with no evaluations
        assertEquals(0, optimizer.getEvaluations());
        
        // Test with some evaluations
        optimizer.incrementEvaluations();
        optimizer.incrementEvaluations();
        assertEquals(2, optimizer.getEvaluations());
    }
    
    @Test
    public void testGetConvergenceChecker() {
        // Create a dummy implementation of BaseOptimizer
        BaseOptimizerImpl optimizer = new BaseOptimizerImpl();
        
        // Test with a valid convergence checker
        ConvergenceCheckerImpl<Object> checker = new ConvergenceCheckerImpl<Object>();
        optimizer.setConvergenceChecker(checker);
        assertEquals(checker, optimizer.getConvergenceChecker());
        
        // Test with a null convergence checker
        optimizer.setConvergenceChecker(null);
        assertNull(optimizer.getConvergenceChecker());
    }
    
    private class BaseOptimizerImpl implements BaseOptimizer<Object> {
        private int maxEvaluations;
        private int evaluations;
        private ConvergenceChecker<Object> convergenceChecker;
        
        public void setMaxEvaluations(int maxEvaluations) {
            this.maxEvaluations = maxEvaluations;
        }
        
        public int getMaxEvaluations() {
            return maxEvaluations;
        }
        
        public void incrementEvaluations() {
            evaluations++;
        }
        
        public int getEvaluations() {
            return evaluations;
        }
        
        public void setConvergenceChecker(ConvergenceChecker<Object> convergenceChecker) {
            this.convergenceChecker = convergenceChecker;
        }
        
        public ConvergenceChecker<Object> getConvergenceChecker() {
            return convergenceChecker;
        }
    }
    
    private class ConvergenceCheckerImpl<T> implements ConvergenceChecker<T> {
        public boolean converged(int iteration, T previous, T current) {
            return false;
        }
    }
}
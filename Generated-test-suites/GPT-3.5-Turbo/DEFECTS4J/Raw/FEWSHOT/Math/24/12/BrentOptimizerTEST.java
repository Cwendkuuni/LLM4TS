package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testBrentOptimizer() {
        double rel = 0.001;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        assertEquals(rel, optimizer.relativeThreshold, 0.0);
        assertEquals(abs, optimizer.absoluteThreshold, 0.0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithInvalidAbs() {
        double rel = 0.001;
        double abs = -0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerWithInvalidRel() {
        double rel = 0.0000001;
        double abs = 0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithInvalidAbsAndRel() {
        double rel = 0.0000001;
        double abs = -0.01;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
    }
    
    @Test
    public void testDoOptimize() {
        double rel = 0.001;
        double abs = 0.01;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return true;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNull(result);
    }
}
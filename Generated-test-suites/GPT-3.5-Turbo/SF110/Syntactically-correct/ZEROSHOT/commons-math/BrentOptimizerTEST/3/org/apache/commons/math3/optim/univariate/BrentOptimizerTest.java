package org.apache.commons.math3.optim.univariate;

import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        double rel = 0.01;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
    }
    
    @Test
    public void testBest() {
        double aValue = 1.0;
        double bValue = 2.0;
        boolean isMinim = true;
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(aValue, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(bValue, 0.0);
        
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.0, null);
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        
        assertNotNull(result);
        assertEquals(isMinim ? a : b, result);
    }
}
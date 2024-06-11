package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class BrentOptimizerTest {
    
    @Test
    public void testBrentOptimizer() {
        // Test constructor
        BrentOptimizer brentOptimizer = new BrentOptimizer(1e-6, 1e-12);
        assertEquals(1e-6, brentOptimizer.relativeThreshold, 0.0);
        assertEquals(1e-12, brentOptimizer.absoluteThreshold, 0.0);
        assertNull(brentOptimizer.getConvergenceChecker());

        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return false;
            }
        };
        BrentOptimizer brentOptimizerWithChecker = new BrentOptimizer(1e-6, 1e-12, checker);
        assertEquals(1e-6, brentOptimizerWithChecker.relativeThreshold, 0.0);
        assertEquals(1e-12, brentOptimizerWithChecker.absoluteThreshold, 0.0);
        assertEquals(checker, brentOptimizerWithChecker.getConvergenceChecker());
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerWithSmallRel() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(1e-20, 1e-12);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerWithZeroAbs() {
        BrentOptimizer brentOptimizer = new BrentOptimizer(1e-6, 0);
    }
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-12);
        
        // Test optimization with the minimum value
        UnivariatePointValuePair result1 = optimizer.optimize(100, new ObjectiveFunction(), GoalType.MINIMIZE, 2, 5);
        assertEquals(2.0, result1.getPoint(), 0.0);
        assertEquals(4.0, result1.getValue(), 0.0);
        
        // Test optimization with the maximum value
        UnivariatePointValuePair result2 = optimizer.optimize(100, new ObjectiveFunction(), GoalType.MAXIMIZE, 2, 5);
        assertEquals(5.0, result2.getPoint(), 0.0);
        assertEquals(25.0, result2.getValue(), 0.0);
    }
    
    public class ObjectiveFunction extends AbstractUnivariateFunction {
        @Override
        public double value(double x) {
            return x * x;
        }
    }
}
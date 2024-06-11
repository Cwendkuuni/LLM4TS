package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.junit.Test;

public class BrentOptimizerTest {
    @Test
    public void testDoOptimize() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return iteration >= 100;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1, checker);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 1e-5);
    }
    
    @Test
    public void testDoOptimize_stopCondition() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 1e-5);
    }
    
    @Test
    public void testDoOptimize_nullConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 1e-5);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 10);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 20);
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertEquals(a, result);
    }
    
    @Test
    public void testBest_nullValues() {
        BrentOptimizer optimizer = new BrentOptimizer(0.1, 0.1);
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = null;
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        assertNull(result);
    }
}
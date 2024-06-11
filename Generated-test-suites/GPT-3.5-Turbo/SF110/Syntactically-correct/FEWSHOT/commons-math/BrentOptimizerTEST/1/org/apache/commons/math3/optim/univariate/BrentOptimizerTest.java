package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        // Test case 1
        BrentOptimizer optimizer1 = new BrentOptimizer(0.1, 1e-6);
        optimizer1.setGoalType(GoalType.MINIMIZE);
        optimizer1.setMin(0);
        optimizer1.setStartValue(0.5);
        optimizer1.setMax(1);
        UnivariatePointValuePair result1 = optimizer1.doOptimize();
        assertEquals(0, result1.getPoint(), 1e-6);
        
        // Test case 2
        BrentOptimizer optimizer2 = new BrentOptimizer(0.1, 1e-6);
        optimizer2.setGoalType(GoalType.MINIMIZE);
        optimizer2.setMin(-10);
        optimizer2.setStartValue(-5);
        optimizer2.setMax(0);
        UnivariatePointValuePair result2 = optimizer2.doOptimize();
        assertEquals(-10, result2.getPoint(), 1e-6);
        
        // Test case 3
        BrentOptimizer optimizer3 = new BrentOptimizer(0.1, 1e-6);
        optimizer3.setGoalType(GoalType.MAXIMIZE);
        optimizer3.setMin(-10);
        optimizer3.setStartValue(-5);
        optimizer3.setMax(0);
        UnivariatePointValuePair result3 = optimizer3.doOptimize();
        assertEquals(0, result3.getPoint(), 1e-6);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRel() {
        new BrentOptimizer(-0.1, 1e-6);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbs() {
        new BrentOptimizer(0.1, 0);
    }
}
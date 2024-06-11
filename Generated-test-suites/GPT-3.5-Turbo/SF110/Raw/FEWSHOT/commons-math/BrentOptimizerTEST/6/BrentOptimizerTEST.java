package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    @Test
    public void testBrentOptimizer() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.5);
        
        assertNotNull(optimizer);
        assertTrue(optimizer instanceof UnivariateOptimizer);
    }
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.5);
        
        optimizer.setMin(0.0);
        optimizer.setStartValue(1.0);
        optimizer.setMax(2.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertTrue(result instanceof UnivariatePointValuePair);
        
        double x = result.getPoint();
        double fx = result.getValue();
        
        assertEquals(1.0, x, 0.0001);
        assertEquals(0.0, fx, 0.0001);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerRelativeThresholdException() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.5);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerAbsoluteThresholdException() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, -0.5);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 0.5);
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.0);
        
        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        UnivariatePointValuePair result2 = optimizer.best(a, b, false);
        
        assertNotNull(result1);
        assertNotNull(result2);
        
        assertEquals(a, result1);
        assertEquals(b, result2);
    }
}
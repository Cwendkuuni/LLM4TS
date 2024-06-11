package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_relTooSmall() {
        new BrentOptimizer(-1, 0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_absNotPositive() {
        new BrentOptimizer(0, -1);
    }
    
    @Test
    public void testDoOptimize() {
        // create an instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        // perform optimization
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        // verify the result
        double expectedValue = 0.0;
        double expectedPoint = 0.0;
        assertEquals(expectedValue, result.getValue(), 1e-10);
        assertEquals(expectedPoint, result.getPoint(), 1e-10);
    }
    
    @Test
    public void testBest() {
        // create an instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);
        
        // create two UnivariatePointValuePair objects
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.5, 5.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(0.75, 10.0);
        
        // test the best method
        UnivariatePointValuePair best = optimizer.best(a, b, true);
        
        assertEquals(a, best);
    }
}
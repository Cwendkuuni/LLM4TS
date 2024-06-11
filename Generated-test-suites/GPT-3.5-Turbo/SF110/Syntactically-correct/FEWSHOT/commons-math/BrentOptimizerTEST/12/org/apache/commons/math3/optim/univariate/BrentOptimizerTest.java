package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.PointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructor() {
        double rel = 0.0001;
        double abs = 0.001;
        ConvergenceChecker<PointValuePair> checker = null;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        assertNotNull(optimizer);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorRelativeToleranceTooSmall() {
        double rel = -0.0001;
        double abs = 0.001;
        ConvergenceChecker<PointValuePair> checker = null;
        
        new BrentOptimizer(rel, abs, checker);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorAbsoluteToleranceZero() {
        double rel = 0.0001;
        double abs = 0.0;
        ConvergenceChecker<PointValuePair> checker = null;
        
        new BrentOptimizer(rel, abs, checker);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorAbsoluteToleranceNegative() {
        double rel = 0.0001;
        double abs = -0.001;
        ConvergenceChecker<PointValuePair> checker = null;
        
        new BrentOptimizer(rel, abs, checker);
    }
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);
        
        // Set up input
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setStartValue(1.0);
        optimizer.setMax(10.0);
        
        PointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
    }
    
    @Test
    public void testDoOptimizeMaxValue() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, 0.001);
        
        // Set up input
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMin(0.0);
        optimizer.setStartValue(1.0);
        optimizer.setMax(Double.MAX_VALUE);
        
        PointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
    }
}
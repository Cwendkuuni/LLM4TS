package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.*;
import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.nonlinear.scalar.*;
import org.apache.commons.math3.util.*;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructor_ThrowsNumberIsTooSmallException() {
        try {
            new BrentOptimizer(0.0, 1.0);
            Assert.fail("Expected NumberIsTooSmallException to be thrown");
        } catch (NumberIsTooSmallException e) {
            Assert.assertEquals(0.0, e.getArgument(), 0.0);
            Assert.assertEquals(BrentOptimizer.MIN_RELATIVE_TOLERANCE, e.getBound(), 0.0);
            Assert.assertTrue(e.goesAbove());
        }
        
        try {
            new BrentOptimizer(-1.0, 1.0);
            Assert.fail("Expected NumberIsTooSmallException to be thrown");
        } catch (NumberIsTooSmallException e) {
            Assert.assertEquals(-1.0, e.getArgument(), 0.0);
            Assert.assertEquals(BrentOptimizer.MIN_RELATIVE_TOLERANCE, e.getBound(), 0.0);
            Assert.assertTrue(e.goesAbove());
        }
    }
    
    @Test
    public void testConstructor_ThrowsNotStrictlyPositiveException() {
        try {
            new BrentOptimizer(1.0, 0.0);
            Assert.fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals(0.0, e.getArgument(), 0.0);
        }
        
        try {
            new BrentOptimizer(1.0, -1.0);
            Assert.fail("Expected NotStrictlyPositiveException to be thrown");
        } catch (NotStrictlyPositiveException e) {
            Assert.assertEquals(-1.0, e.getArgument(), 0.0);
        }
    }
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setMax(2.0);
        optimizer.setStartValue(1.0);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        Assert.assertEquals(1.0, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }
    
    @Test
    public void testDoOptimize_ConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0, new SimpleValueChecker());
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setMax(2.0);
        optimizer.setStartValue(1.0);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        Assert.assertEquals(1.0, result.getPoint(), 0.0);
        Assert.assertEquals(0.0, result.getValue(), 0.0);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1.0, 1.0);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 1.0);
        
        UnivariatePointValuePair resultMinimize = optimizer.best(a, b, true);
        UnivariatePointValuePair resultMaximize = optimizer.best(a, b, false);
        
        Assert.assertEquals(a, resultMinimize);
        Assert.assertEquals(b, resultMaximize);
    }
}
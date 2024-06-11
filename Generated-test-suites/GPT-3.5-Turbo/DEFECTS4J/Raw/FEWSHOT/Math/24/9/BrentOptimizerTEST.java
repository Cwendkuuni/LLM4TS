package org.apache.commons.math3.optimization.univariate;

import org.apache.commons.math3.util.Precision;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;

import org.junit.Test;
import org.junit.Assert;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructor() {
        double rel = 1e-6;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        Assert.assertEquals(rel, optimizer.relativeThreshold, Double.MIN_VALUE);
        Assert.assertEquals(abs, optimizer.absoluteThreshold, Double.MIN_VALUE);
        Assert.assertNull(optimizer.getConvergenceChecker());
        
        rel = 1e-5;
        abs = 1e-5;
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return Precision.equals(previous.getValue(), current.getValue(), 1e-6);
            }
        };
        optimizer = new BrentOptimizer(rel, abs, checker);
        
        Assert.assertEquals(rel, optimizer.relativeThreshold, Double.MIN_VALUE);
        Assert.assertEquals(abs, optimizer.absoluteThreshold, Double.MIN_VALUE);
        Assert.assertEquals(checker, optimizer.getConvergenceChecker());
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorInvalidRel() {
        double rel = 1e-12;
        double abs = 1e-6;
        new BrentOptimizer(rel, abs);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorInvalidAbs() {
        double rel = 1e-6;
        double abs = -1e-6;
        new BrentOptimizer(rel, abs);
    }
    
    @Test
    public void testDoOptimize() {
        double rel = 1e-6;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setStartValue(1);
        optimizer.setMax(2);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        Assert.assertEquals(1, result.getPoint(), 1e-6);
        
        optimizer.setStartValue(2);
        result = optimizer.doOptimize();
        
        Assert.assertEquals(2, result.getPoint(), 1e-6);
    }
    
    @Test
    public void testBest() {
        double rel = 1e-6;
        double abs = 1e-6;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 2);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        Assert.assertEquals(a, result);
        
        result = optimizer.best(a, b, false);
        Assert.assertEquals(b, result);
        
        result = optimizer.best(null, null, true);
        Assert.assertNull(result);
    }
}
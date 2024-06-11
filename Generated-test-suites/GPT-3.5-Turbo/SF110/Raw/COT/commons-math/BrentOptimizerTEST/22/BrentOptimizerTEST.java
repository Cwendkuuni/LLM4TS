
package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariateOptimizer;
import org.apache.commons.math3.util.Precision;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructorWithInvalidRel() {
        try {
            new BrentOptimizer(1e-10, 0.1);
            Assert.fail("Expected NumberIsTooSmallException");
        } catch (NumberIsTooSmallException e) {
            // expected
        }
    }
    
    @Test
    public void testConstructorWithInvalidAbs() {
        try {
            new BrentOptimizer(0.001, -0.1);
            Assert.fail("Expected NotStrictlyPositiveException");
        } catch (NotStrictlyPositiveException e) {
            // expected
        }
    }
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1);
        optimizer.setMin(-1.0);
        optimizer.setMax(1.0);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        Assert.assertNotNull(result);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(-1.0, -0.5);
        
        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        UnivariatePointValuePair result2 = optimizer.best(a, b, false);
        
        Assert.assertEquals(a, result1);
        Assert.assertEquals(b, result2);
    }
    
    @Test
    public void testBestWithEqualValues() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(-1.0, 0.5);
        
        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        UnivariatePointValuePair result2 = optimizer.best(a, b, false);
        
        Assert.assertEquals(a, result1);
        Assert.assertEquals(b, result2);
    }
    
    @Test
    public void testBestWithNull() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        
        UnivariatePointValuePair result1 = optimizer.best(a, null, true);
        UnivariatePointValuePair result2 = optimizer.best(a, null, false);
        
        Assert.assertEquals(a, result1);
        Assert.assertEquals(null, result2);
    }
    
    @Test
    public void testBestWithBothNulls() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1);
        
        UnivariatePointValuePair result1 = optimizer.best(null, null, true);
        UnivariatePointValuePair result2 = optimizer.best(null, null, false);
        
        Assert.assertEquals(null, result1);
        Assert.assertEquals(null, result2);
    }
    
    @Test
    public void testBestWithNullAndValue() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        
        UnivariatePointValuePair result1 = optimizer.best(null, a, true);
        UnivariatePointValuePair result2 = optimizer.best(null, a, false);
        
        Assert.assertEquals(a, result1);
        Assert.assertEquals(null, result2);
    }
    
    @Test
    public void testBestWithEqualValuesAndMinim() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(-1.0, 0.5);
        
        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        
        Assert.assertEquals(a, result1);
    }
    
    @Test
    public void testBestWithEqualValuesAndMaxim() {
        BrentOptimizer optimizer = new BrentOptimizer(0.001, 0.1);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(-1.0, 0.5);
        
        UnivariatePointValuePair result1 = optimizer.best(a, b, false);
        
        Assert.assertEquals(b, result1);
    }
    
}


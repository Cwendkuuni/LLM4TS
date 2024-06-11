package org.apache.commons.math3.optim.univariate;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.nonlinear.scalar.UnivariatePointValuePair;
import org.apache.commons.math3.util.Precision;
import org.junit.*;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    // Test constructor
    @Test
    public void testConstructor() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-12, null);
        
        assertEquals(1e-5, optimizer.getRelativeThreshold(), 1e-15);
        assertEquals(1e-12, optimizer.getAbsoluteThreshold(), 1e-15);
    }
    
    // Test constructor with invalid relative threshold
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_InvalidRelativeThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-20, 1e-12, null);
    }
    
    // Test constructor with invalid absolute threshold
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_InvalidAbsoluteThreshold() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 0, null);
    }
    
    // Test doOptimize method
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-12, null);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(0.0, result.getPoint(), 1e-15);
        assertEquals(0.0, result.getValue(), 1e-15);
    }
    
    // Test best method with null inputs
    @Test
    public void testBest_NullInputs() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-12, null);
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = null;
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertNull(result);
    }
    
    // Test best method with valid inputs
    @Test
    public void testBest_ValidInputs() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-12, null);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertEquals(0.0, result.getPoint(), 1e-15);
        assertEquals(0.0, result.getValue(), 1e-15);
    }
    
    // Test best method with minimization goal type
    @Test
    public void testBest_MinimizationGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-12, null);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertEquals(0.0, result.getPoint(), 1e-15);
        assertEquals(0.0, result.getValue(), 1e-15);
    }
    
    // Test best method with maximization goal type
    @Test
    public void testBest_MaximizationGoalType() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-12, null);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        
        UnivariatePointValuePair result = optimizer.best(a, b, false);
        
        assertEquals(1.0, result.getPoint(), 1e-15);
        assertEquals(1.0, result.getValue(), 1e-15);
    }
    
    // Test best method with equal values
    @Test
    public void testBest_EqualValues() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-12, null);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 0);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertEquals(0.0, result.getPoint(), 1e-15);
        assertEquals(0.0, result.getValue(), 1e-15);
    }
    
    // Test best method with null value
    @Test
    public void testBest_NullValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-12, null);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        
        UnivariatePointValuePair result = optimizer.best(a, null, true);
        
        assertEquals(0.0, result.getPoint(), 1e-15);
        assertEquals(0.0, result.getValue(), 1e-15);
    }
    
    // Test best method with Precision.equals
    @Test
    public void testBest_PrecisionEquals() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-12, null);
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertEquals(0.0, result.getPoint(), 1e-15);
        assertEquals(0.0, result.getValue(), 1e-15);
    }
    
    // Test computeObjectiveValue method
    @Test
    public void testComputeObjectiveValue() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-12, null);
        
        double result = optimizer.computeObjectiveValue(5);
        
        assertEquals(0.0, result, 1e-15);
    }

}
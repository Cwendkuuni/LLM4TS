package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.GoalType;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setup() {
        optimizer = new BrentOptimizer(0.001, 0.001);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(optimizer);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_withInvalidRelativeThreshold() {
        new BrentOptimizer(0.0001, 0.001);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_withInvalidAbsoluteThreshold() {
        new BrentOptimizer(0.001, -0.001);
    }
    
    @Test
    public void testDoOptimize_withMinimizeGoalType() {
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(0.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }
    
    @Test
    public void testDoOptimize_withMaximizeGoalType() {
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(10.0, result.getPoint(), 0.001);
        assertEquals(0.0, result.getValue(), 0.001);
    }

    @Test
    public void testBest_withFirstValueMinim() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(2, 2);
        UnivariatePointValuePair b = new UnivariatePointValuePair(3, 3);
        boolean isMinim = true;
        
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        
        assertEquals(a, result);
    }
    
    @Test
    public void testBest_withFirstValueMaxim() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(2, 2);
        UnivariatePointValuePair b = new UnivariatePointValuePair(3, 3);
        boolean isMinim = false;
        
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        
        assertEquals(b, result);
    }
    
    @Test
    public void testBest_withSecondValueMinim() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(2, 3);
        UnivariatePointValuePair b = new UnivariatePointValuePair(3, 2);
        boolean isMinim = true;
        
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        
        assertEquals(b, result);
    }
    
    @Test
    public void testBest_withSecondValueMaxim() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(2, 3);
        UnivariatePointValuePair b = new UnivariatePointValuePair(3, 2);
        boolean isMinim = false;
        
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        
        assertEquals(a, result);
    }
    
    @Test
    public void testBest_withTwoEqualValues() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(2, 3);
        UnivariatePointValuePair b = new UnivariatePointValuePair(3, 3);
        boolean isMinim = true;
        
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        
        assertEquals(a, result);
    }
    
    @Test
    public void testBest_withNullValue() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(2, 3);
        UnivariatePointValuePair b = null;
        boolean isMinim = true;
        
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        
        assertEquals(a, result);
    }
    
    @Test
    public void testBest_withBothValuesNull() {
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = null;
        boolean isMinim = true;
        
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        
        assertEquals(null, result);
    }
}

package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.util.Precision;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setup() {
        optimizer = new BrentOptimizer(0.0001, 0.01);
    }
    
    // Test cases for the constructor
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_RelativeThresholdLessThanMinimum_ThrowsNumberIsTooSmallException() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0, 0.01);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_AbsoluteThresholdNotPositive_ThrowsNotStrictlyPositiveException() {
        BrentOptimizer optimizer = new BrentOptimizer(0.0001, -0.01);
    }
    
    // Test cases for doOptimize method
    @Test
    public void testDoOptimize_MinimizeGoalType_ReturnsMinimumValueWithinTolerance() {
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testDoOptimize_MaximizeGoalType_ReturnsMaximumValueWithinTolerance() {
        optimizer.setGoalType(GoalType.MAXIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(10, result.getValue(), 0.0001);
    }
    
    // Test cases for best method
    @Test
    public void testBest_AIsNull_ReturnsB() {
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        
        UnivariatePointValuePair result = optimizer.best(null, b, true);
        
        assertSame(b, result);
    }
    
    @Test
    public void testBest_BIsNull_ReturnsA() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 1);
        
        UnivariatePointValuePair result = optimizer.best(a, null, true);
        
        assertSame(a, result);
    }
    
    @Test
    public void testBest_AValueLessThanOrEqualBValueAndMinimizeGoalType_ReturnsA() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 2);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertSame(a, result);
    }
    
    @Test
    public void testBest_AValueGreaterThanBValueAndMinimizeGoalType_ReturnsB() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(2, 2);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertSame(b, result);
    }
    
    @Test
    public void testBest_AValueGreaterThanOrEqualBValueAndMaximizeGoalType_ReturnsA() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(2, 2);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        
        UnivariatePointValuePair result = optimizer.best(a, b, false);
        
        assertSame(a, result);
    }
    
    @Test
    public void testBest_AValueLessThanBValueAndMaximizeGoalType_ReturnsB() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 2);
        
        UnivariatePointValuePair result = optimizer.best(a, b, false);
        
        assertSame(b, result);
    }
    
    // Test cases for edge and error scenarios
    @Test
    public void testDoOptimize_MinGreaterThanMax_ReturnsMinimumValueWithinTolerance() {
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(10);
        optimizer.setMax(0);
        optimizer.setStartValue(5);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testDoOptimize_ConvergenceCheckerReturnsTrue_ReturnsBestValueWithinTolerance() {
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0);
        optimizer.setMax(10);
        optimizer.setStartValue(5);
        optimizer.setConvergenceChecker((iter, previous, current) -> true);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertTrue(Precision.equals(0, result.getValue(), 0.01));
    }
}


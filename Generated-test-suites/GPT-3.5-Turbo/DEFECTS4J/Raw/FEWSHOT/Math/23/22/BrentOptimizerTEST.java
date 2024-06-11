package org.apache.commons.math3.optimization.univariate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.util.Precision;
import org.junit.Before;
import org.junit.Test;

public class BrentOptimizerTest {

    private BrentOptimizer optimizer;
    private UnivariateObjectiveFunction objectiveFunction;
    private ConvergenceChecker<UnivariatePointValuePair> convergenceChecker;

    @Before
    public void setUp() {
        objectiveFunction = mock(UnivariateObjectiveFunction.class);
        convergenceChecker = mock(ConvergenceChecker.class);
         // Setting defaults values for constructors parameters
        double rel = 1;
        double abs = 1;
        optimizer = new BrentOptimizer(rel, abs, convergenceChecker);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizer_ThrowsNumberIsTooSmallException() {
        double rel = 0;
        double abs = 0;
        optimizer = new BrentOptimizer(rel, abs, convergenceChecker);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizer_ThrowsNotStrictlyPositiveException() {
        double rel = 1;
        double abs = 0;
        optimizer = new BrentOptimizer(rel, abs, convergenceChecker);
    }
    
    @Test
    public void testDoOptimize() {
        double lo = 1;
        double mid = 2;
        double hi = 3;
        GoalType goalType = GoalType.MINIMIZE;
        double minValue = 0.5;
        double start = 1.5;
        
        when(convergenceChecker.converged(any(), any(), any())).thenReturn(true);
        when(objectiveFunction.value(any())).thenReturn(minValue);
        
        optimizer.setObjective(objectiveFunction);
        optimizer.setGoalType(goalType);
        optimizer.setMin(lo);
        optimizer.setMax(hi);
        optimizer.setStartValue(start);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        verify(convergenceChecker, times(1)).converged(any(), any(), any());
        verify(objectiveFunction, times(1)).value(any());
        assertEquals(start, result.getPoint(), 0.0);
        assertEquals(minValue, result.getValue(), 0.0);
    }
    
    @Test
    public void testDoOptimize_NotConverged() {
        double lo = 1;
        double mid = 2;
        double hi = 3;
        GoalType goalType = GoalType.MINIMIZE;
        double minValue = 0.5;
        double start = 1.5;
        
        when(convergenceChecker.converged(any(), any(), any())).thenReturn(false);
        when(objectiveFunction.value(any())).thenReturn(minValue);
        
        optimizer.setObjective(objectiveFunction);
        optimizer.setGoalType(goalType);
        optimizer.setMin(lo);
        optimizer.setMax(hi);
        optimizer.setStartValue(start);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        verify(convergenceChecker, times(1)).converged(any(), any(), any());
        verify(objectiveFunction, times(1)).value(any());
        assertNull(result);
    }
    
    @Test
    public void testBest() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 2);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2, 1);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertEquals(a.getPoint(), result.getPoint(), 0.0);
        assertEquals(a.getValue(), result.getValue(), 0.0);
    }
    
    @Test
    public void testBest_ReturnsNullWhenBothParametersNull() {
        UnivariatePointValuePair result = optimizer.best(null, null, true);
        
        assertNull(result);
    }
    
    @Test
    public void testBest_WhenValuesAreEqual_ReturnsFirst() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(1, 2);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 1);
        
        UnivariatePointValuePair result = optimizer.best(a, b, true);
        
        assertEquals(a.getPoint(), result.getPoint(), 0.0);
        assertEquals(a.getValue(), result.getValue(), 0.0);
    }
}
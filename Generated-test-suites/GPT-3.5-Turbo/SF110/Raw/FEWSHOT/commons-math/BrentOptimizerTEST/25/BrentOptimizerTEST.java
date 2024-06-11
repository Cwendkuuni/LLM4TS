
package org.apache.commons.math3.optim.univariate;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        optimizer.withMax(10);
        optimizer.withMin(0);
        optimizer.withStartValue(5);
        optimizer.withGoalType(GoalType.MINIMIZE);

        UnivariatePointValuePair result = optimizer.doOptimize();

        assertEquals(0.0, result.getValue(), 0.001);
    }
    
    @Test
    public void testRelativeThresholdIsTooSmall() {
        try {
            new BrentOptimizer(-0.01, 0.1);
        } catch (NumberIsTooSmallException e) {
            assertEquals("Number -0.01 is smaller than the minimum 2.220446049250313E-16", e.getMessage());
        }
    }
    
    @Test
    public void testAbsoluteThresholdIsNotStrictlyPositive() {
        try {
            new BrentOptimizer(0.01, 0.0);
        } catch (NotStrictlyPositiveException e) {
            assertEquals("0.0 is not strictly positive.", e.getMessage());
        }
    }
    
    @Test
    public void testRelativeThresholdAndAbsoluteThresholdAreValid() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        
        assertEquals(0.01, optimizer.relativeThreshold, 0.001);
        assertEquals(0.1, optimizer.absoluteThreshold, 0.001);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.1);
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(0, 1);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1, 0);
        
        UnivariatePointValuePair bestAB = optimizer.best(a, b, true);
        assertEquals(1, bestAB.getPoint(), 0.001);
        assertEquals(0, bestAB.getValue(), 0.001);
        
        UnivariatePointValuePair bestBA = optimizer.best(b, a, true);
        assertEquals(1, bestBA.getPoint(), 0.001);
        assertEquals(0, bestBA.getValue(), 0.001);
    }
}

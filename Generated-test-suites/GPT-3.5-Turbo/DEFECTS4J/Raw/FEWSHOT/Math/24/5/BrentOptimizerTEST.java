import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optimization.tests.UnivariatePointValuePair;
import org.junit.Test;

public class BrentOptimizerTest {
    
    // Test Constructor Exception Handling
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_smallRelativeThreshold() {
        new BrentOptimizer(1E-20, 0);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_nonPositiveAbsoluteThreshold() {
        new BrentOptimizer(1, -1);
    }
    
    // Test doOptimize() Method
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1E-6, 1E-6);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(-10);
        optimizer.setMax(10);
        optimizer.setStartValue(0);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        assertEquals(0, result.getPoint(), 0);
    }
    
    // Test best() Method
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(1E-6, 1E-6);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair pointA = new UnivariatePointValuePair(1, 1);
        UnivariatePointValuePair pointB = new UnivariatePointValuePair(2, 2);
        
        UnivariatePointValuePair bestPoint = optimizer.best(pointA, pointB, true);
        assertEquals(1, bestPoint.getPoint(), 0);
        
        bestPoint = optimizer.best(pointA, pointB, false);
        assertEquals(2, bestPoint.getPoint(), 0);
    }
    
}
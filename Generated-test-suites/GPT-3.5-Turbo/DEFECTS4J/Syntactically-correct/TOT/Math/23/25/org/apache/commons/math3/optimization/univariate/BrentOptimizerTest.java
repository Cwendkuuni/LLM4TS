import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {
    
    @Test
    public void testDoOptimize_minimizationFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01);
        optimizer.setMin(-10.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(0.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
    
    @Test
    public void testDoOptimize_maximizationFunction() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.01);
        optimizer.setMin(-10.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(0.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertEquals(0.0, result.getPoint(), 0.0001);
        assertEquals(0.0, result.getValue(), 0.0001);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_invalidRelativeTolerance() {
        new BrentOptimizer(0.0, 0.01);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_invalidAbsoluteTolerance() {
        new BrentOptimizer(0.01, -0.001);
    }
    
    @Test
    public void testConstructor_validParameters() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        
        assertEquals(0.01, optimizer.getRelativeThreshold(), 0.0001);
        assertEquals(0.001, optimizer.getAbsoluteThreshold(), 0.0001);
    }
    
    @Test
    public void testBest() {
        UnivariatePointValuePair point1 = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair point2 = new UnivariatePointValuePair(2.0, 0.6);
        UnivariatePointValuePair point3 = new UnivariatePointValuePair(3.0, 0.7);
        
        assertTrue(BrentOptimizer.best(point1, point2, true) == point1);
        assertTrue(BrentOptimizer.best(point1, point2, false) == point2);
        
        assertTrue(BrentOptimizer.best(point2, point3, true) == point2);
        assertTrue(BrentOptimizer.best(point2, point3, false) == point3);
    }
    
    // Add more test cases for other methods
    
}
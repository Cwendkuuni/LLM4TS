import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructorWithConvergenceChecker() {
        // Test constructor with convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        assertNotNull(optimizer);
    }
    
    @Test
    public void testConstructorWithoutConvergenceChecker() {
        // Test constructor without convergence checker
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        assertNotNull(optimizer);
    }
    
    @Test
    public void testOptimize() {
        // Test optimize method with typical inputs
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        optimizer.setMin(0.0);
        optimizer.setMax(10.0);
        optimizer.setStartValue(5.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        UnivariatePointValuePair result = optimizer.optimize();
        
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 1e-6);
        assertEquals(0.0, result.getValue(), 1e-6);
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        // Test constructor with invalid relative threshold
        new BrentOptimizer(1e-12, 1e-6);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        // Test constructor with invalid absolute threshold
        new BrentOptimizer(1e-6, -1e-6);
    }

    @Test
    public void testBest() {
        // Test best method with two valid points
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair bestPoint = optimizer.best(a, b, true);
        
        assertEquals(b, bestPoint);
    }
}
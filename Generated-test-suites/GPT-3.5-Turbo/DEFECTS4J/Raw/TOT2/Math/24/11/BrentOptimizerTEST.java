import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructorWithConvergenceChecker() {
        // Test constructor with ConvergenceChecker
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-7, new SimpleConvergenceChecker<UnivariatePointValuePair>());
        assertNotNull(optimizer);
    }
    
    @Test
    public void testConstructorWithoutConvergenceChecker() {
        // Test constructor without ConvergenceChecker
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-7);
        assertNotNull(optimizer);
    }
    
    @Test
    public void testDoOptimize() {
        // Test doOptimize method
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-7);
        optimizer.setMin(0.0);
        optimizer.setStartValue(1.0);
        optimizer.setMax(2.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertEquals(1.0, result.getPoint(), 1e-10);
        assertEquals(0.0, result.getValue(), 1e-7);
    }
    
    @Test
    public void testBest() {
        // Test best method
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-7);
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 10.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 5.0);
        
        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        assertEquals(b, result1);
        
        UnivariatePointValuePair result2 = optimizer.best(a, b, false);
        assertEquals(a, result2);
    }
}
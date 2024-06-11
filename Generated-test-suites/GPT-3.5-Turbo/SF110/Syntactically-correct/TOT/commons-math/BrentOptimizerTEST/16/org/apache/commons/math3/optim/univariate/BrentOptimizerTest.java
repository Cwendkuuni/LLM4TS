import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testConstructorWithConvergenceChecker() {
        double rel = 0.01;
        double abs = 0.001;
        ConvergenceChecker<UnivariatePointValuePair> convergenceChecker = new MyConvergenceChecker();
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, convergenceChecker);
        
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.001);
        assertEquals(convergenceChecker, optimizer.getConvergenceChecker());
    }
    
    @Test
    public void testConstructorWithoutConvergenceChecker() {
        double rel = 0.01;
        double abs = 0.001;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.001);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.001);
        assertNull(optimizer.getConvergenceChecker());
    }
    
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        double rel = -0.01;
        double abs = 0.001;
        
        new BrentOptimizer(rel, abs);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        double rel = 0.01;
        double abs = 0.0;
        
        new BrentOptimizer(rel, abs);
    }
    
    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setMin(0.0);
        optimizer.setStartValue(1.0);
        optimizer.setMax(2.0);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        double expectedValue = 1.0;
        double expectedScore = optimizer.computeObjectiveValue(expectedValue);
        
        assertEquals(expectedValue, result.getPoint(), 0.001);
        assertEquals(expectedScore, result.getValue(), 0.001);
    }
    
    @Test
    public void testBest() {
        BrentOptimizer optimizer = new BrentOptimizer(0.01, 0.001);
        
        UnivariatePointValuePair a = new UnivariatePointValuePair(1.0, 0.5);
        UnivariatePointValuePair b = new UnivariatePointValuePair(2.0, 0.3);
        
        UnivariatePointValuePair result1 = optimizer.best(a, b, true);
        UnivariatePointValuePair result2 = optimizer.best(a, b, false);
        
        assertEquals(a, result1);
        assertEquals(b, result2);
    }
    
    private class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
      
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            // implementation of convergence check
            return false;
        }
    }
}
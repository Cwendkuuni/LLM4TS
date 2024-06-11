import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optim.nonlinear.scalar.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.util.*;

public class BrentOptimizerTest {
    
    // Test constructor with convergence checker
    @Test
    public void testConstructorWithConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new MyConvergenceChecker();
        double rel = 1e-6;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        assertNotNull(optimizer);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        assertEquals(checker, optimizer.getConvergenceChecker());
    }
    
    // Test constructor without convergence checker
    @Test
    public void testConstructorWithoutConvergenceChecker() {
        double rel = 1e-6;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        assertNotNull(optimizer);
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
        assertNull(optimizer.getConvergenceChecker());
    }
    
    // Test doOptimize() with valid points
    @Test
    public void testDoOptimizeWithValidPoints() {
        double rel = 1e-6;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        optimizer.setMin(-10.0);
        optimizer.setStartValue(0.0);
        optimizer.setMax(10.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.0);
    }
    
    // Test doOptimize() with invalid points
    @Test
    public void testDoOptimizeWithInvalidPoints() {
        double rel = 1e-6;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        optimizer.setMin(10.0);
        optimizer.setStartValue(0.0);
        optimizer.setMax(-10.0);
        optimizer.setGoalType(GoalType.MINIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.0);
    }
    
    // Test doOptimize() with valid goal type
    @Test
    public void testDoOptimizeWithValidGoalType() {
        double rel = 1e-6;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        optimizer.setMin(-10.0);
        optimizer.setStartValue(0.0);
        optimizer.setMax(10.0);
        optimizer.setGoalType(GoalType.MAXIMIZE);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.0);
    }
    
    // Test doOptimize() with invalid goal type
    @Test
    public void testDoOptimizeWithInvalidGoalType() {
        double rel = 1e-6;
        double abs = 1e-12;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        optimizer.setMin(-10.0);
        optimizer.setStartValue(0.0);
        optimizer.setMax(10.0);
        optimizer.setGoalType(null);
        
        UnivariatePointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
        assertEquals(0.0, result.getPoint(), 0.0);
    }
    
    // Test best() with a null UnivariatePointValuePair
    @Test
    public void testBestWithNullValuePair() {
        UnivariatePointValuePair a = null;
        UnivariatePointValuePair b = new UnivariatePointValuePair(0.0, 0.0);
        boolean isMinim = true;
        
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-12);
        
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        
        assertNotNull(result);
        assertEquals(b, result);
    }
    
    // Test best() with a non-null UnivariatePointValuePair
    @Test
    public void testBestWithNonNullValuePair() {
        UnivariatePointValuePair a = new UnivariatePointValuePair(0.0, 0.0);
        UnivariatePointValuePair b = new UnivariatePointValuePair(1.0, 1.0);
        boolean isMinim = true;
        
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-12);
        
        UnivariatePointValuePair result = optimizer.best(a, b, isMinim);
        
        assertNotNull(result);
        assertEquals(a, result);
    }
    
    // Custom ConvergenceChecker implementation
    private class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return false;
        }
    }
}
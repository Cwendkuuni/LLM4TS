import static org.junit.Assert.*;

import org.junit.Test;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.optimization.simplex.SimplexOptimizer;
import org.apache.commons.math3.optimization.univariate.SearchInterval;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NullArgumentException;

public class PowellOptimizerTest {
    
    // Test case for the constructor with default convergence checking procedure
    @Test
    public void testPowellOptimizerDefault() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        
        assertNotNull(optimizer);
    }
    
    // Test case for the constructor with user-defined convergence checker
    @Test
    public void testPowellOptimizerWithChecker() {
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            
            @Override
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                // Custom convergence check
                return (iteration > 10);
            }
        };
        
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001, checker);
        
        assertNotNull(optimizer);
    }
    
    // Test case for the constructor with invalid values
    @Test(expected = NumberIsTooSmallException.class)
    public void testPowellOptimizerInvalid() {
        PowellOptimizer optimizer = new PowellOptimizer(0.001, 0.01);
    }

    // Test case for the deprecated doOptimize() method
    @Test
    public void testDoOptimize() {
        PowellOptimizer optimizer = new PowellOptimizer(0.01, 0.001);
        optimizer.setGoalType(GoalType.MINIMIZE);
        optimizer.setStartPoint(new double[]{0, 0, 0});
        
        PointValuePair result = optimizer.doOptimize();
        
        assertNotNull(result);
    }
    
}
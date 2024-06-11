import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGradient;
import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {
    
    @Test
    public void testBrentOptimizerConstructor() {
        // Arrange
        double rel = 1e-6;
        double abs = 1e-6;
        
        // Act
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        // Assert
        assertNotNull(optimizer);
    }
    
    @Test
    public void testBrentOptimizerConstructorWithInvalidRel() {
        // Arrange
        double rel = -1e-6;
        double abs = 1e-6;
        
        try {
            // Act
            BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
            
            // Assert
            fail("Expected NumberIsTooSmallException not thrown");
        } catch (NumberIsTooSmallException e) {
            // Expected exception thrown
        }
    }
    
    @Test
    public void testBrentOptimizerConstructorWithInvalidAbs() {
        // Arrange
        double rel = 1e-6;
        double abs = -1e-6;
        
        try {
            // Act
            BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
            
            // Assert
            fail("Expected NotStrictlyPositiveException not thrown");
        } catch (NotStrictlyPositiveException e) {
            // Expected exception thrown
        }
    }
    
    @Test
    public void testBrentOptimizerDoOptimize() {
        // Arrange
        double rel = 1e-6;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        ObjectiveFunction objective = new ObjectiveFunction(x -> x * x);
        double min = -100.0;
        double max = 100.0;
        GoalType goal = GoalType.MINIMIZE;
        double startValue = 0.0;
        
        // Act
        UnivariatePointValuePair result = optimizer.optimize(objective, goal, min, max, startValue);
        
        // Assert
        assertTrue(Precision.equals(result.getValue(), 0.0, 1e-6));
    }
    
    @Test
    public void testBrentOptimizerDoOptimizeWithInvalidRange() {
        // Arrange
        double rel = 1e-6;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = null;
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        ObjectiveFunction objective = new ObjectiveFunction(x -> x * x);
        double min = 100.0;
        double max = -100.0;
        GoalType goal = GoalType.MINIMIZE;
        double startValue = 0.0;
        
        try {
            // Act
            UnivariatePointValuePair result = optimizer.optimize(objective, goal, min, max, startValue);
            
            // Assert
            fail("Expected NumberIsTooSmallException not thrown");
        } catch (NumberIsTooSmallException e) {
            // Expected exception thrown
        }
    }
    
    @Test
    public void testBrentOptimizerDoOptimizeWithConvergenceChecker() {
        // Arrange
        double rel = 1e-6;
        double abs = 1e-6;
        ConvergenceChecker<UnivariatePointValuePair> checker = (iter, prev, curr) -> {
            if (iter > 10) {
                return true;
            }
            if (prev == null || curr == null) {
                return false;
            }
            return Math.abs(curr.getValue() - prev.getValue()) < 1e-6;
        };
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        ObjectiveFunction objective = new ObjectiveFunction(x -> x * x);
        double min = -100.0;
        double max = 100.0;
        GoalType goal = GoalType.MINIMIZE;
        double startValue = 0.0;
        
        // Act
        UnivariatePointValuePair result = optimizer.optimize(objective, goal, min, max, startValue);
        
        // Assert
        assertTrue(result.getPoint() > 0.0);
    }
    
    
}
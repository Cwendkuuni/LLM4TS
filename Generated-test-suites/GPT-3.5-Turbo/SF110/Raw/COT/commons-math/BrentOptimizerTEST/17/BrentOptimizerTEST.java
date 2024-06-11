import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.optim.ConvergenceChecker;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.UnivariatePointValuePair;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunction;
import org.apache.commons.math3.optim.nonlinear.scalar.ObjectiveFunctionGradient;
import org.apache.commons.math3.optim.nonlinear.scalar.gradient.NonLinearConjugateGradientOptimizer;
import org.apache.commons.math3.util.Precision;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class BrentOptimizerTest {
    
    // Test constructor with valid parameters
    @Test
    public void testConstructorWithValidParameters() {
        double rel = 0.01;
        double abs = 0.001;
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs);
        
        assertEquals(rel, optimizer.getRelativeThreshold(), 0.0);
        assertEquals(abs, optimizer.getAbsoluteThreshold(), 0.0);
    }
    
    // Test constructor with invalid relative threshold
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelativeThreshold() {
        double rel = -0.01;
        double abs = 0.001;
        
        new BrentOptimizer(rel, abs);
    }
    
    // Test constructor with invalid absolute threshold
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsoluteThreshold() {
        double rel = 0.01;
        double abs = -0.001;
        
        new BrentOptimizer(rel, abs);
    }
    
    // Test optimize method
    @Test
    public void testOptimize() {
        double rel = 1e-6;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = new UnivariatePointValuePairConvergenceChecker();
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        // Set optimization parameters
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        GoalType goalType = GoalType.MINIMIZE;
        ObjectiveFunction objective = new ObjectiveFunction(x -> x * x);
        
        optimizer.optimize(
            new MaxEval(100),
            objective,
            goalType,
            min,
            max,
            startValue);
            
        UnivariatePointValuePair solution = optimizer.getOptimizationData().getSolution();
        
        double expectedMin = 0.0;
        double expectedValue = 0.0;
        assertEquals(expectedMin, solution.getPoint(), Precision.EPSILON);
        assertEquals(expectedValue, solution.getValue(), Precision.EPSILON);
    }
    
    // Test optimize method with invalid goal type
    @Test(expected = IllegalArgumentException.class)
    public void testOptimizeWithInvalidGoalType() {
        double rel = 1e-6;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = new UnivariatePointValuePairConvergenceChecker();
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        // Set optimization parameters
        GoalType goalType = GoalType.EITHER;
        
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        ObjectiveFunction objective = new ObjectiveFunction(x -> x * x);
        
        optimizer.optimize(
            new MaxEval(100),
            objective,
            goalType,
            min,
            max,
            startValue);
    }
    
    // Test optimize method with invalid bounds
    @Test(expected = NumberIsTooSmallException.class)
    public void testOptimizeWithInvalidBounds() {
        double rel = 1e-6;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = new UnivariatePointValuePairConvergenceChecker();
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        // Set optimization parameters
        GoalType goalType = GoalType.MINIMIZE;
        
        double min = 10.0;
        double max = 0.0;
        double startValue = 5.0;
        ObjectiveFunction objective = new ObjectiveFunction(x -> x * x);
        
        optimizer.optimize(
            new MaxEval(100),
            objective,
            goalType,
            min,
            max,
            startValue);
    }
    
    // Test optimize method with custom convergence checker
    @Test
    public void testOptimizeWithCustomConvergenceChecker() {
        double rel = 1e-6;
        double abs = 1e-8;
        ConvergenceChecker<UnivariatePointValuePair> checker = new UnivariatePointValuePairConvergenceChecker();
        
        BrentOptimizer optimizer = new BrentOptimizer(rel, abs, checker);
        
        // Set optimization parameters
        double min = 0.0;
        double max = 10.0;
        double startValue = 5.0;
        GoalType goalType = GoalType.MINIMIZE;
        ObjectiveFunction objective = new ObjectiveFunction(x -> x * x);
        
        optimizer.optimize(
            new MaxEval(100),
            objective,
            GoalType.MINIMIZE,
            min,
            max,
            startValue);
            
        UnivariatePointValuePair solution = optimizer.getOptimizationData().getSolution();
        
        double expectedMin = 0.0;
        double expectedValue = 0.0;
        assertEquals(expectedMin, solution.getPoint(), Precision.EPSILON);
        assertEquals(expectedValue, solution.getValue(), Precision.EPSILON);
    }
    
    // Helper class for custom convergence checker
    private static class UnivariatePointValuePairConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        
        @Override
        public boolean converged(int iterations, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return current.getValue() <= 0.001;
        }
    }
}
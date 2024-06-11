import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.univariate.MaxIterationsExceededException;
import org.apache.commons.math.optimization.univariate.FunctionEvaluationException;

public class BrentOptimizerTest {
    
    // Test cases for optimize(UnivariateRealFunction f, GoalType goalType, double min, double max, double startValue)
    
    @Test
    public void testOptimize_StartValueInBounds_Minimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = x -> x * x; // y = x^2
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 5.0;
        
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, goalType, min, max, startValue);
        
        double expected = 0.0;
        Assertions.assertEquals(expected, result, 1e-10);
    }
    
    @Test
    public void testOptimize_StartValueInBounds_Maximize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = x -> -x * x; // y = -x^2
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = -3.0;
        
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, goalType, min, max, startValue);
        
        double expected = 0.0;
        Assertions.assertEquals(expected, result, 1e-10);
    }
    
    @Test
    public void testOptimize_StartValueOutOfBounds() {
        UnivariateRealFunction f = x -> x * x; // y = x^2
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 15.0;
        
        BrentOptimizer optimizer = new BrentOptimizer();
        Assertions.assertThrows(FunctionEvaluationException.class, () -> {
            optimizer.optimize(f, goalType, min, max, startValue);
        });
    }
    
    // Test cases for optimize(UnivariateRealFunction f, GoalType goalType, double min, double max)
    
    @Test
    public void testOptimize_DefaultStartValue_Minimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = x -> Math.sin(x); // y = sin(x)
        GoalType goalType = GoalType.MINIMIZE;
        double min = -Math.PI;
        double max = Math.PI;
        
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, goalType, min, max);
        
        double expected = -1.0; // Minimum value of sin(x) in the range [-pi, pi]
        Assertions.assertEquals(expected, result, 1e-10);
    }
    
    @Test
    public void testOptimize_DefaultStartValue_Maximize() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = x -> -Math.sin(x); // y = -sin(x)
        GoalType goalType = GoalType.MAXIMIZE;
        double min = -Math.PI;
        double max = Math.PI;
        
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(f, goalType, min, max);
        
        double expected = 1.0; // Maximum value of -sin(x) in the range [-pi, pi]
        Assertions.assertEquals(expected, result, 1e-10);
    }
    
    // Test case for localMin(...)
    
    @Test
    public void testLocalMin_Minimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double lo = -10.0;
        double mid = 0.0;
        double hi = 10.0;
        double eps = 1e-7;
        double t = 1e-9;
        
        UnivariateRealFunction f = x -> x * x; // y = x^2
        GoalType goalType = GoalType.MINIMIZE;
        
        double result = optimizer.localMin(goalType, f, goalType, lo, mid, hi, eps, t);
        
        double expected = 0.0; // Minimum value of x^2 in the range [-10, 10]
        Assertions.assertEquals(expected, result, 1e-7);
    }
    
    // Additional test cases for different scenarios
    
    @Test
    public void testDoOptimize_NotSupported() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            optimizer.doOptimize();
        });
    }
    
    // Add more test cases as needed for edge cases and error scenarios
    
}
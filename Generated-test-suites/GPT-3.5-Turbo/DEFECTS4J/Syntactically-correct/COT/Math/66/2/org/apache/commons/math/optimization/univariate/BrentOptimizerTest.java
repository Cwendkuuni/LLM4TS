import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.exception.NotStrictlyPositiveException;
import org.apache.commons.math.optimization.GoalType;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Test;

public class BrentOptimizerTest {
    
    // Test data for optimize() method
    private UnivariateRealFunction testFunction = new UnivariateRealFunction() {
        @Override
        public double value(double x) throws FunctionEvaluationException {
            return Math.pow(x, 2);
        }
    };
    
    private double testMin = -10.0;
    private double testMax = 10.0;
    private double testStartValue = 5.0;
    
    // Test for successful optimization
    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(testFunction, GoalType.MINIMIZE, testMin, testMax, testStartValue);
        assertEquals("Optimized value is incorrect", 0.0, result, 1E-10);
    }
    
    // Test for maximum iteration count exceeded exception
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeMaxIterationsExceededException() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(10);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, testMin, testMax, testStartValue);
    }
    
    // Test for function evaluation exception
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimizeFunctionEvaluationException() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction exceptionFunction = new UnivariateRealFunction() {
            @Override
            public double value(double x) throws FunctionEvaluationException {
                throw new FunctionEvaluationException(x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(exceptionFunction, GoalType.MINIMIZE, testMin, testMax, testStartValue);
    }
    
    // Test for not strictly positive exception on epsilon
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeNotStrictlyPositiveExceptionOnEpsilon() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setRelativeAccuracy(0);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, testMin, testMax, testStartValue);
    }
    
    // Test for not strictly positive exception on tolerance
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeNotStrictlyPositiveExceptionOnTolerance() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setAbsoluteAccuracy(0);
        optimizer.optimize(testFunction, GoalType.MINIMIZE, testMin, testMax, testStartValue);
    }
    
    // Additional edge cases and exception handling scenarios can be added here
    
    // Test for other public methods in BrentOptimizer class
    
    // ...
    
    // 
    // Merge all the individual test cases into one complete JUnit test file
    // 
}
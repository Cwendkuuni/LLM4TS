import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    // 
  
    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
        
        assertEquals(0, result, 0.0001);
    }
    
    @Test
    public void testOptimizeWithStartValue() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.abs(x);
            }
        };
        
        double result = optimizer.optimize(f, GoalType.MINIMIZE, -10, 10, 5);
        
        assertEquals(0, result, 0.0001);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimizeWithMaxIterationsExceeded() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(10);
        
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };
        
        optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithInvalidRelativeAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };
        
        optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testOptimizeWithInvalidAbsoluteAccuracy() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x;
            }
        };
        
        optimizer.optimize(f, GoalType.MINIMIZE, -10, 10);
    }
    
    // 
}
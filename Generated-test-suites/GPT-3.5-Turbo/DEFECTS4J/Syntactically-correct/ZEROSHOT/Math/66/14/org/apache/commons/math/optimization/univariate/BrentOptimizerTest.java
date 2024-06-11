import static org.junit.Assert.*;
import org.junit.Test;

public class BrentOptimizerTest {
    
    @Test
    public void testOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        // Test case 1
        UnivariateRealFunction f1 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x;
            }
        };
        double result1 = optimizer.optimize(f1, GoalType.MINIMIZE, 0, 10);
        assertEquals(0, result1, 1E-10);
        
        // Test case 2
        UnivariateRealFunction f2 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return -x * x;
            }
        };
        double result2 = optimizer.optimize(f2, GoalType.MAXIMIZE, -10, 0);
        assertEquals(0, result2, 1E-10);
        
        // Test case 3
        UnivariateRealFunction f3 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.sin(x);
            }
        };
        double result3 = optimizer.optimize(f3, GoalType.MINIMIZE, 0, 2 * Math.PI);
        assertEquals(Math.PI / 2, result3, 1E-10);
    }
    
    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        // Test case 4
        UnivariateRealFunction f4 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return Math.cos(x);
            }
        };
        optimizer.optimize(f4, GoalType.MAXIMIZE, -10, 10);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testNotStrictlyPositiveException() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        // Test case 5
        UnivariateRealFunction f5 = new UnivariateRealFunction() {
            public double value(double x) throws FunctionEvaluationException {
                return x * x * x;
            }
        };
        optimizer.optimize(f5, GoalType.MINIMIZE, 0, 10, -1);
    }
}
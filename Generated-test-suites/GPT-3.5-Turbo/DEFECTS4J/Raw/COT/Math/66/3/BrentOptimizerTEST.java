import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {
    
    private BrentOptimizer optimizer;
    
    @Before
    public void setUp() {
        optimizer = new BrentOptimizer();
    }
    
    
    
    @Test
    public void testOptimize_UnivariateRealFunction_GoalType_double_double_double() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        double startValue = 0.0;
        
        double result = optimizer.optimize(f, goalType, min, max, startValue);
        
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test
    public void testOptimize_UnivariateRealFunction_GoalType_double_double() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = -10.0;
        double max = 10.0;
        
        double result = optimizer.optimize(f, goalType, min, max);
        
        assertEquals(0.0, result, 0.0001);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin_InvalidEps() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double lo = -10.0;
        double mid = 0.0;
        double hi = 10.0;
        double eps = -1.0;
        double t = 0.1;
        
        optimizer.localMin(true, f, goalType, lo, mid, hi, eps, t);
    }
    
    @Test(expected = NotStrictlyPositiveException.class)
    public void testLocalMin_InvalidT() throws MaxIterationsExceededException, FunctionEvaluationException {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double lo = -10.0;
        double mid = 0.0;
        double hi = 10.0;
        double eps = 0.1;
        double t = -1.0;
        
        optimizer.localMin(true, f, goalType, lo, mid, hi, eps, t);
    }
    
    
}
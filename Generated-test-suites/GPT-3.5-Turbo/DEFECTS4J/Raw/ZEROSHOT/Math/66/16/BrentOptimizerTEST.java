import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.MaxIterationsExceededException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

public class BrentOptimizerTest {
    
    private static final double EPSILON = 1e-10;
    
    @Test
    public void testOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        
        double result = optimizer.optimize(function, GoalType.MINIMIZE, 0, 5);
        
        Assert.assertEquals(2.0, result, EPSILON);
    }
    
    @Test
    public void testLocalMin() throws MaxIterationsExceededException, FunctionEvaluationException {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4 * x + 3;
            }
        };
        
        double result = optimizer.localMin(true, function, GoalType.MINIMIZE, 0, 2.5, 5, EPSILON, EPSILON);
        
        Assert.assertEquals(2.0, result, EPSILON);
    }
}
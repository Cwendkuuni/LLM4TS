import org.junit.Test;
import org.junit.Assert;

public class BrentOptimizerTest {

    // Typical use case
    @Test
    public void testOptimize_typical() {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, 0.0, 10.0);
        
        Assert.assertEquals(result, 0.0, 1e-10);
    }
    
    // Edge cases
    @Test
    public void testOptimize_edgeCases() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        // Lower bound
        double resultLowerBound = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, 0.0, 1.0);
        
        Assert.assertEquals(resultLowerBound, 0.0, 1e-10);
        
        // Upper bound
        double resultUpperBound = optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, 0.0, 100.0);
        
        Assert.assertEquals(resultUpperBound, 0.0, 1e-10);
    }
    
    // Error scenarios
    @Test(expected = MaxIterationsExceededException.class)
    public void testOptimize_maxIterationsExceeded() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.setMaximalIterationCount(1);
        
        optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                return x * x;
            }
        }, GoalType.MINIMIZE, 0.0, 10.0);
    }
    
    @Test(expected = FunctionEvaluationException.class)
    public void testOptimize_functionEvaluationError() {
        BrentOptimizer optimizer = new BrentOptimizer();
        
        optimizer.optimize(new UnivariateRealFunction() {
            public double value(double x) {
                throw new RuntimeException("Function evaluation error");
            }
        }, GoalType.MINIMIZE, 0.0, 10.0);
    }
}
import org.junit.Test;
import org.apache.commons.math.optimization.univariate.BrentOptimizer;
import org.apache.commons.math.optimization.univariate.AbstractUnivariateRealOptimizer;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.optimization.GoalType;

public class BrentOptimizerTest {
    
    @Test
    public void testLocalMin() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double lo = 0.0;
        double mid = 1.0;
        double hi = 2.0;
        double eps = 1E-6;
        double t = 1E-6;
        double result = optimizer.localMin(true, f, goalType, lo, mid, hi, eps, t);
        assertEquals(0.0, result, 1E-6);
    }
    
    @Test
    public void testOptimize() throws Exception {
        BrentOptimizer optimizer = new BrentOptimizer();
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 2);
            }
        };
        GoalType goalType = GoalType.MINIMIZE;
        double min = 0.0;
        double max = 1.0;
        double result = optimizer.optimize(f, goalType, min, max);
        assertEquals(0.0, result, 1E-6);
    }
}
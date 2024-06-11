import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BrentOptimizerTest {

    @Test
    public void testOptimize() {
        // Test with a simple function
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, null);
        UnivariatePointValuePair result = optimizer.optimize(0, 10, new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        });

        assertEquals(0, result.getPoint(), 1e-6);
        assertEquals(0, result.getValue(), 1e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
     public void testInvalidAbsThreshold() {
         // Test with invalid abs threshold
         BrentOptimizer optimizer = new BrentOptimizer(1, 0, null);
         UnivariatePointValuePair result = optimizer.optimize(0, 10, new UnivariateFunction() {
             public double value(double x) {
                 return x * x;
             }
         });
     }
    
    @Test
    public void testOptimizeWithConvergeneChecker() {
        // Test with a simple function and convergence checker
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                return Math.abs(previous.getPoint() - current.getPoint()) < 1e-4;
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, checker);
        UnivariatePointValuePair result = optimizer.optimize(0, 10, new UnivariateFunction() {
            public double value(double x) {
                return x * x;
            }
        });

        assertEquals(0, result.getPoint(), 1e-4);
        assertEquals(0, result.getValue(), 1e-6);
    }
}
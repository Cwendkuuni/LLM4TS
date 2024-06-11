import org.junit.Test;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithNegativeAbsoluteThreshold() {
        new BrentOptimizer(1.0, -1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithTooSmallRelativeThreshold() {
        new BrentOptimizer(1e-16, 1.0);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8);
        UnivariatePointValuePair result = optimizer.optimize(100, x -> x * x); // example optimization
        assertEquals(0.0, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }

    @Test
    public void testDoOptimizeWithConvergenceChecker() {
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            @Override
            public boolean converged(int iteration, UnivariatePointValuePair previousResult, UnivariatePointValuePair currentResult) {
                // custom convergence checking logic
                return iteration > 10;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-8, 1e-8, checker);
        UnivariatePointValuePair result = optimizer.optimize(100, x -> x * x); // example optimization
        assertEquals(0.0, result.getPoint(), 1e-8);
        assertEquals(0.0, result.getValue(), 1e-8);
    }
}
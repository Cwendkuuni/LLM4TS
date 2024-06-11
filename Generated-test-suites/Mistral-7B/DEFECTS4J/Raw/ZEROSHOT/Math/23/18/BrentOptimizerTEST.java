import org.apache.commons.math3.optim.univariate.BrentOptimizer;
import org.apache.commons.math3.optim.univariate.UnivariateFunction;
import org.apache.commons.math3.optim.univariate.UnivariatePointValuePair;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.pow(x, 3) - 4 * x + 2;
            }
        };

        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6);
        UnivariatePointValuePair result = optimizer.optimize(function, -10, 10);

        assertEquals(result.getPoint(), -1.0, 1e-6);
        assertEquals(result.getValue(), -2.0, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerAbsLessThanZero() {
        new BrentOptimizer(1e-6, -1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerRelSmallerThanMachineEpsilon() {
        new BrentOptimizer(1e-9, 1e-6);
    }

    @Test
    public void testBrentOptimizerConvergenceChecker() {
        UnivariateFunction function = new UnivariateFunction() {
            @Override
            public double value(double x) {
                return Math.abs(Math.sin(Math.PI * x) - x);
            }
        };
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1e-6, new MyConvergenceChecker());
        UnivariatePointValuePair result = optimizer.optimize(function, -2, 2);

        assertEquals(result.getPoint(), 0.0, 1e-6);
        assertEquals(result.getValue(), 0.0, 1e-6);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<UnivariatePointValuePair> {
        @Override
        public boolean converged(int iteration, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
            return current.getValue() < 1e-6;
        }
    }
}
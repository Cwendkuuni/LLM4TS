import org.apache.commons.math3.optim.UnivariateOptimizer;
import org.apache.commons.math3.optim.PointValuePair;
import org.apache.commons.math3.optim.goaltype.GoalType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizer() {
        // Test with default constructor
        BrentOptimizer optimizer = new BrentOptimizer(null);
        UnivariateOptimizer result = optimizer.optimize(x -> Math.pow(x, 2), -5.0, 5.0);
        PointValuePair expected = new PointValuePair(Math.sqrt(2.5), Math.sqrt(2.5));
        assertEquals(expected, result);

        // Test with custom relative and absolute tolerances
        double relTolerance = 0.0001;
        double absTolerance = 0.001;
        BrentOptimizer optimizerCustom = new BrentOptimizer(relTolerance, absTolerance, null);
        result = optimizerCustom.optimize(x -> Math.exp(x) - 1, -5.0, 5.0);
        expected = new PointValuePair(1.718281828459045, 0.0);
        assertEquals(expected, result);

        // Test with custom convergence checker
        BrentOptimizer optimizerWithChecker = new BrentOptimizer(0.01, 0.0, new MyConvergenceChecker());
        result = optimizerWithChecker.optimize(x -> Math.abs(Math.sin(x)), -5.0, 5.0);
        double expectedValue = 1.27320535083887;
        assertEquals(expectedValue, result.getValue(), 0.001);
        assertEquals(Math.PI / 2, result.getPoint(), 0.001);
    }

    private static class MyConvergenceChecker implements ConvergenceChecker<PointValuePair> {
        @Override
        public boolean converged(int iteration, PointValuePair last, PointValuePair current) {
            return Math.abs(current.getValue() - last.getValue()) < 0.001;
        }
    }
}
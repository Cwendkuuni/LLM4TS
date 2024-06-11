import org.apache.commons.math3.analysis.UnivariateRealFunction;
import org.apache.commons.math3.optim.AbstractUnivariateRealOptimizer;
import org.apache.commons.math3.optim.GoalType;
import org.apache.commons.math3.optim.MaxIterationsExceededException;
import org.apache.commons.math3.optim.NonConvergentException;
import org.junit.Assert;
import org.junit.Test;

public class BrentOptimizerTest {

    /**
     * A simple test function for Brent Optimizer.
     */
    private static final UnivariateRealFunction TEST_FUNCTION = new UnivariateRealFunction() {
        @Override
        public double value(double x) {
            return Math.pow(x, 2) + Math.pow(x, 3) - 2 * Math.pow(x, 2) * Math.log(Math.abs(x));
        }
    };

    /**
     * Test optimization with minimum goal.
     */
    @Test
    public void testMinimize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = 0;
        double expected = -1.1315566;
        try {
            result = optimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, -10.0, 10.0, 0.0);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        Assert.assertEquals(expected, result, 1e-5);
    }

    /**
     * Test optimization with maximum goal.
     */
    @Test
    public void testMaximize() {
        BrentOptimizer optimizer = new BrentOptimizer();
        double result = 0;
        double expected = 1.1315566;
        try {
            result = optimizer.optimize(TEST_FUNCTION, GoalType.MAXIMIZE, -10.0, 10.0, 0.0);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
        Assert.assertEquals(expected, result, 1e-5);
    }

    /**
     * Test exception handling when minimum is greater than maximum.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidBounds() {
        BrentOptimizer optimizer = new BrentOptimizer();
        optimizer.optimize(TEST_FUNCTION, GoalType.MINIMIZE, 10.0, 5.0, 0.0);
    }
}
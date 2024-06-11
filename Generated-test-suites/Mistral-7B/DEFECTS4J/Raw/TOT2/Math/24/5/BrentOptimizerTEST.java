import org.apache.commons.math3.optim.*;
import org.apache.commons.math3.optim.univariate.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testBrentOptimizerMinimization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 10.0);
        UnivariateFunction function = new SimpleFunction("-x * x + 2 * x - 1");
        UnivariatePointValuePair result = optimizer.optimize(function, null);
        double expected = 1.0;
        assertEquals(expected, result.getPoint(), 0.001);
        assertEquals(expected, result.getValue(), 1.01e-6);
    }

    @Test
    public void testBrentOptimizerMaximization() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 10.0, GoalType.MAXIMIZE);
        UnivariateFunction function = new SimpleFunction("x * x + 2 * x + 1");
        UnivariatePointValuePair result = optimizer.optimize(function, null);
        double expected = 3.0;
        assertEquals(expected, result.getPoint(), 0.001);
        assertEquals(expected, result.getValue(), 1.01e-6);
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testBrentOptimizerInvalidRelThreshold() {
        new BrentOptimizer(-1.0, 1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testBrentOptimizerInvalidAbsThreshold() {
        new BrentOptimizer(1.0, -1.0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerInvalidGoalType() {
        new BrentOptimizer(1.0, 1.0, GoalType.MAXIMIZE_INDEFINITE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBrentOptimizerNullConvergenceChecker() {
        new BrentOptimizer(1.0, 1.0, null);
    }

    @Test
    public void testBrentOptimizerEdgeCases() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-6, 1.0, GoalType.MINIMIZE);

        UnivariateFunction function1 = new SimpleFunction("Math.abs(x) * Math.log(Math.abs(x))");
        UnivariatePointValuePair result1 = optimizer.optimize(function1, null);
        double expected1 = 0.0;
        assertEquals(expected1, result1.getPoint(), 0.001);
        assertEquals(expected1, result1.getValue(), 1.01e-6);

        UnivariateFunction function2 = new SimpleFunction("Math.exp(-x) / x");
        UnivariatePointValuePair result2 = optimizer.optimize(function2, null);
        double expected2 = 0.5403023058687556;
        assertEquals(expected2, result2.getPoint(), 0.001);
        assertEquals(expected2, result2.getValue(), 1.01e-6);
    }
}
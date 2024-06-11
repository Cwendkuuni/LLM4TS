import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.SimpleUnivariateFunction;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.univariate.UnivariateObjectiveFunction;
import org.apache.commons.math3.optimization.ConvergenceChecker;

import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorAbsNotStrictlyPositive() {
        new BrentOptimizer(1.0, -1.0);
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorRelTooSmall() {
        new BrentOptimizer(1e-20, 1.0);
    }

    @Test
    public void testConstructorAbsAndRel() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-15);
        assertEquals(1e-12, optimizer.getRelativeThreshold(), Double.MIN_VALUE);
        assertEquals(1e-15, optimizer.getAbsoluteThreshold(), Double.MIN_VALUE);
    }

    @Test
    public void testDoOptimize() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-15);

        // Test with a simple function x^2
        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(x -> x * x);
        UnivariatePointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, -10, 10);

        assertEquals(0.0, result.getPoint(), 1e-12);
        assertEquals(0.0, result.getValue(), 1e-15);

        // Test with a function that has a minimum at a boundary
        function = new UnivariateObjectiveFunction(x -> Math.exp(-x) + x);
        result = optimizer.optimize(100, function, GoalType.MINIMIZE, Double.NEGATIVE_INFINITY, 0.0);

        assertEquals(0.0, result.getPoint(), 1e-12);
        assertEquals(1.0, result.getValue(), 1e-15);

        // Test with a simple function that has a maximum at a boundary
        function = new UnivariateObjectiveFunction(x -> -Math.exp(x) - x);
        result = optimizer.optimize(100, function, GoalType.MAXIMIZE, 0.0, Double.POSITIVE_INFINITY);

        assertEquals(0.0, result.getPoint(), 1e-12);
        assertEquals(-1.0, result.getValue(), 1e-15);

        // Test with a function that has a local minimum and maximum
        function = new UnivariateObjectiveFunction(x -> x * x * x - x * x + x);
        result = optimizer.optimize(100, function, GoalType.MINIMIZE, -10, 10);

        assertEquals(1.0, result.getPoint(), 1e-12);
        assertEquals(-0.5, result.getValue(), 1e-15);

        // Test with a function that has a steep gradient
        function = new UnivariateObjectiveFunction(x -> Math.abs(x));
        result = optimizer.optimize(100, function, GoalType.MINIMIZE, -10, 10);

        assertEquals(0.0, result.getPoint(), 1e-12);
        assertEquals(0.0, result.getValue(), 1e-15);
    }

    @Test
    public void testDoOptimizeCustomConvergenceChecker() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-15,
                (iter, previous, current) -> {
                    double diff = Math.abs(current.getValue() - previous.getValue());
                    return diff < 1e-6;
                });

        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(x -> x * x);
        UnivariatePointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, -10, 10);

        assertEquals(0.0, result.getPoint(), 1e-12);
        assertEquals(0.0, result.getValue(), 1e-15);
    }

    @Test(timeout = 5000)
    public void testDoOptimizeConvergenceCheckerTimeout() {
        BrentOptimizer optimizer = new BrentOptimizer(1e-12, 1e-15,
                (iter, previous, current) -> {
                    // always return false to simulate a convergence checker that never converges
                    return false;
                });

        UnivariateObjectiveFunction function = new UnivariateObjectiveFunction(x -> x * x);
        UnivariatePointValuePair result = optimizer.optimize(100, function, GoalType.MINIMIZE, -10, 10);

        assertNull(result);
    }

}
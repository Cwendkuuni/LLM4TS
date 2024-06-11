import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math3.optimization.univariate.BrentOptimizer;
import org.apache.commons.math3.optimization.univariate.UnivariatePointValuePair;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize_minimizationSuccess() {
        // Create a BrentOptimizer instance
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);

        // Define the objective function for minimization
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x * x; // function f(x) = x^2
            }
        };

        // Set the minimization goal
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Set the boundaries for the optimization
        optimizer.setMin(-10);
        optimizer.setMax(10);

        // Perform the optimization
        UnivariatePointValuePair result = optimizer.optimize(function);

        // Check if the optimization result is correct
        assertEquals(0.0, result.getPoint(), 1e-10); // The minimum of f(x) = x^2 is at x = 0
    }

    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructor_relTooSmall() {
        new BrentOptimizer(1e-20, 1e-10); // rel should not be smaller than 2 * Math.ulp(1d)
    }

    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructor_absNegative() {
        new BrentOptimizer(1e-10, -1e-10); // abs should be positive
    }

    @Test
    public void testDoOptimize_maximizationSuccess() {
        // Create a BrentOptimizer instance
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);

        // Define the objective function for maximization
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return -x * x; // function f(x) = -x^2
            }
        };

        // Set the maximization goal
        optimizer.setGoalType(GoalType.MAXIMIZE);

        // Set the boundaries for the optimization
        optimizer.setMin(-10);
        optimizer.setMax(10);

        // Perform the optimization
        UnivariatePointValuePair result = optimizer.optimize(function);

        // Check if the optimization result is correct
        assertEquals(0.0, result.getPoint(), 1e-10); // The maximum of f(x) = -x^2 is at x = 0
    }

    @Test
    public void testDoOptimize_edgeCaseMinAndMax() {
        // Create a BrentOptimizer instance
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);

        // Define the objective function
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return x - 10; // function f(x) = x - 10
            }
        };

        // Set the minimization goal
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Set the boundaries for the optimization
        optimizer.setMin(-10);
        optimizer.setMax(10);

        // Perform the optimization
        UnivariatePointValuePair result = optimizer.optimize(function);

        // Check if the optimization result is correct
        assertEquals(10.0, result.getPoint(), 1e-10); // The minimum of f(x) = x - 10 is at x = 10
    }

    @Test
    public void testDoOptimize_errorScenario() {
        // Create a BrentOptimizer instance
        BrentOptimizer optimizer = new BrentOptimizer(1e-10, 1e-10);

        // Define the objective function
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return 1 / (x - 1); // function f(x) = 1 / (x - 1)
            }
        };

        // Set the minimization goal
        optimizer.setGoalType(GoalType.MINIMIZE);

        // Set the boundaries for the optimization
        optimizer.setMin(-10);
        optimizer.setMax(10);

        // Perform the optimization
        UnivariatePointValuePair result = optimizer.optimize(function);

        // Check if the optimization result is correct
        assertTrue(Double.isNaN(result.getPoint())); // The function f(x) = 1 / (x - 1) is undefined at x = 1
    }

}
import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.math3.optimization.direct.PowellOptimizer;
import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.optimization.GoalType;
import org.apache.commons.math3.optimization.PointValuePair;
import org.apache.commons.math3.optimization.ConvergenceChecker;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

public class PowellOptimizerTest {

    // Test the doOptimize() method
    @Test
    public void testDoOptimize() {
        // Define the test function
        MultivariateFunction testFunction = new MultivariateFunction() {
            public double value(double[] point) {
                // Example function: f(x, y) = x^2 + y^2 - 2
                return Math.pow(point[0], 2) + Math.pow(point[1], 2) - 2;
            }
        };

        // Create an instance of the PowellOptimizer with convergence criteria
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                // Check the convergence based on the tolerance
                double tolerance = 1e-6;
                double diff = Math.abs(current.getValue() - previous.getValue());
                return diff < tolerance;
            }
        };

        // Create a PowellOptimizer object
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);

        // Set the optimization goal
        GoalType goal = GoalType.MINIMIZE;
        optimizer.setGoalType(goal);

        // Set the starting point
        double[] startPoint = {1, 1};
        optimizer.setStartPoint(startPoint);

        // Perform the optimization
        PointValuePair result = optimizer.optimize(1000, testFunction, goal);

        // Check the result
        double[] expectedPoint = {0, 0};
        double expectedValue = -2.0;
        Assert.assertArrayEquals(expectedPoint, result.getPoint(), 1e-6);
        Assert.assertEquals(expectedValue, result.getValue(), 1e-6);
    }

    // Test the constructor with custom convergence criteria
    @Test
    public void testConstructorWithConvergenceChecker() {
        ConvergenceChecker<PointValuePair> checker = new ConvergenceChecker<PointValuePair>() {
            public boolean converged(int iteration, PointValuePair previous, PointValuePair current) {
                // Custom convergence criteria
                return iteration > 100 || previous.getValue() < current.getValue();
            }
        };

        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 1e-6, checker);

        // Check that the convergence checker is set correctly
        Assert.assertEquals(checker, optimizer.getConvergenceChecker());
    }
  
    // Test the constructor with invalid parameters
    @Test(expected = NumberIsTooSmallException.class)
    public void testConstructorWithInvalidRelTolerance() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-16, 1e-6);
    }

    // Test the constructor with invalid parameters
    @Test(expected = NotStrictlyPositiveException.class)
    public void testConstructorWithInvalidAbsTolerance() {
        PowellOptimizer optimizer = new PowellOptimizer(1e-6, 0);
    }

}
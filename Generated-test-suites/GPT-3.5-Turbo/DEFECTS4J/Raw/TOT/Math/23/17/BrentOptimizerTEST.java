import org.junit.Test;
import static org.junit.Assert.*;

public class BrentOptimizerTest {

    @Test
    public void testDoOptimize_Minimize() {
        // Create a test instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-7);

        // Define the function to be minimized
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return (x - 2) * (x - 2);
            }
        };

        // Set the function to be minimized
        optimizer.setFunction(function);

        // Set the optimization parameters
        optimizer.setMin(0);
        optimizer.setMax(4);
        optimizer.setStartValue(3);

        // Perform the optimization
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Check the result
        assertEquals(2, result.getPoint(), 1e-5);
    }

    @Test
    public void testDoOptimize_Maximize() {
        // Create a test instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-7);

        // Define the function to be maximized
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return -(x - 2) * (x - 2);
            }
        };

        // Set the function to be maximized
        optimizer.setFunction(function);

        // Set the optimization parameters
        optimizer.setMin(0);
        optimizer.setMax(4);
        optimizer.setStartValue(3);

        // Perform the optimization
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Check the result
        assertEquals(2, result.getPoint(), 1e-5);
    }

    @Test
    public void testDoOptimize_MinimizeWithConvergenceChecker() {
        // Create a test instance of BrentOptimizer
        BrentOptimizer optimizer = new BrentOptimizer(1e-5, 1e-7);

        // Define the function to be minimized
        UnivariateFunction function = new UnivariateFunction() {
            public double value(double x) {
                return (x - 2) * (x - 2);
            }
        };

        // Set the function to be minimized
        optimizer.setFunction(function);

        // Set the optimization parameters
        optimizer.setMin(0);
        optimizer.setMax(4);
        optimizer.setStartValue(3);

        // Create a custom convergence checker
        ConvergenceChecker<UnivariatePointValuePair> checker = new ConvergenceChecker<UnivariatePointValuePair>() {
            public boolean converged(int iter, UnivariatePointValuePair previous, UnivariatePointValuePair current) {
                // Check if the function value has converged
                return Math.abs(current.getValue() - previous.getValue()) < 1e-9;
            }
        };

        // Set the convergence checker
        optimizer.setConvergenceChecker(checker);

        // Perform the optimization
        UnivariatePointValuePair result = optimizer.doOptimize();

        // Check the result
        assertEquals(2, result.getPoint(), 1e-5);
    }

}
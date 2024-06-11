import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.junit.Assert;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithMaxEval_fReturnsValidRoot() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Define the function to be solved
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        };

        // Solve the equation using solve method with maxEval
        double result = solver.solve(100, function, 0, 10);

        // Verify the result
        Assert.assertEquals(2, result, 1e-6);
    }

    @Test
    public void testSolve_WithValidBounds_ReturnsValidRoot() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        // Define the function to be solved
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x); // f(x) = sin(x), which has a root at x = 0
            }
        };

        // Solve the equation using solve method with valid bounds
        double result = solver.solve(100, function, -1, 1);

        // Verify the result
        Assert.assertEquals(0, result, 1e-6);
    }

    @Test
    public void testSolve_WithInvalidBounds_ThrowsIllegalArgumentException() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        // Define the function to be solved
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sqrt(-x); // f(x) = sqrt(-x), which has no roots for real numbers
            }
        };

        // Try to solve the equation using solve method with invalid bounds
        try {
            solver.solve(100, function, 0, 10);
            Assert.fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
            Assert.assertEquals("Function values at endpoints do not have different signs.", e.getMessage());
        }
    }

    @Test
    public void testSolve_WithFunctionValueAccuracy_ReturnsValidRoot() {
        // Create an instance of BaseSecantSolver
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Define the function to be solved
        UnivariateRealFunction function = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.pow(x, 3) - 3 * Math.pow(x, 2) - 9 * x + 27; // f(x) = x^3 - 3x^2 - 9x + 27
            }
        };

        // Solve the equation using solve method with function value accuracy
        double result = solver.solve(100, function, -10, 10);

        // Verify the result
        Assert.assertEquals(3, result, 1e-6);
    }

}
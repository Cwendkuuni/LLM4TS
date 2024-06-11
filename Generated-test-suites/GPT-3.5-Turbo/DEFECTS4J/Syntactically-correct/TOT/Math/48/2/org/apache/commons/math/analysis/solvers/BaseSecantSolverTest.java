import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.math.analysis.UnivariateRealFunction;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WithValidFunction_ReturnsRoot() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);

        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolve_WithFunctionThatDoesNotConverge_ThrowsConvergenceException() {
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x) + 1;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);

        try {
            double result = solver.solve(100, f, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
            fail("Expected ConvergenceException to be thrown");
        } catch (ConvergenceException e) {
            // success
        }
    }

    @Test
    public void testSolve_WithFuntionThatHasMultipleSolutions_ReturnsCorrectSolutionBasedOnAllowedSolution() {
        // Function: f(x) = x^2 - 4
        // Roots: x = -2 and x = 2

        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        };

        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);

        // test with allowed solution as ANY_SIDE
        double result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);

        // test with allowed solution as LEFT_SIDE
        result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(-2.0, result, 1e-6);

        // test with allowed solution as RIGHT_SIDE
        result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, result, 1e-6);

        // test with allowed solution as BELOW_SIDE
        result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(-2.0, result, 1e-6);

        // test with allowed solution as ABOVE_SIDE
        result = solver.solve(100, f, 0, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

}
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_RegularCase() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        double expected = 2.0; // The exact root is 2.0
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_NoRootWithinBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5; // f(x) = x - 5
            }
        }, 0, 4, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        // No root within the bounds, expected NaN
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testSolve_ExactRootOnTheBoundary() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 10; // f(x) = x - 10
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        double expected = 10.0; // The exact root is 10.0, which is on the upper boundary
        assertEquals(expected, result, 1e-6);
    }

    @Test
    public void testSolve_RelativeAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-4, 1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        double expected = 2.0; // The exact root is 2.0
        assertEquals(expected, result, 1e-4);
    }

    @Test
    public void testSolve_MaximumNumberOfEvaluations() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(5, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4; // f(x) = x^2 - 4
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        // Maximum number of evaluations reached, expected NaN
        assertTrue(Double.isNaN(result));
    }
}
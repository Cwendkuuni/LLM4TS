import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolveForMaxEval() {
        // Test solving for max evaluations
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 2; // Root at x = 2
            }
        };
        double result = solver.solve(100, f, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolveForLeftSideSolution() {
        // Test looking for solution on the left side
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x + 5; // Root at x = -5
            }
        };
        double result = solver.solve(100, f, -10, 0, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(-5.0, result, 1e-6);
    }

    @Test
    public void testSolveForAboveSideSolution() {
        // Test looking for solution on above side
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x - 7; // Root at x = 7
            }
        };
        double result = solver.solve(100, f, 5, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(7.0, result, 1e-6);
    }

    @Test
    public void testSolveForInvalidStartValue() {
        // Test solving with an invalid start value
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 16; // Root at x = 4
            }
        };
        assertEquals(4.0, solver.solve(100, f, 0, 5, 6, BaseSecantSolver.AllowedSolution.ANY_SIDE), 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveForNoConvergence() {
        // Test for no convergence
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        UnivariateRealFunction f = new UnivariateRealFunction() {
            public double value(double x) {
                return 1; // No root
            }
        };
        solver.solve(100, f, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }
}
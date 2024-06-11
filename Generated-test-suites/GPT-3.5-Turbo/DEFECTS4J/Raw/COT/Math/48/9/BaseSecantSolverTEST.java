
import org.apache.commons.math.exception.ConvergenceException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.BaseSecantSolver;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test(expected = ConvergenceException.class)
    public void testSolve_ThrowsConvergenceException() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return 1.0; // Will never converge
            }
        };

        solver.solve(100, null, -10, 10);
    }

    @Test
    public void testSolve_SolutionOnLeftSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x + 2; // Simple linear function with root at -2
            }
        };

        double solution = solver.solve(100, null, -10, 1);
        assertEquals(-2.0, solution, 1e-6);
    }

    @Test
    public void testSolve_SolutionOnRightSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x - 2; // Simple linear function with root at 2
            }
        };

        double solution = solver.solve(100, null, -1, 10);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolve_AnySolutionAllowed() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x - 4; // Quadratic function with roots at -2 and 2
            }
        };

        double solution = solver.solve(100, null, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, solution, 1e-6);

        solution = solver.solve(100, null, -10, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(-2.0, solution, 1e-6);

        solution = solver.solve(100, null, -10, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, solution, 1e-6);

        solution = solver.solve(100, null, -10, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(-2.0, solution, 1e-6);

        solution = solver.solve(100, null, -10, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(2.0, solution, 1e-6);
    }

    @Test
    public void testSolve_MaximumNumberOfEvaluationsReached() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x; // Derivative is always 1
            }
        };

        double solution = solver.solve(3, null, -10, 10); // Will require 4 iterations
        assertEquals(3.0, solution, 1e-6);
    }

    @Test
    public void testSolve_FunctionValueAccuracyReached() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, 1e-3, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                return x * x * x - 4; // Cubic function with root near 1.587
            }
        };

        double solution = solver.solve(100, null, -10, 10);
        assertEquals(1.587401, solution, 1e-6);
    }
}

import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_exactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                // Define a function with exact root at x = 5
                return x - 5;
            }
        };

        double root = solver.solve(100, null, 4, 6);
        assertEquals(5, root, 1e-6);
    }

    @Test
    public void testSolve_approximateRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                // Define a function with approximate root at x = 5
                return x*x - 26 * x + 125;
            }
        };

        double root = solver.solve(100, null, 4, 6);
        assertEquals(5, root, 1e-6);
    }

    @Test
    public void testSolve_maxEvalReached() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                // Define a function with no root, to reach maxEval
                return x + 1;
            }
        };

        double root = solver.solve(10, null, 0, 10);
        assertEquals(Double.NaN, root, 1e-6);
    }

    @Test
    public void testSolve_outsideBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                // Define a function with root outside bounds
                return x - 10;
            }
        };

        double root = solver.solve(100, null, 1, 5);
        assertEquals(Double.NaN, root, 1e-6);
    }

    @Test
    public void testSolve_allowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                // Define a function with root at x = 5
                return x - 5;
            }
        };

        double root = solver.solve(100, null, 4, 6, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(5, root, 1e-6);

        root = solver.solve(100, null, 4, 6, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(Double.NaN, root, 1e-6);
    }

}
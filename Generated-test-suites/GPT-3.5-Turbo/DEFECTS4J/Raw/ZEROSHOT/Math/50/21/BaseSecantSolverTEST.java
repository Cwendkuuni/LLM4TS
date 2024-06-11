import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
        };

        double result = solver.solve(100, null, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
        };

        double result = solver.solve(100, null, 1, 3, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
        };

        double result = solver.solve(100, null, 1, 3, 2, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolveMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
        };

        double result = solver.solve(10, null, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolveInvalidMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return x * x - 4;
            }
        };

        solver.solve(-10, null, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }
}
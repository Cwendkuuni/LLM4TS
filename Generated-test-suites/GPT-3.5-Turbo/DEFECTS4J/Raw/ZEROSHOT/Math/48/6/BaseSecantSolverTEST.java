import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return Math.pow(x, 3) - x - 1;
            }
        };

        double result = solver.solve(100, 0, 2);
        double expected = 1.3246;
        double delta = 1e-4;

        assertEquals(expected, result, delta);
    }

    @Test
    public void testSolve_WithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            protected double computeObjectiveValue(double x) {
                return Math.pow(x, 2) - 2;
            }
        };

        double result = solver.solve(100, 0, 2, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        double expected = 1.4142;
        double delta = 1e-4;

        assertEquals(expected, result, delta);
    }

    private void assertEquals(double expected, double result, double delta) {
        if (Math.abs(expected - result) > delta) {
            throw new AssertionError("Expected: " + expected + ", but got: " + result);
        }
    }
}
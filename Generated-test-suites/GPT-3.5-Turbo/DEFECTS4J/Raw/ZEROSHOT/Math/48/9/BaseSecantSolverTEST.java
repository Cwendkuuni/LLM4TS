import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test case 1
        double result1 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2, result1, 1e-6);

        // Test case 2
        double result2 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0, result2, 1e-6);

        // Test case 3
        double result3 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5, result3, 1e-6);
    }

    @Test
    public void testSolve_WithAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);

        // Test case 1
        double result1 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(2, result1, 1e-6);

        // Test case 2
        double result2 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return Math.sin(x);
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(0, result2, 1e-6);

        // Test case 3
        double result3 = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, -10, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(5, result3, 1e-6);
    }
}
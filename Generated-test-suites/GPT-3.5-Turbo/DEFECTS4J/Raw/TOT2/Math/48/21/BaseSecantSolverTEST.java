import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    // Test case for solve method with default allowed solution
    @Test
    public void testSolveWithDefaultAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5);
        assertEquals(2.0, result, 1e-6);
    }

    // Test case for solve method with specified allowed solution
    @Test
    public void testSolveWithSpecifiedAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(-2.0, result, 1e-6);
    }

    // Test case for solve method with invalid interval
    @Test(expected = ConvergenceException.class)
    public void testSolveWithInvalidInterval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 5, 0);
    }

    // Test case for solve method with max iterations exceeded
    @Test(expected = MaxEvaluationsExceededException.class)
    public void testSolveWithMaxIterationsExceeded() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(10, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 0, 5);
    }

    // Test case for solve method with function value too small
    @Test
    public void testSolveWithFunctionValueTooSmall() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x * x - 4;
            }
        }, 1, 3);
        assertEquals(2.0, result, 1e-6);
    }
}
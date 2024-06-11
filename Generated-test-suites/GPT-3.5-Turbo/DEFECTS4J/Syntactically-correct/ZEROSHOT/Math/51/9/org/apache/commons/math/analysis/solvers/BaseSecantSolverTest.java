import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    private BaseSecantSolver solver;

    @Test
    public void testSolve_ExactRoot() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5, result, 1e-6);
    }

    @Test
    public void testSolve_NoRoot() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, 0, 4, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(Double.NaN, result, 1e-6);
    }

    @Test
    public void testSolve_LeftSideSolution() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(5, result, 1e-6);
    }

    @Test
    public void testSolve_RightSideSolution() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(5, result, 1e-6);
    }

    @Test
    public void testSolve_BelowSideSolution() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(5, result, 1e-6);
    }

    @Test
    public void testSolve_AboveSideSolution() {
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return x - 5;
            }
        }, 0, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(5, result, 1e-6);
    }
}
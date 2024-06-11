import static org.junit.Assert.*;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_anySide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return Math.cos(x) - x;
            }
        }, 0, 2 * Math.PI, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.739085133215259, result, 1e-6);
    }

    @Test
    public void testSolve_leftSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return Math.cos(x) - x;
            }
        }, 0, 2 * Math.PI, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(0.739085133215259, result, 1e-6);
    }

    @Test
    public void testSolve_rightSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return Math.cos(x) - x;
            }
        }, 0, 2 * Math.PI, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(5.425184879247767, result, 1e-6);
    }

    @Test
    public void testSolve_belowSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return Math.cos(x) - x;
            }
        }, 0, 2 * Math.PI, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(0.739085133215259, result, 1e-6);
    }

    @Test
    public void testSolve_aboveSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, new UnivariateRealFunction() {
            public double value(double x) {
                return Math.cos(x) - x;
            }
        }, 0, 2 * Math.PI, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(5.425184879247767, result, 1e-6);
    }
}
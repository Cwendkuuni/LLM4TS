import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_WhenRootExistsInAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, new TestFunction(), -1, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, solution, 1e-6);
    }
    
    @Test
    public void testSolve_WhenRootExistsInLeftSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, new TestFunction(), -1, 1, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(-0.5, solution, 1e-6);
    }

    @Test
    public void testSolve_WhenRootExistsInRightSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, new TestFunction(), -1, 1, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(0.5, solution, 1e-6);
    }

    @Test
    public void testSolve_WhenRootExistsInBelowSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, new TestFunction(), -1, 1, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(-0.5, solution, 1e-6);
    }

    @Test
    public void testSolve_WhenRootExistsInAboveSide() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, new TestFunction(), -1, 1, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(0.0, solution, 1e-6);
    }

    @Test(timeout = 1000)
    public void testSolve_WhenMaxIterationsReached() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(5, new TestFunction(), -1, 1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSolve_WhenInvalidBoundsAndStartValue() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, new TestFunction(), 1, -1, BaseSecantSolver.AllowedSolution.ANY_SIDE);
    }

    private class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x * x - 1;
        }
    }
}
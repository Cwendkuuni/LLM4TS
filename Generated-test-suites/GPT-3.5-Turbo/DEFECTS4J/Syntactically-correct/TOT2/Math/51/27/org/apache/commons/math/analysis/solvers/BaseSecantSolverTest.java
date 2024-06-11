import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolveWithAllowedSideAny() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, x -> x - 5, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5, root, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSideLeft() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(100, x -> x - 5, 0, 10, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(5, root, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSideRight() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double root = solver.solve(100, x -> x - 5, 0, 10, BaseSecantSolver.AllowedSolution.RIGHT_SIDE);
        assertEquals(5, root, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSideBelow() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, x -> x - 5, 0, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(5, root, 1e-6);
    }

    @Test
    public void testSolveWithAllowedSideAbove() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double root = solver.solve(100, x -> x - 5, 0, 10, BaseSecantSolver.AllowedSolution.ABOVE_SIDE);
        assertEquals(5, root, 1e-6);
    }

}
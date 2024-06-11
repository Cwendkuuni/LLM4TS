import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testDefaultConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        assertNotNull(solver);
    }

    @Test
    public void testRelativeAccuracyConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-12, 1e-6, Method.ILLINOIS);
        assertNotNull(solver);
    }

    @Test
    public void testAbsoluteAccuracyConstructor() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-12, 1e-6, 1e-8, Method.PEGASUS);
        assertNotNull(solver);
    }

    @Test
    public void testSolveWithMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        double root = solver.solve(100, x -> x * x - 4, 0, 3, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test
    public void testSolveWithoutMaxEval() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        double root = solver.solve(x -> x * x - 4, 0, 3, AllowedSolution.ANY_SIDE);
        assertEquals(2.0, root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolveMaxIterReached() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, Method.REGULA_FALSI);
        solver.solve(1, x -> x * x - 4, 0, 3, AllowedSolution.ANY_SIDE);
    }

    @Test(expected = MathInternalError.class)
    public void testIllegalMethod() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, null);
    }

}
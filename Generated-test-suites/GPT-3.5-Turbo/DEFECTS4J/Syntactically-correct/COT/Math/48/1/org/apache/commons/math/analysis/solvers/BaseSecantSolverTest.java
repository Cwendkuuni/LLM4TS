
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve_ExactRoot() {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, new TestFunction(), 0, 10, 5);
        assertEquals(5.0, root, 1e-6);
    }

    @Test
    public void testSolve_BracketingInterval() {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, new TestFunction(), 0, 10, 3);
        assertTrue(root > 0 && root < 10);
    }

    @Test
    public void testSolve_FunctionValueAccuracy() {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, 1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, new TestFunction(), 0, 10, 5);
        assertEquals(5.0, root, 1e-6);
    }

    @Test
    public void testSolve_AbsoluteAccuracy() {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, new TestFunction(), 0, 10, 5);
        assertEquals(5.0, root, 1e-6);
    }

    @Test
    public void testSolve_RelativeAccuracy() {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, 1e-8, BaseSecantSolver.Method.REGULA_FALSI);
        double root = solver.solve(100, new TestFunction(), 0, 10, 5);
        assertEquals(5.0, root, 1e-6);
    }

    @Test(expected = ConvergenceException.class)
    public void testSolve_ConvergenceException() {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(5, new TestFunction(), 0, 10, 5);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_MathInternalError() {
        BaseSecantSolver solver = new TestSecantSolver(1e-6, BaseSecantSolver.Method.SECANT);
        solver.solve(100, new TestFunction(), 0, 10, 5);
    }

}

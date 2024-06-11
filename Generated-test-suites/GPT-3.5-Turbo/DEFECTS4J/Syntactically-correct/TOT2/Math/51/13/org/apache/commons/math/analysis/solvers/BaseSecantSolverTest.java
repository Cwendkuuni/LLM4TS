import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class BaseSecantSolverTest {

    @Test
    public void testDefaultAbsoluteAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, x -> x*x - 4, 0, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testDoSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.setFunctionValueAccuracy(1e-12);
        double result = solver.solve(100, x -> x*x - 4, 0, 5, 1.0, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertTrue(solver.getEvaluations() <= 100);
    }
}
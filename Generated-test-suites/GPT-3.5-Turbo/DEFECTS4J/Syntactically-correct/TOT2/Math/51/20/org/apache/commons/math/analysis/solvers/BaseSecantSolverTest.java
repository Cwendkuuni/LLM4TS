import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testConstruct() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        assertEquals(1e-6, solver.getAbsoluteAccuracy(), 0.0);
        assertEquals(BaseSecantSolver.Method.REGULA_FALSI, solver.getMethod());
    }

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double solution = solver.solve(100, Math::cos, 0.0, Math.PI, AllowedSolution.ANY_SIDE);
        assertEquals(Math.PI, solution, 1e-6);
    }

    @Test
    public void testSolveWithInvalidBounds() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double solution = solver.solve(100, Math::sin, Math.PI, 2 * Math.PI, AllowedSolution.ANY_SIDE);
        assertEquals(Double.NaN, solution, 0.0);
    }

    @Test
    public void testSolveWithZeroFunctionValueAccuracy() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double solution = solver.solve(100, Math::exp, 0.0, 1.0, AllowedSolution.ABOVE_SIDE);
        assertEquals(1.0, solution, 1e-6);
    }

}
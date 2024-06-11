import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, x -> x * x - 4, 0, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(Math.sqrt(4), result, 1e-6);
    }

    @Test
    public void testSolve_MaxEval_Zero() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(0, x -> x * x - 4, 0, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertTrue(Double.isNaN(result));
    }

    @Test
    public void testSolve_AllowedSolution_LeftSide() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, x -> x * x - 4, 0, 3, BaseSecantSolver.AllowedSolution.LEFT_SIDE);
        assertEquals(-Math.sqrt(4), result, 1e-6);
    }

    @Test
    public void testSolve_FunctionValueTooSmall() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, x -> 1e-10, 0, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(0.0, result, 1e-6);
    }

    @Test(expected = MathInternalError.class)
    public void testSolve_InvalidAllowedSolution() {
        BaseSecantSolver solver = new BaseSecantSolverImpl(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        solver.solve(100, x -> x * x - 4, 0, 3, BaseSecantSolver.AllowedSolution.valueOf("INVALID"));
    }

}

class BaseSecantSolverImpl extends BaseSecantSolver {

    protected BaseSecantSolverImpl(final double absoluteAccuracy, final Method method) {
        super(absoluteAccuracy, method);
    }

    @Override
    protected double computeObjectiveValue(double x) {
        return computeObjectiveValue(x);
    }
}
import org.junit.Test;
import static org.junit.Assert.*;

public class BaseSecantSolverTest {

    @Test
    public void testSolveWithMaxEvalAndAllowed() {
        // Test solving with maximum evaluations and allowed solution
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, x -> x - 5, 0, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolveWithMaxEval() {
        // Test solving with maximum evaluations
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        double result = solver.solve(100, x -> x - 5, 0, 10);
        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolveWithAllowed() {
        // Test solving with allowed solution
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.PEGASUS);
        double result = solver.solve(100, x -> x - 5, 0, 10, BaseSecantSolver.AllowedSolution.BELOW_SIDE);
        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testSolve() {
        // Test solving
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, x -> x - 5, 0, 10, 5);
        assertEquals(5.0, result, 1e-6);
    }

    @Test
    public void testDoSolve() {
        // Test internal method doSolve
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS);
        solver.setMaxEvaluations(100);
        double result = solver.doSolve();
        assertEquals(0.0, result, 1e-6);
    }
}
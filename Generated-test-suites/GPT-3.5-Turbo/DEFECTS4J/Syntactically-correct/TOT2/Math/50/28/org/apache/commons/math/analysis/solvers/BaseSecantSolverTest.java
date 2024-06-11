import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testSolve() {
        // Test regular functionality
        BaseSecantSolver solver = new BaseSecantSolverMock(0.0001, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, x -> x - 5, 2, 8, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5.0, result, 0.0001);
    }

    @Test
    public void testSolveWithInvalidSolution() {
        // Test case where no solution is found within the given constraints
        BaseSecantSolver solver = new BaseSecantSolverMock(0.0001, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, x -> x - 5, 2, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(Double.NaN, result, 0.0001);
    }

    @Test
    public void testSolveWithFunctionValueAtRoot() {
        // Test case where the function value at the root is zero
        BaseSecantSolver solver = new BaseSecantSolverMock(0.0001, BaseSecantSolver.Method.REGULA_FALSI);
        double result = solver.solve(100, x -> x - 5, 2, 5, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5.0, result, 0.0001);
    }

}

class BaseSecantSolverMock extends BaseSecantSolver {

    public BaseSecantSolverMock(double absoluteAccuracy, Method method) {
        super(absoluteAccuracy, method);
    }

    @Override
    protected double computeObjectiveValue(double x) {
        // Mock implementation for the objective function x - 5
        return x - 5;
    }
}
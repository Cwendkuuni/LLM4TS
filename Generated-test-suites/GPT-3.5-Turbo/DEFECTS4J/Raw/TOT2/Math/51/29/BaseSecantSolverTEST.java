import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BaseSecantSolverTest {

    @Test
    public void testSolveBasic() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.REGULA_FALSI) {
            @Override
            protected double computeObjectiveValue(double x) {
                // Function for testing purposes
                return x * x - 4;
            }
        };
        double result = solver.solve(100, null, 1, 3, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(2.0, result, 1e-6);
    }

    @Test
    public void testSolveExactRoot() {
        BaseSecantSolver solver = new BaseSecantSolver(1e-6, BaseSecantSolver.Method.ILLINOIS) {
            @Override
            protected double computeObjectiveValue(double x) {
                // Function for testing purposes
                return x - 5;
            }
        };
        double result = solver.solve(100, null, 5, 10, BaseSecantSolver.AllowedSolution.ANY_SIDE);
        assertEquals(5.0, result, 1e-6);
    }

    // Add more test cases here for edge cases and exceptions

}
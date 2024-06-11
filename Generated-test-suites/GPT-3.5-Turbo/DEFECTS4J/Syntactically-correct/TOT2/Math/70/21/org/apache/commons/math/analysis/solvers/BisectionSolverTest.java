import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

    @Test
    public void testSolveWithFunction() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(x -> x * x - 4, 1, 3); // Function x^2 - 4 has a root at x = 2
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithFunctionAndInitial() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(x -> x * x - 4, 1, 3, 2); // Function x^2 - 4 has a root at x = 2
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithErrorInterval() {
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(3, 1);
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            assertEquals("Exception thrown for invalid interval", e.getMessage());
        }
    }

    @Test
    public void testSolveWithMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(x -> x, 1, 3); // Function x does not have a root in the interval [1, 3]
        } catch (MaxIterationsExceededException | FunctionEvaluationException e) {
            assertEquals("Max iterations exceeded without finding root", e.getMessage());
        }
    }
}
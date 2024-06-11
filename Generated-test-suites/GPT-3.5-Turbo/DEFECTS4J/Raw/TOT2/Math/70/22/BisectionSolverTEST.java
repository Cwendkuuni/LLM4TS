import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

    @Test
    public void testSolveWithFunction() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(x -> x*x - 4, 0, 3);
        assertEquals(2.0, result, 1E-6);
    }

    @Test
    public void testSolveWithFunctionAndInitialValue() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(x -> x*x - 4, 0, 3, 1);
        assertEquals(2.0, result, 1E-6);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(x -> x*x - 4, -10, -1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationError() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(x -> 1 / (x - 1), 0, 3);
    }

    @Test
    public void testBoundaryCase() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(x -> x*x - 4, 2, 2);
        assertEquals(2.0, result, 1E-6);
    }
}
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {
    
    @Test
    public void testSolveWithFunctionParam() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(x -> x - 2, 1, 3);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithMinMaxParam() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(1, 3, 2);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveInitial() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(x -> x - 2, 1, 3, 2);
        assertEquals(2.0, result, 0.0001);
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(x -> x, -1, 1);
    }

    @Test(expected = FunctionEvaluationException.class)
    public void testFunctionEvaluationException() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(x -> {throw new FunctionEvaluationException();}, 1, 3);
    }
}
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class BisectionSolverTest {

    @Test
    public void testSolveWithFunction() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(x -> x * x - 4, -2, 2);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveWithFunctionAndInitial() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(x -> x * x - 4, -2, 2, 1.0);
        assertEquals(2.0, result, 0.0001);
    }

    @Test
    public void testSolveExceptionHandling() {
        BisectionSolver solver = new BisectionSolver();
        try {
            solver.solve(x -> x * x - 4, 2, -2);
        } catch (Exception e) {
            assertEquals(MaxIterationsExceededException.class, e.getClass());
        }
    }

    @Test
    public void testSolveEdgeCases() {
        BisectionSolver solver = new BisectionSolver();
        double result1 = solver.solve(x -> x * x - 4, -2, 0); // Edge case: root at min
        assertEquals(0.0, result1, 0.0001);

        double result2 = solver.solve(x -> x * x - 4, 0, 2); // Edge case: root at max
        assertEquals(2.0, result2, 0.0001);
    }
}
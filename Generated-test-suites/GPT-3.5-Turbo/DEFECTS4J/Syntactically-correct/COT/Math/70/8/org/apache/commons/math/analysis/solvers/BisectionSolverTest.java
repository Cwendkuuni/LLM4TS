
import org.junit.Test;
import org.apache.commons.math.analysis.solvers.BisectionSolver;

public class BisectionSolverTest {

    @Test
    public void testSolveMinAndMax() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(-10, 10);
        // Assert result
    }

    @Test
    public void testSolveMinAndMaxWithInitial() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(-10, 10, 5);
        // Assert result
    }

    @Test
    public void testSolveMinMaxAndInitial() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve((x) -> Math.exp(x) - 5, -10, 10, 5);
        // Assert result
    }

    @Test
    public void testSolveMinMaxAndInitialWithCustomFunction() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve((x) -> Math.sin(x) - 0.5, -10, 10, 5);
        // Assert result
    }

    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveMaxIterationsExceededException() throws Exception {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve((x) -> x - 1, -10, 10, 5);
    }

}

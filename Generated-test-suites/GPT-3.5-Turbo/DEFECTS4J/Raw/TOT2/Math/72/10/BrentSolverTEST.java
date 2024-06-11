import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        // Typical use case
        // Test with function f(x) = x^2 - 4, interval [-2, 2]
        // Root should be 2.0
        boolean rootFound = solver.findRoot(x -> x * x - 4, new ConvergenceChecker(), 100, -2.0, 0.0, 2.0, 0.0);
        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 1e-10);

        // Edge cases
        // Test with function f(x) = sin(x), interval [0, PI]
        // Root should be PI
        rootFound = solver.findRoot(x -> Math.sin(x), new ConvergenceChecker(), 100, 0.0, 0.0, Math.PI, 0.0);
        assertTrue(rootFound);
        assertEquals(Math.PI, solver.getRoot(), 1e-10);
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        // Edge cases
        // Check if getRoot() returns Double.NaN when no root is found
        assertEquals(Double.NaN, solver.getRoot(), 0.0);
    }
}
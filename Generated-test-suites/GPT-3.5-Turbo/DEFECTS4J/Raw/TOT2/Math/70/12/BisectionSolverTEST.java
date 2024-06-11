import static org.junit.Assert.*;
import org.junit.Test;

public class BisectionSolverTest {

    // Test case for BisectionSolver constructor
    @Test
    public void testConstructor() {
        BisectionSolver solver = new BisectionSolver();
        assertNotNull(solver);
    }

    // Test case for solve method with initial value
    @Test
    public void testSolveWithInitial() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(new TestFunction(), 0, 1, 0.5);
        assertEquals(0.5, result, 1e-6);
    }

    // Test case for solve method without initial value
    @Test
    public void testSolveWithoutInitial() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(new TestFunction(), 0, 1);
        assertEquals(0.5, result, 1e-6);
    }

    // Test case for solve method with edge case where min equals max
    @Test
    public void testSolveWithEqualMinMax() {
        BisectionSolver solver = new BisectionSolver();
        double result = solver.solve(new TestFunction(), 0, 0);
        assertEquals(0.0, result, 1e-6);
    }

    // Test case for solve method with maximum iterations exceeded
    @Test(expected = MaxIterationsExceededException.class)
    public void testSolveWithMaxIterationsExceeded() {
        BisectionSolver solver = new BisectionSolver();
        solver.solve(new TestFunction(), -1, 1);
    }

    // Mock class for testing
    private class TestFunction implements UnivariateRealFunction {
        public double value(double x) {
            return x - 0.5;
        }
    }
}
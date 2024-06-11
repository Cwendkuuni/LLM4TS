import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testConstructor() {
        BrentSolver solver = new BrentSolver();
        assertNotNull(solver);
    }

    @Test
    public void testFindRoot() {
        // Typical Use Case
        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(new TestFunction(), new TestChecker(), 100, 0.0, -1.0, 1.0, 1.0);
        
        assertTrue(rootFound);
        assertEquals(0.0, solver.getRoot(), 1e-15);
    }

    @Test
    public void testGetRoot() {
        // Edge Case
        BrentSolver solver = new BrentSolver();
        assertEquals(Double.NaN, solver.getRoot(), 0.0);
    }

    private static class TestFunction implements ComputableFunction {
        public double valueAt(double x) {
            return x * x - 4;
        }
    }

    private static class TestChecker implements ConvergenceChecker {
        public int converged(double xLow, double fLow, double xHigh, double fHigh) {
            if (Math.abs(fLow) < 1e-15) {
                return LOW;
            } else if (Math.abs(fHigh) < 1e-15) {
                return HIGH;
            } else {
                return UNCONVERGED;
            }
        }
    }

}
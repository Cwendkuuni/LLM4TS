import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        // Typical use case
        BrentSolver solver = new BrentSolver();
        double expectedRoot = 2.0;
        double tolerance = 1e-6;
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return ConvergenceChecker.LOW;
            }
        };
        try {
            solver.findRoot(function, checker, 100, -3.0, -7.0, 5.0, 21.0);
        } catch (FunctionException e) {
            e.printStackTrace();
        }
        assertEquals(expectedRoot, solver.getRoot(), tolerance);
        // Edge case with nan as input
        assertTrue(Double.isNaN(solver.getRoot()));
        // Edge case with positive and negative infinity as input
        try {
            solver.findRoot(function, checker, 100, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
            assertTrue(Double.isNaN(solver.getRoot())); // Expecting root to be NaN
        } catch (FunctionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetRoot() {
        // Typical use case
        BrentSolver solver = new BrentSolver();
        double expectedRoot = 5.0;
        solver.getRoot();
        solver.findRoot(new ComputableFunction() {
            public double valueAt(double x) {
                return x;
            }
        }, new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return ConvergenceChecker.HIGH;
            }
        }, 100, -10.0, -10.0, 20.0, 20.0);
        assertEquals(expectedRoot, solver.getRoot(), 0.0);
    }

}
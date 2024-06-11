import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();
        
        // Typical use case
        assertTrue(solver.findRoot(new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        }, new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        }, 100, 0, -4, 10, 96));
        
        // Edge case: empty interval
        assertFalse(solver.findRoot(new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        }, new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        }, 100, 0, 4, 0, -4));

        // Error scenario: function throws an exception
        try {
            solver.findRoot(new ComputableFunction() {
                public double valueAt(double x) {
                    throw new FunctionException("Function evaluation error");
                }
            }, new ConvergenceChecker() {
                public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                    return ConvergenceChecker.NONE;
                }
            }, 100, 0, -4, 10, 96);
            fail("FunctionException not thrown");
        } catch (FunctionException e) {
            // Expected behavior
        }
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        assertEquals(Double.NaN, solver.getRoot(), 0.0);

        solver.findRoot(new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        }, new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return ConvergenceChecker.LOW;
            }
        }, 100, 0, -4, 10, 96);

        assertEquals(2.0, solver.getRoot(), 1e-6);
    }
}
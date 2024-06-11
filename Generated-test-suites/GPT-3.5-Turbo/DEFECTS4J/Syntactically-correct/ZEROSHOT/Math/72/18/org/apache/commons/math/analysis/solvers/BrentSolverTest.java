import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        BrentSolver solver = new BrentSolver();

        // Test case 1
        ComputableFunction function1 = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker1 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        };

        double x0 = -2.0;
        double f0 = function1.valueAt(x0);
        double x1 = 2.0;
        double f1 = function1.valueAt(x1);

        boolean result1 = solver.findRoot(function1, checker1, 100, x0, f0, x1, f1);
        assertTrue(result1);
        assertEquals(2.0, solver.getRoot(), 1e-6);

        // Test case 2
        ComputableFunction function2 = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.sin(x);
            }
        };
        ConvergenceChecker checker2 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        };

        x0 = 1.0;
        f0 = function2.valueAt(x0);
        x1 = 4.0;
        f1 = function2.valueAt(x1);

        boolean result2 = solver.findRoot(function2, checker2, 100, x0, f0, x1, f1);
        assertFalse(result2);
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();

        // Test case 1
        ComputableFunction function1 = new ComputableFunction() {
            public double valueAt(double x) {
                return x - 5;
            }
        };

        double x0 = 4.0;
        double f0 = function1.valueAt(x0);
        double x1 = 6.0;
        double f1 = function1.valueAt(x1);

        boolean result1 = solver.findRoot(function1, new ConvergenceChecker(), 100, x0, f0, x1, f1);
        assertTrue(result1);
        assertEquals(5.0, solver.getRoot(), 1e-6);
    }
}
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class BrentSolverTest {

    private BrentSolver solver;

    @Before
    public void setUp() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot() throws FunctionException {
        // Test for an interval that contains a root
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 16;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                } else {
                    return NONE;
                }
            }
        };
        double x0 = -4;
        double f0 = function.valueAt(x0);
        double x1 = 4;
        double f1 = function.valueAt(x1);

        boolean result = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
        assertTrue(result);
        assertEquals(4.0, solver.getRoot(), 1e-6);

        // Test for an interval that does not contain a root
        ComputableFunction function2 = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x + 16;
            }
        };
        x0 = -4;
        f0 = function2.valueAt(x0);
        x1 = 4;
        f1 = function2.valueAt(x1);

        result = solver.findRoot(function2, checker, 100, x0, f0, x1, f1);
        assertFalse(result);
    }

    @Test
    public void testGetRoot() {
        // Test when no root has been found
        double root = solver.getRoot();
        assertTrue(Double.isNaN(root));

        // Test when a root has been found
        solver.findRoot(new ComputableFunction() {
            public double valueAt(double x) {
                return x - 3;
            }
        }, new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                } else {
                    return NONE;
                }
            }
        }, 100, 0, -3, 5, 2);

        root = solver.getRoot();
        assertEquals(3.0, root, 1e-6);
    }
}
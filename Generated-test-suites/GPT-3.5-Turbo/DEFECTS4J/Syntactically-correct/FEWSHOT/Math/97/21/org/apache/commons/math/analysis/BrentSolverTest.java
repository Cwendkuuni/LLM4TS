import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class BrentSolverTest {
    private BrentSolver solver;

    @Before
    public void setUp() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4.0;
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

        boolean result = solver.findRoot(function, checker, 100, 0.0, -4.0, 3.0, 5.0);
        assertTrue(result);
        assertEquals(2.0, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRootNoRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4.0;
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

        boolean result = solver.findRoot(function, checker, 100, 0.0, -10.0, 3.0, 5.0);
        assertFalse(result);
    }
}
import org.junit.Before;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.RootsFinder.ConvergenceChecker;

import static org.junit.Assert.*;

public class BrentSolverTest {

    private BrentSolver solver;

    @Before
    public void setUp() {
        solver = new BrentSolver();
    }

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return (Math.abs(fLow) < 1e-6) ? LOW : (Math.abs(fHigh) < 1e-6) ? HIGH : UNCONVERGED;
            }
        };

        boolean result = solver.findRoot(function, checker, 100, 1.0, -3.0, 3.0, 7.0);
        assertTrue(result);
        assertEquals(2.0, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRootNoRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x + 1.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return (Math.abs(fLow) < 1e-6) ? LOW : (Math.abs(fHigh) < 1e-6) ? HIGH : UNCONVERGED;
            }
        };

        boolean result = solver.findRoot(function, checker, 100, 1.0, -3.0, 3.0, 7.0);
        assertFalse(result);
    }

    @Test
    public void testFindRootInvalidInterval() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x + 1.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                return (Math.abs(fLow) < 1e-6) ? LOW : (Math.abs(fHigh) < 1e-6) ? HIGH : UNCONVERGED;
            }
        };

        boolean result = solver.findRoot(function, checker, 100, 3.0, 7.0, 1.0, -3.0);
        assertFalse(result);
    }

    @Test
    public void testGetRoot() {
        assertEquals(Double.NaN, solver.getRoot(), 1e-6);
    }
}
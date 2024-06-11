import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot_typical() throws FunctionException {
        // Typical case where the function has a root in the given interval [x0, x1]
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x - 2; // f(x) = x - 2
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;  // Low convergence
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;  // High convergence
                } else {
                    return NO_CONVERGENCE;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean isRootFound = solver.findRoot(function, checker, 100, 1.0, -1.0, 5.0, 3.0);
        assertTrue(isRootFound);
        assertEquals(2.0, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRoot_edge() throws FunctionException {
        // Edge case where the function has a root exactly at the lower bound of the interval x0
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;  // Low convergence
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;  // High convergence
                } else {
                    return NO_CONVERGENCE;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean isRootFound = solver.findRoot(function, checker, 100, 0.0, 0.0, 5.0, 3.0);
        assertTrue(isRootFound);
        assertEquals(0.0, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRoot_error() throws FunctionException {
        // Error case where the function does not have a root in the given interval [x0, x1]
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x - 5; // f(x) = x - 5
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;  // Low convergence
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;  // High convergence
                } else {
                    return NO_CONVERGENCE;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean isRootFound = solver.findRoot(function, checker, 100, 1.0, -1.0, 2.0, -3.0);
        assertFalse(isRootFound);
    }

}
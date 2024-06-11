package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import org.junit.Test;

public class BrentSolverTest {
    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        // Test case 1: Root exists in the interval
        ComputableFunction function1 = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x*x*x - x + 1;
            }
        };

        ConvergenceChecker checker1 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        double x0 = -2.0;
        double f0 = function1.valueAt(x0);
        double x1 = 2.0;
        double f1 = function1.valueAt(x1);

        assertTrue(solver.findRoot(function1, checker1, 100, x0, f0, x1, f1));
        assertEquals(0.6823278038280184, solver.getRoot(), 1e-6);

        // Test case 2: Root does not exist in the interval
        ComputableFunction function2 = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return Math.sin(x);
            }
        };

        ConvergenceChecker checker2 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        x0 = 0.0;
        f0 = function2.valueAt(x0);
        x1 = Math.PI / 2;
        f1 = function2.valueAt(x1);

        assertFalse(solver.findRoot(function2, checker2, 100, x0, f0, x1, f1));
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        solver.getRoot();
    }
}
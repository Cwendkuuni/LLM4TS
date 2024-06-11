package org.spaceroots.mantissa.roots;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        // Test case 1: Root is found within the interval
        assertTrue(solver.findRoot(new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        }, new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow * fHigh < 0) {
                    return ROOT;
                } else if (fLow == 0) {
                    return LOW;
                } else if (fHigh == 0) {
                    return HIGH;
                } else {
                    return UNCONVERGED;
                }
            }
        }, 100, 0, -4, 4, 12));

        assertEquals(2.0, solver.getRoot(), 1e-10);

        // Test case 2: No root is found within the interval
        assertFalse(solver.findRoot(new ComputableFunction() {
            public double valueAt(double x) {
                return Math.sin(x);
            }
        }, new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow * fHigh < 0) {
                    return ROOT;
                } else if (fLow == 0) {
                    return LOW;
                } else if (fHigh == 0) {
                    return HIGH;
                } else {
                    return UNCONVERGED;
                }
            }
        }, 100, 0, -1, 1, 1));

        assertEquals(Double.NaN, solver.getRoot(), 1e-10);
    }

    @Test
    public void testGetRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        // Test case 1: Root has been found
        assertTrue(solver.findRoot(new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        }, new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow * fHigh < 0) {
                    return ROOT;
                } else if (fLow == 0) {
                    return LOW;
                } else if (fHigh == 0) {
                    return HIGH;
                } else {
                    return UNCONVERGED;
                }
            }
        }, 100, 0, -4, 4, 12));

        assertEquals(2.0, solver.getRoot(), 1e-10);

        // Test case 2: No root has been found
        assertFalse(solver.findRoot(new ComputableFunction() {
            public double valueAt(double x) {
                return Math.sin(x);
            }
        }, new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (fLow * fHigh < 0) {
                    return ROOT;
                } else if (fLow == 0) {
                    return LOW;
                } else if (fHigh == 0) {
                    return HIGH;
                } else {
                    return UNCONVERGED;
                }
            }
        }, 100, 0, -1, 1, 1));

        assertEquals(Double.NaN, solver.getRoot(), 1e-10);
    }
}
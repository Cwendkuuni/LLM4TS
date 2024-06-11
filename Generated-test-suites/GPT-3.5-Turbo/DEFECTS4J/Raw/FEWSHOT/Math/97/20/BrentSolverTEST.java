package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {
    private static final double EPSILON = 1e-10;

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        // Test case 1: f(x) = x^2 - 4x + 3, root at x = 1
        ComputableFunction function1 = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x * x - 4 * x + 3;
            }
        };
        ConvergenceChecker checker1 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < EPSILON) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < EPSILON) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        assertTrue(solver.findRoot(function1, checker1, 100, 0, -1, 2, 1));
        assertEquals(1, solver.getRoot(), EPSILON);

        // Test case 2: f(x) = sin(x), root at x = 0
        ComputableFunction function2 = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return Math.sin(x);
            }
        };
        ConvergenceChecker checker2 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < EPSILON) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < EPSILON) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        assertTrue(solver.findRoot(function2, checker2, 100, -1, Math.sin(-1), 1, Math.sin(1)));
        assertEquals(0, solver.getRoot(), EPSILON);

        // Test case 3: f(x) = x^3 - 2x^2 + x - 2, root at x = 2
        ComputableFunction function3 = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x * x * x - 2 * x * x + x - 2;
            }
        };
        ConvergenceChecker checker3 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < EPSILON) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < EPSILON) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        assertTrue(solver.findRoot(function3, checker3, 100, 1, -2, 3, 10));
        assertEquals(2, solver.getRoot(), EPSILON);

        // Test case 4: f(x) = x^2 - 3x + 2, no root
        ComputableFunction function4 = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x * x - 3 * x + 2;
            }
        };
        ConvergenceChecker checker4 = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < EPSILON) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < EPSILON) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        assertFalse(solver.findRoot(function4, checker4, 100, 0, 1, 2, 1));
    }
}
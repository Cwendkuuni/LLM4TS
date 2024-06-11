package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        // Define the function and the convergence checker
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return Math.pow(x, 2) - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                } else if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                } else {
                    return NOT_YET_CONVERGED;
                }
            }
        };

        // Create a BrentSolver object
        BrentSolver solver = new BrentSolver();

        // Test case 1: Function has one root in the interval [1, 2]
        assertTrue(solver.findRoot(function, checker, 100, 1, -3, 2, 0));

        // Test case 2: Function has one root in the interval [3, 4]
        assertTrue(solver.findRoot(function, checker, 100, 3, 5, 4.5, -5));

        // Test case 3: Function has no root in the interval [1, 2]
        assertFalse(solver.findRoot(function, checker, 100, 1, -3, 1.5, -5));

        // Test case 4: Function has multiple roots in the interval [1, 2]
        assertFalse(solver.findRoot(function, checker, 100, 1, -3, 3, 7));
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();

        // Test case 1: No root has been found yet
        assertEquals(Double.NaN, solver.getRoot(), 0);

        // Test case 2: Root has been found
        solver.root = 2.0;
        assertEquals(2.0, solver.getRoot(), 0);
    }

}
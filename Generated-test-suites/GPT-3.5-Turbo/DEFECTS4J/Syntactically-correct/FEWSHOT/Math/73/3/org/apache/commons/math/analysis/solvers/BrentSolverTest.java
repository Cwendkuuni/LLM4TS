package org.spaceroots.mantissa.roots;

import org.junit.Test;
import static org.junit.Assert.*;

public class BrentSolverTest {

    @Test
    public void testFindRoot() {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-10) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-10) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean found = solver.findRoot(function, checker, 100, 0, -4, 2, 0);
        assertTrue(found);
        assertEquals(2.0, solver.getRoot(), 1e-10);

        found = solver.findRoot(function, checker, 100, 0, 4, 2, 0);
        assertTrue(found);
        assertEquals(-2.0, solver.getRoot(), 1e-10);

        found = solver.findRoot(function, checker, 100, 0, 0, 2, -4);
        assertFalse(found);
    }
}
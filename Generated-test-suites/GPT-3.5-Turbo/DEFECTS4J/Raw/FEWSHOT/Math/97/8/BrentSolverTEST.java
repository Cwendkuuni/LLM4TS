package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws Exception {
        BrentSolver solver = new BrentSolver();

        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) {
                return x * x - 4;
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-5) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fHigh) < 1e-5) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NONE;
                }
            }
        };

        assertTrue(solver.findRoot(function, checker, 100, 0, function.valueAt(0), 3, function.valueAt(3)));
        assertEquals(2, solver.getRoot(), 1e-5);

        assertFalse(solver.findRoot(function, checker, 100, 0, function.valueAt(0), 1, function.valueAt(1)));

        assertFalse(solver.findRoot(function, checker, 5, 0, function.valueAt(0), 3, function.valueAt(3)));

    }

}

package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {
    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x*x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(xLow - xHigh) < 1e-6) {
                    return HIGH;
                } else if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                } else {
                    return NONE;
                }
            }
        };

        double x0 = 0;
        double f0 = -4;
        double x1 = 10;
        double f1 = 96;

        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        assertEquals(2.0, solver.getRoot(), 1e-6);

        x0 = -10;
        f0 = 96;
        x1 = 0;
        f1 = -4;

        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        assertEquals(-2.0, solver.getRoot(), 1e-6);

        x0 = 1;
        f0 = -3;
        x1 = 1.5;
        f1 = -0.75;

        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        assertEquals(2.0, solver.getRoot(), 1e-6);

        x0 = 1;
        f0 = 3;
        x1 = 1.5;
        f1 = 0.75;

        assertTrue(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
        assertEquals(-2.0, solver.getRoot(), 1e-6);

        x0 = 1;
        f0 = -3;
        x1 = 0.5;
        f1 = -3.25;

        assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));

        x0 = 1;
        f0 = 3;
        x1 = 0.5;
        f1 = 3.25;

        assertFalse(solver.findRoot(function, checker, 100, x0, f0, x1, f1));
    }
}

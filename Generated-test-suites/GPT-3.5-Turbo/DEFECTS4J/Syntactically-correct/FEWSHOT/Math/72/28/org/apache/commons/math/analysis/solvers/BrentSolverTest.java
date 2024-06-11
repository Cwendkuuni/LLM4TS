package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.spaceroots.mantissa.functions.FunctionException;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return x * x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
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

        boolean result = solver.findRoot(function, checker, 100, 0, -4, 3, 5);
        assertTrue(result);
        assertEquals(2, solver.getRoot(), 1e-6);

        result = solver.findRoot(function, checker, 100, 0, 0, 3, -12);
        assertTrue(result);
        assertEquals(-2, solver.getRoot(), 1e-6);

        result = solver.findRoot(function, checker, 100, 0, 10, 3, 20);
        assertFalse(result);
    }

}
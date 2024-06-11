package org.spaceroots.mantissa.roots;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                }
                if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                }
                return NONE;
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 1000, -10, -96, 10, 96);

        assertTrue(rootFound);
        assertEquals(2.0, solver.getRoot(), 1e-6);
    }

    @Test
    public void testFindRootInvalidInterval() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                return x * x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double xLow, double fLow, double xHigh, double fHigh) {
                if (Math.abs(fLow) < 1e-6) {
                    return LOW;
                }
                if (Math.abs(fHigh) < 1e-6) {
                    return HIGH;
                }
                return NONE;
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 1000, -10, 96, 10, -96);

        assertFalse(rootFound);
        assertNull(solver.getRoot());
    }

}
package org.spaceroots.mantissa.roots;

import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

import static org.junit.Assert.*;
import org.junit.Test;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x*x - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) throws FunctionException {
                double tolerance = 1e-6;
                if (Math.abs(f0) < tolerance) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(f1) < tolerance) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 100, 1, -3, 2, 0);
        assertTrue(rootFound);
        assertEquals(2.000000000002328, solver.getRoot(), 1e-12);
    }

    @Test
    public void testFindRoot_noRootInInterval() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            public double valueAt(double x) throws FunctionException {
                return x*x + 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            public int converged(double x0, double f0, double x1, double f1) throws FunctionException {
                double tolerance = 1e-6;
                if (Math.abs(f0) < tolerance) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(f1) < tolerance) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NOT_CONVERGED;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        boolean rootFound = solver.findRoot(function, checker, 100, -2, 8, 2, 8);
        assertFalse(rootFound);
    }

}
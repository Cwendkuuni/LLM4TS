package org.apache.commons.cli.test; // Change the package name if needed

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.pow(x, 2) - 2;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrevious, double fxPrevious) {
                return (fx > 0) ? ConvergenceChecker.LOW : ConvergenceChecker.HIGH;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = -2.5;
        double f0 = Math.pow(x0, 2) - 2;
        double x1 = 2.5;
        double f1 = Math.pow(x1, 2) - 2;

        boolean success = solver.findRoot(function, checker, 10, x0, f0, x1, f1);
        assertTrue(success);
        assertEquals(0.0, solver.getRoot(), 0.001);
    }

    @Test(expected = FunctionException.class)
    public void testFindRootWithException() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                throw new FunctionException("Test exception");
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrevious, double fxPrevious) {
                return ConvergenceChecker.CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 0.0;
        double f0 = 0.0;
        double x1 = 1.0;
        double f1 = 1.0;

        solver.findRoot(function, checker, 10, x0, f0, x1, f1);
    }

}
package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.FunctionException;

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
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                double tol = 0.0001;
                if (Math.abs(fx) < tol || Math.abs(x - xPrev) < tol) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(fxPrev) < Math.abs(fx) && Math.abs(x - xPrev) > tol) {
                    return ConvergenceChecker.HIGH;
                }
                return ConvergenceChecker.UNDETERMINED;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 1.0;
        double f0 = Math.pow(x0, 2) - 2;
        double x1 = 2.0;
        double f1 = Math.pow(x1, 2) - 2;

        boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
        assertTrue(rootFound);
        assertEquals(-1.0, solver.getRoot(), 0.001);
    }

    @Test
    public void testFindRootDivergence() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.pow(x, 2);
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.UNDETERMINED;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 1.0;
        double f0 = Math.pow(x0, 2);
        double x1 = 2.0;
        double f1 = Math.pow(x1, 2);

        boolean rootFound = solver.findRoot(function, checker, 100, x0, f0, x1, f1);
        assertFalse(rootFound);
    }

    @Test(expected = FunctionException.class)
    public void testFindRootException() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                throw new ArithmeticException("Divide by zero");
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.UNDETERMINED;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 1.0;
        double f0 = Double.NaN;
        double x1 = 2.0;
        double f1 = 1.0;

        solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    }
}
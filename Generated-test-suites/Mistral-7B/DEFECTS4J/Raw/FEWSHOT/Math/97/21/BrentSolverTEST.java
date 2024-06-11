package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.pow(x, 2) - 4;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xOld, double fxOld) {
                double tolerance = 0.0001;
                return Math.abs(fx) < tolerance || Math.abs(x - xOld) < tolerance ? ConvergenceChecker.CONVERGED : ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 1.0;
        double x1 = 3.0;
        double f0 = function.valueAt(x0);
        double f1 = function.valueAt(x1);
        solver.findRoot(function, checker, 100, x0, f0, x1, f1);
        assertEquals(2.0, solver.getRoot(), 0.001);
    }

    @Test
    public void testFindRootWithConvergenceException() throws FunctionException {
        ComputableFunction function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                throw new FunctionException("Function error");
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xOld, double fxOld) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = 1.0;
        double x1 = 3.0;
        double f0 = Double.NaN;
        double f1 = Double.NaN;
        try {
            solver.findRoot(function, checker, 100, x0, f0, x1, f1);
            fail();
        } catch (AssertionError e) {
            // expected
        }
    }
}
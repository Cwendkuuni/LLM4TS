package org.apache.commons.cli;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        MockFunction function = new MockFunction();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xLow, double fLow) {
                return ConvergenceChecker.LOW;
            }
        };

        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 2.0;
        double f1 = function.valueAt(x1);

        solver.findRoot(function, checker, 100, x0, f0, x1, f1);

        assertEquals(x1, solver.getRoot(), 0.001);
    }

    @Test(expected = FunctionException.class)
    public void testFindRootException() throws FunctionException {
        MockFunction function = new MockFunction() {
            @Override
            public double valueAt(double x) {
                throw new FunctionException("Function error");
            }
        };
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xLow, double fLow) {
                return ConvergenceChecker.LOW;
            }
        };

        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 2.0;
        double f1 = function.valueAt(x1);

        solver.findRoot(function, checker, 100, x0, f0, x1, f1);
    }

    @Test
    public void testFindRootConvergenceChecker() throws FunctionException {
        MockFunction function = new MockFunction();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xLow, double fLow) {
                return ConvergenceChecker.HIGH;
            }
        };

        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 2.0;
        double f1 = function.valueAt(x1);

        solver.findRoot(function, checker, 100, x0, f0, x1, f1);

        assertNotNull(solver.getRoot());
    }

    @Test
    public void testFindRootMaxIter() throws FunctionException {
        MockFunction function = new MockFunction();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double f, double xLow, double fLow) {
                return ConvergenceChecker.LOW;
            }
        };

        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double f0 = function.valueAt(x0);
        double x1 = 2.0;
        double f1 = function.valueAt(x1);

        solver.findRoot(function, checker, 0, x0, f0, x1, f1);

        assertFalse(solver.getRoot() == Double.NaN);
    }
}
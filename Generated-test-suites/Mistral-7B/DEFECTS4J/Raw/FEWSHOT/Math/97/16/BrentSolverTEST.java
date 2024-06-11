package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.MockFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws Exception {
        MockFunction function = new MockFunction();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xOld, double fxOld) {
                return ConvergenceChecker.LOW;
            }
        };

        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double fx0 = function.valueAt(x0);
        double x1 = 2.0;
        double fx1 = function.valueAt(x1);

        assertTrue(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
        assertEquals(1.5, solver.getRoot(), 0.001);
    }

    @Test
    public void testFindRootFail() throws Exception {
        MockFunction function = new MockFunction();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xOld, double fxOld) {
                return ConvergenceChecker.HIGH;
            }
        };

        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double fx0 = function.valueAt(x0);
        double x1 = 2.0;
        double fx1 = function.valueAt(x1);

        assertFalse(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
    }

    @Test
    public void testFindRootConverged() throws Exception {
        MockFunction function = new MockFunction();
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xOld, double fxOld) {
                return (Math.abs(fx) < 0.001) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
            }
        };

        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double fx0 = function.valueAt(x0);
        double x1 = 2.0;
        double fx1 = function.valueAt(x1);

        assertTrue(solver.findRoot(function, checker, 100, x0, fx0, x1, fx1));
        assertEquals(1.5, solver.getRoot(), 0.001);
    }

    @Test(expected = FunctionException.class)
    public void testFindRootFunctionException() throws Exception {
        MockFunction function = new MockFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Function error");
            }
        };

        BrentSolver solver = new BrentSolver();

        double x0 = 1.0;
        double fx0 = Double.NaN;
        double x1 = 2.0;
        double fx1 = Double.NaN;

        assertFalse(solver.findRoot(function, null, 100, x0, fx0, x1, fx1));
    }
}
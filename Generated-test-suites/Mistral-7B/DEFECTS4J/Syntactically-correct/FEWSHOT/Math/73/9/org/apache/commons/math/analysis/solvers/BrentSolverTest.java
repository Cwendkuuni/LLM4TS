package org.apache.commons.cli.test;

import static org.junit.Assert.*;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.RationalFunction;
import org.spaceroots.mantissa.roots.BrentSolver;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

public class BrentSolverTest {

    @Test
    public void testBrentSolverWithSimpleFunction() throws FunctionException {
        ComputableFunction function = new RationalFunction("x^2 + 1", "x");
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double x2, double fx2) {
                return (Math.abs(fx) < 0.001) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
            }
        };
        BrentSolver solver = new BrentSolver();

        double x0 = -3.0;
        double x1 = 3.0;
        boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertTrue(result);
        double root = solver.getRoot();
        assertEquals(0.0, root, 0.001);
    }

    @Test
    public void testBrentSolverWithDivergentFunction() throws FunctionException {
        ComputableFunction function = new RationalFunction("x^2 - 1", "x");
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double x2, double fx2) {
                return ConvergenceChecker.NONE;
            }
        };
        BrentSolver solver = new BrentSolver();

        double x0 = 0.0;
        double x1 = 2.0;
        boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertFalse(result);
    }

    @Test
    public void testBrentSolverWithFunctionWithTwoRoots() throws FunctionException {
        ComputableFunction function = new RationalFunction("x^3 - 2*x^2 + 2", "x");
        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double x2, double fx2) {
                return (Math.abs(fx) < 0.001) ? ConvergenceChecker.LOW : ConvergenceChecker.NONE;
            }
        };
        BrentSolver solver = new BrentSolver();

        double x0 = -2.0;
        double x1 = 2.0;
        boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertTrue(result);
        double root1 = solver.getRoot();
        assertEquals(-1.0, root1, 0.001);
        result = solver.findRoot(function, checker, 100, x1, function.valueAt(x1), x0, function.valueAt(x0));
        assertTrue(result);
        double root2 = solver.getRoot();
        assertEquals(1.0, root2, 0.001);
    }
}
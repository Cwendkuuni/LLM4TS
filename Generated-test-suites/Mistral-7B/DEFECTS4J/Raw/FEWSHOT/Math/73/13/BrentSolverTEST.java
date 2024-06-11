package org.apache.commons.cli;

import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.roots.ConvergenceChecker;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                double tolerance = 1e-6;
                if (Math.abs(fx) < tolerance) {
                    return ConvergenceChecker.LOW;
                } else if (Math.abs(fx - fxPrev) < tolerance) {
                    return ConvergenceChecker.HIGH;
                } else {
                    return ConvergenceChecker.NEITHER;
                }
            }
        };

        BrentSolver solver = new BrentSolver();
        double x0 = -2.0;
        double x1 = 2.0;
        double root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

        assertEquals(-1.4142135625, root, 1e-6);

        function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.sin(Math.PI * x) - x;
            }
        };
        solver = new BrentSolver();
        x0 = 0.5;
        x1 = 1.5;
        root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)) ? solver.getRoot() : Double.NaN;

        assertEquals(0.8646831215, root, 1e-6);

        function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                return Math.cos(Math.PI * x) - 1;
            }
        };
        solver = new BrentSolver();
        x0 = -1.0;
        x1 = 0.0;
        root = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
        assertFalse(solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1)));
        assertNull(solver.getRoot());

        function = new ComputableFunction() {
            @Override
            public double valueAt(double x) {
                throw new FunctionException("test");
            }
        };
        solver = new BrentSolver();
        x0 = 0.0;
        x1 = 0.0;
        try {
            solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));
            fail();
        } catch (FunctionException e) {
            // expected
        }
        assertNull(solver.getRoot());
    }
}
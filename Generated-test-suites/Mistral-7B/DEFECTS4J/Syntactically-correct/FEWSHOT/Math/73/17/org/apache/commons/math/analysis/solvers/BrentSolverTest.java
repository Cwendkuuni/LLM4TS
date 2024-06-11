package org.apache.commons.cli;

import org.junit.Assert;
import org.junit.Test;
import org.spaceroots.mantissa.functions.scalar.ComputableFunction;
import org.spaceroots.mantissa.functions.scalar.SimpleFunction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class BrentSolverTest {

    @Test
    public void testFindRoot() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        ComputableFunction function = new SimpleFunction() {
            @Override
            public double valueAt(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                double tol = 1e-5;
                if (Math.abs(fx) < Double.MIN_VALUE) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(x - xPrev) < tol) {
                    return ConvergenceChecker.HIGH;
                }
                if (Math.abs(fx) < tol * Math.max(Math.abs(x), Math.abs(xPrev))) {
                    return ConvergenceChecker.CONVERGED;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        double x0 = 1.0;
        double x1 = 3.0;
        double root = 2.0;

        boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

        assertEquals(root, solver.getRoot(), epsilon);
        assertFalse(Double.isNaN(solver.getRoot()));
        assertFalse(result);
    }

    @Test(expected = FunctionException.class)
    public void testFindRootWithException() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        ComputableFunction function = new SimpleFunction() {
            @Override
            public double valueAt(double x) throws FunctionException {
                throw new FunctionException("Exception");
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        double x0 = 1.0;
        double x1 = 3.0;

        solver.findRoot(function, checker, 100, x0, Double.NaN, x1, Double.NaN);
    }

    @Test
    public void testFindRootWithDifferentInterval() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        ComputableFunction function = new SimpleFunction() {
            @Override
            public double valueAt(double x) {
                return Math.pow(x, 2) - 4.0;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                double tol = 1e-5;
                if (Math.abs(fx) < Double.MIN_VALUE) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(x - xPrev) < tol) {
                    return ConvergenceChecker.HIGH;
                }
                if (Math.abs(fx) < tol * Math.max(Math.abs(x), Math.abs(xPrev))) {
                    return ConvergenceChecker.CONVERGED;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        double x0 = 0.1;
        double x1 = 1.0;
        double root = 0.5;

        boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

        assertEquals(root, solver.getRoot(), epsilon);
        assertFalse(Double.isNaN(solver.getRoot()));
        assertFalse(result);
    }

    @Test
    public void testFindRootWithSimpleFunction() throws FunctionException {
        BrentSolver solver = new BrentSolver();

        ComputableFunction function = new SimpleFunction() {
            @Override
            public double valueAt(double x) {
                return Math.sin(x) - 0.5;
            }
        };

        ConvergenceChecker checker = new ConvergenceChecker() {
            @Override
            public int converged(double x, double fx, double xPrev, double fxPrev) {
                double tol = 1e-5;
                if (Math.abs(fx) < Double.MIN_VALUE) {
                    return ConvergenceChecker.LOW;
                }
                if (Math.abs(x - xPrev) < tol) {
                    return ConvergenceChecker.HIGH;
                }
                if (Math.abs(fx) < tol * Math.max(Math.abs(x), Math.abs(xPrev))) {
                    return ConvergenceChecker.CONVERGED;
                }
                return ConvergenceChecker.NOT_CONVERGED;
            }
        };

        double x0 = 0.0;
        double x1 = 2.0 * Math.PI;

        boolean result = solver.findRoot(function, checker, 100, x0, function.valueAt(x0), x1, function.valueAt(x1));

        double root = Math.PI;
        assertEquals(root, solver.getRoot(), epsilon);
        assertFalse(Double.isNaN(solver.getRoot()));
        assertFalse(result);
    }

    @Test
    public void testGetRoot() {
        BrentSolver solver = new BrentSolver();
        assertNull(solver.getRoot());
    }
}